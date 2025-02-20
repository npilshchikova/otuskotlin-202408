package ru.otus.otuskotlin.herodotus.biz.repo

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import ru.otus.otuskotlin.herodotus.biz.ReportProcessor
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import ru.otus.otuskotlin.herodotus.common.models.*
import ru.otus.otuskotlin.herodotus.common.repo.DbReportResponseErr
import ru.otus.otuskotlin.herodotus.common.repo.DbReportResponseOk
import ru.otus.otuskotlin.herodotus.repo.tests.ReportRepositoryMock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BizRepoDeleteTest {

    private val command = ReportCommand.DELETE
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
            DbReportResponseOk(
                data = initReport,
            )
        },
        invokeDeleteReport = {
            if (it.reportId == initReport.reportId)
                DbReportResponseOk(
                    data = initReport
                )
            else DbReportResponseErr()
        }
    )
    private val settings by lazy {
        ReportCorSettings(
            repoTest = repo
        )
    }
    private val processor = ReportProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val toDelete = Report(
            reportId = ReportId("report-repo-test"),
        )
        val context = ReportContext(
            command = command,
            state = JobState.NONE,
            workMode = WorkMode.TEST,
            reportRequest = toDelete,
        )
        processor.exec(context)
        assertEquals(JobState.FINISHING, context.state)
        assertTrue { context.errors.isEmpty() }
        assertEquals(initReport.reportId, context.reportResponse.reportId)
        assertEquals(initReport.applicationId, context.reportResponse.applicationId)
        assertEquals(initReport.event, context.reportResponse.event)
        assertEquals(initReport.timestamp, context.reportResponse.timestamp)
        assertEquals(initReport.content, context.reportResponse.content)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
