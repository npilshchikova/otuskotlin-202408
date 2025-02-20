package ru.otus.otuskotlin.herodotus.biz.repo

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import ru.otus.otuskotlin.herodotus.biz.ReportProcessor
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import ru.otus.otuskotlin.herodotus.common.models.*
import ru.otus.otuskotlin.herodotus.common.repo.DbReportResponseOk
import ru.otus.otuskotlin.herodotus.common.repo.errorNotFound
import ru.otus.otuskotlin.herodotus.repo.tests.ReportRepositoryMock
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initReport = Report(
    reportId = ReportId("report-repo-test"),
    applicationId = ApplicationId("Test"),
    event = Event("Test"),
    timestamp =  Instant.fromEpochMilliseconds(123456),
    content = buildJsonObject {
        put("sampleId", "Test")
        put("case", "one more really stupid stub")
        put("info", "Test")
    }
)

private val repo = ReportRepositoryMock(
    invokeReadReport = {
        if (it.reportId == initReport.reportId) {
            DbReportResponseOk(
                data = initReport,
            )
        } else errorNotFound(it.reportId)
    }
)

private val settings = ReportCorSettings(repoTest = repo)
private val processor = ReportProcessor(settings)

fun repoNotFoundTest(command: ReportCommand) = runTest {
    val context = ReportContext(
        command = command,
        state = JobState.NONE,
        workMode = WorkMode.TEST,
        reportRequest = Report(
            reportId = ReportId("report-repo-test-other"),
            applicationId = ApplicationId("Test"),
            event = Event("Test"),
            timestamp =  Instant.fromEpochMilliseconds(1234567),
            content = buildJsonObject {
                put("sampleId", "Test")
                put("case", "one more really stupid stub")
                put("info", "Test")
            }
        )
    )
    processor.exec(context)
    assertEquals(JobState.FAILING, context.state)
    assertEquals(Report(), context.reportResponse)
    assertEquals(1, context.errors.size)
    assertNotNull(context.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
