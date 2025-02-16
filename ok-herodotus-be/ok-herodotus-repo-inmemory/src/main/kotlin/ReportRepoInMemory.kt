package ru.otus.otuskotlin.herodotus.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import ru.otus.otuskotlin.herodotus.common.models.ApplicationId
import ru.otus.otuskotlin.herodotus.common.models.Report
import ru.otus.otuskotlin.herodotus.common.models.ReportId
import ru.otus.otuskotlin.herodotus.common.models.ReportSearchFilter
import ru.otus.otuskotlin.herodotus.common.repo.*
import ru.otus.otuskotlin.herodotus.repo.common.IRepoReportInitializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class ReportRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : ReportRepoBase(), IRepoReport, IRepoReportInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, ReportEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(reports: Collection<Report>) = reports.map { report ->
        val entity = ReportEntity(report)
        require(entity.reportId != null)
        cache.put(entity.reportId, entity)
        report
    }

    override suspend fun createReport(request: DbReportRequest): IDbReportResponse = tryReportMethod {
        val key = randomUuid()
        val report = request.report.copy(reportId = ReportId(key))
        val entity = ReportEntity(report)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbReportResponseOk(report)
    }

    override suspend fun readReport(request: DbReportIdRequest): IDbReportResponse = tryReportMethod {
        val key = request.reportId.takeIf { it != ReportId.NONE }?.asString() ?: return@tryReportMethod errorEmptyReportId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbReportResponseOk(it.toInternal())
                } ?: errorNotFound(request.reportId)
        }
    }

    override suspend fun deleteReport(request: DbReportIdRequest): IDbReportResponse = tryReportMethod {
        val reportId = request.reportId.takeIf { it != ReportId.NONE } ?: return@tryReportMethod errorEmptyReportId
        val key = reportId.asString()

        mutex.withLock {
            val oldReport = cache.get(key)?.toInternal()
            when {
                oldReport == null -> errorNotFound(reportId)
                else -> {
                    cache.invalidate(key)
                    DbReportResponseOk(oldReport)
                }
            }
        }
    }

    override suspend fun searchReport(request: DbReportFilterRequest): IDbReportsResponse = tryReportsMethod {
        val result: List<Report> = cache.asMap().asSequence()
            .filter { entry ->
                request.applicationId.takeIf { it != ApplicationId.NONE }?.let {
                    it.asString() == entry.value.applicationId
                } ?: true
            }
            .filter { entry ->
                request.events.takeIf { it.isNotEmpty() }?.let {
                    it.any { event -> event.asString() == entry.value.event }
                } ?: true
            }
            .filter { entry ->
                request.searchFields.takeIf { it.isNotEmpty() }?.let {
                    it.any { searchField ->
                        entry.value.content?.get(searchField.fieldName)?.let { value ->
                            when (searchField) {
                                is ReportSearchFilter.NumericSearchField -> {
                                    when (value) {
                                        is JsonPrimitive -> {
                                            value.intOrNull?.let { actualValue ->
                                                when (searchField.action) {
                                                    ReportSearchFilter.SearchAction.EQUALS -> actualValue == searchField.numericValue
                                                    ReportSearchFilter.SearchAction.MORE -> actualValue > searchField.numericValue
                                                    ReportSearchFilter.SearchAction.LESS -> actualValue < searchField.numericValue
                                                    else -> false
                                                }
                                            }
                                        }
                                        else -> false
                                    }
                                }
                                is ReportSearchFilter.StringSearchField -> {
                                    when (value) {
                                        is JsonPrimitive -> {
                                            value.contentOrNull?.let { actualValue ->
                                                when (searchField.action) {
                                                    ReportSearchFilter.SearchAction.EQUALS -> actualValue == searchField.stringValue
                                                    ReportSearchFilter.SearchAction.CONTAINS -> actualValue.contains(searchField.stringValue, ignoreCase = true)
                                                    else -> false
                                                }
                                            }
                                        }
                                        else -> false
                                    }
                                }
                                else -> {
                                    false
                                }
                            }
                        } ?: false
                    }
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbReportsResponseOk(result)
    }
}
