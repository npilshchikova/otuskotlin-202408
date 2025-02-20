package ru.otus.otuskotlin.herodotus.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.herodotus.biz.ReportProcessor
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.models.ReportCommand
import ru.otus.otuskotlin.herodotus.common.models.ReportId
import ru.otus.otuskotlin.herodotus.common.models.WorkMode
import ru.otus.otuskotlin.herodotus.stubs.ReportStub
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationReportIdCorrect(command: ReportCommand, processor: ReportProcessor) = runTest {
    val context = ReportContext(
        command = command,
        state = JobState.NONE,
        workMode = WorkMode.TEST,
        reportRequest = ReportStub.prepareReport(
            reportId = ReportId("Test"),
        ),
    )
    processor.exec(context)
    assertEquals(0, context.errors.size)
    assertNotEquals(JobState.FAILING, context.state)
    assertEquals(ReportId("Test"), context.reportValidated.reportId)
}

fun validationReportIdTrim(command: ReportCommand, processor: ReportProcessor) = runTest {
    val context = ReportContext(
        command = command,
        state = JobState.NONE,
        workMode = WorkMode.TEST,
        reportRequest = ReportStub.prepareReport(
            reportId = ReportId("\n\tTest\n\t"),
        ),
    )
    processor.exec(context)
    assertEquals(0, context.errors.size)
    assertNotEquals(JobState.FAILING, context.state)
    assertEquals(ReportId("Test"), context.reportValidated.reportId)
}

fun validationReportIdEmpty(command: ReportCommand, processor: ReportProcessor) = runTest {
    val context = ReportContext(
        command = command,
        state = JobState.NONE,
        workMode = WorkMode.TEST,
        reportRequest = ReportStub.prepareReport(
            reportId = ReportId("\n\t\n\t"),
        ),
    )
    processor.exec(context)
    assertEquals(1, context.errors.size)
    assertEquals(JobState.FAILING, context.state)
    val error = context.errors.firstOrNull()
    assertEquals("reportId", error?.field)
}
