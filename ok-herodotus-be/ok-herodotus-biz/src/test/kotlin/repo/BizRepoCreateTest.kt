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
import ru.otus.otuskotlin.herodotus.repo.tests.ReportRepositoryMock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val command = ReportCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = ReportRepositoryMock(
        invokeCreateReport = {
            DbReportResponseOk(
                data = Report(
                    reportId = ReportId(uuid),
                    applicationId = it.report.applicationId,
                    event = it.report.event,
                    timestamp = it.report.timestamp,
                    content = it.report.content,
                )
            )
        }
    )
    private val settings = ReportCorSettings(
        repoTest = repo
    )
    private val processor = ReportProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val context = ReportContext(
            command = command,
            state = JobState.NONE,
            workMode = WorkMode.TEST,
            reportRequest = Report(
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
        assertEquals(JobState.FINISHING, context.state)
        assertNotEquals(ReportId.NONE, context.reportResponse.reportId)
        assertEquals("Test", context.reportResponse.applicationId.asString())
        assertEquals("Test", context.reportResponse.event.asString())
    }
}
