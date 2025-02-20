package ru.otus.otuskotlin.herodotus.repo.tests

import kotlinx.datetime.Instant
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import ru.otus.otuskotlin.herodotus.common.models.ApplicationId
import ru.otus.otuskotlin.herodotus.common.models.Event
import ru.otus.otuskotlin.herodotus.common.models.Report
import ru.otus.otuskotlin.herodotus.common.models.ReportId
import ru.otus.otuskotlin.herodotus.common.repo.*
import ru.otus.otuskotlin.herodotus.repo.common.IRepoReportInitializable
import kotlin.test.*


abstract class RepoReportCreateTest {
    abstract val repo: IRepoReportInitializable
    protected open val uuidNew = ReportId("10000000-0000-0000-0000-000000000001")

    private val createObj = Report(
        applicationId = ApplicationId("Test"),
        event = Event("Test"),
        timestamp = Instant.fromEpochMilliseconds(123456),
        content = buildJsonObject {
            put("sampleId", "test")
            put("case", "create object")
            put("info", "Why should I repeat this initialization one more time???")
        }
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createReport(DbReportRequest(createObj))
        val expected = createObj
        assertIs<DbReportResponseOk>(result)
        assertEquals(uuidNew, result.data.reportId)
        assertEquals(expected.applicationId, result.data.applicationId)
        assertEquals(expected.event, result.data.event)
        assertEquals(expected.timestamp, result.data.timestamp)
        assertNotEquals(ReportId.NONE, result.data.reportId)
    }

    companion object : BaseInitReports("create") {
        override val initObjects: List<Report> = emptyList()
    }
}
