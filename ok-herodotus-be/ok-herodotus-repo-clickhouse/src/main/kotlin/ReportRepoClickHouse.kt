package ru.otus.otuskotlin.herodotus.repo.clickhouse

import com.benasher44.uuid.uuid4
import com.clickhouse.client.api.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.otus.otuskotlin.herodotus.common.models.Report
import ru.otus.otuskotlin.herodotus.common.models.ReportId
import ru.otus.otuskotlin.herodotus.common.repo.*
import ru.otus.otuskotlin.herodotus.repo.common.IRepoReportInitializable
import java.util.concurrent.TimeUnit

class ReportRepoClickHouse (
    properties: DbProperties,
    private val randomUuid: () -> String = { uuid4().toString() }
) : ReportRepoBase(), IRepoReport, IRepoReportInitializable {

    private val reportTable: String = "report"

    private val client: Client = Client.Builder()
        .addEndpoint("https://${properties.host}:${properties.port}/")
        .setUsername(properties.user)
        .setPassword(properties.password)
        .build().also {
            it.register(ReportTableRecord::class.java, it.getTableSchema(reportTable))
        }

    override fun save(reports: Collection<Report>): Collection<Report> {
        try {
            client.insert(
                reportTable,
                reports.map { report -> ReportTableRecord(report) }
            )
            return reports
        } catch (e : Exception) {
            return listOf()
        }
    }

    override suspend fun createReport(request: DbReportRequest): IDbReportResponse = tryReportMethod {
        val key = randomUuid()
        val report = request.report.copy(reportId = ReportId(key))
        val record = ReportTableRecord(report)
        withContext(Dispatchers.IO) {
            client.insert(reportTable, listOf(record))
        }
        DbReportResponseOk(report)
    }

    override suspend fun readReport(request: DbReportIdRequest): IDbReportResponse = tryReportMethod {
        val key = request.reportId.takeIf { it != ReportId.NONE }?.asString() ?: return@tryReportMethod errorEmptyReportId

        val sql = "select * from $reportTable where reportId = $key"

        // Default format is RowBinaryWithNamesAndTypesFormatReader so reader have all information about columns
        withContext(Dispatchers.IO) {
            client.query(sql)[3, TimeUnit.SECONDS].use { response ->

                // Create a reader to access the data in a convenient way
                val reader = client.newBinaryFormatReader(response)
                if (reader.hasNext()) {
                    // Read the next record from stream and parse it
                    reader.next()

                    // get values
                    DbReportResponseOk(
                        ReportTableRecord(
                            reportId = reader.getString("reportId"),
                            applicationId = reader.getString("applicationId"),
                            event = reader.getString("event"),
                            timestamp = reader.getInstant("timestamp"),
                            content = reader.getString("content")
                        ).toInternal()
                    )
                } else {
                    errorNotFound(request.reportId)
                }

            }
        }
    }

    override suspend fun deleteReport(request: DbReportIdRequest): IDbReportResponse = tryReportMethod {
        val reportId = request.reportId.takeIf { it != ReportId.NONE } ?: return@tryReportMethod errorEmptyReportId
        val key = reportId.asString()

        val result = readReport(request)
        val sql = "ALTER TABLE $reportTable DELETE WHERE reportId = $key"

        withContext(Dispatchers.IO) {
            client.query(sql)[3, TimeUnit.SECONDS]
        }
        result
    }

    override suspend fun searchReport(request: DbReportFilterRequest): IDbReportsResponse = tryReportsMethod {
        val result: MutableList<ReportTableRecord> = mutableListOf()

        val sql = "select * from $reportTable where applicationId = ${request.applicationId}"

        // Default format is RowBinaryWithNamesAndTypesFormatReader so reader have all information about columns
        withContext(Dispatchers.IO) {
            client.query(sql)[3, TimeUnit.SECONDS].use { response ->

                // Create a reader to access the data in a convenient way
                val reader = client.newBinaryFormatReader(response)
                while (reader.hasNext()) {
                    // Read the next record from stream and parse it
                    reader.next()

                    // get values
                    result.add(
                        ReportTableRecord(
                            reportId = reader.getString("reportId"),
                            applicationId = reader.getString("applicationId"),
                            event = reader.getString("event"),
                            timestamp = reader.getInstant("timestamp"),
                            content = reader.getString("content")
                        )
                    )
                }

            }
        }

        DbReportsResponseOk(result.map { it.toInternal() })
    }
}
