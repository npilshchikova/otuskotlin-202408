package ru.otus.otuskotlin.herodotus.biz.repo

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import ru.otus.otuskotlin.herodotus.biz.ReportProcessor
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import ru.otus.otuskotlin.herodotus.common.models.*
import ru.otus.otuskotlin.herodotus.common.repo.DbReportsResponseOk
import ru.otus.otuskotlin.herodotus.repo.tests.ReportRepositoryMock
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoResumeTest {

    private val command = ReportCommand.RESUME
    private val initReport = Report(
        reportId = ReportId("report-repo-test"),
        applicationId = ApplicationId("Test"),
        event = Event("Test"),
        timestamp = Instant.fromEpochMilliseconds(123456),
        content = buildJsonObject {
            put("sampleId", "Test")
            put("case", "one more really stupid stub")
            put("info", "Test")
        }
    )
    private val repo = ReportRepositoryMock(
        invokeSearchReport = {
            DbReportsResponseOk(
                data = listOf(initReport)
            )
        }
    )
    private val settings = ReportCorSettings(repoTest = repo)
    private val processor = ReportProcessor(settings)

    @Test
    fun repoResumeSuccessTest() = runTest {
        val context = ReportContext(
            command = command,
            state = JobState.NONE,
            workMode = WorkMode.TEST,
            reportFilterRequest = ReportSearchFilter(
                applicationId = ApplicationId("Test")
            ),
            reportResumeRequest = ReportResumeFilter(
                fieldName = "sampleId"
            )
        )
        processor.exec(context)
        assertEquals(JobState.FINISHING, context.state)
        assertEquals(1, context.reportsResponse.size)
        assertEquals(1, context.resumeResponse.itemsNumber)
        assertEquals("Test", context.resumeResponse.summary.firstOrNull()?.fieldValue)
        assertEquals("Test", context.resumeResponse.summary.firstOrNull()?.event?.asString())
        assertEquals(Instant.fromEpochMilliseconds(123456), context.resumeResponse.summary.firstOrNull()?.timestamp)
    }

    @Test
    fun repoOffersNotFoundTest() = repoNotFoundTest(command)
}
