package ru.otus.otuskotlin.herodotus.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.herodotus.biz.ReportProcessor
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.*
import ru.otus.otuskotlin.herodotus.stubs.ReportStub
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = ReportStub.get()

fun validationApplicationIdCorrect(command: ReportCommand, processor: ReportProcessor) = runTest {
    val context = ReportContext(
        command = command,
        state = JobState.NONE,
        workMode = WorkMode.TEST,
        reportRequest = Report(
            reportId = stub.reportId,
            applicationId = ApplicationId("Test"),
            event = stub.event,
            timestamp = stub.timestamp,
            content = stub.content,
        ),
    )
    processor.exec(context)
    assertEquals(0, context.errors.size)
    assertNotEquals(JobState.FAILING, context.state)
    assertEquals(ApplicationId("Test"), context.reportValidated.applicationId)
}

fun validationApplicationIdTrim(command: ReportCommand, processor: ReportProcessor) = runTest {
    val context = ReportContext(
        command = command,
        state = JobState.NONE,
        workMode = WorkMode.TEST,
        reportRequest = Report(
            reportId = stub.reportId,
            applicationId = ApplicationId("\n\tTest\t\t\t"),
            event = stub.event,
            timestamp = stub.timestamp,
            content = stub.content,
        ),
    )
    processor.exec(context)
    assertEquals(0, context.errors.size)
    assertNotEquals(JobState.FAILING, context.state)
    assertEquals(ApplicationId("Test"), context.reportValidated.applicationId)
}

fun validationApplicationIdEmpty(command: ReportCommand, processor: ReportProcessor) = runTest {
    val context = ReportContext(
        command = command,
        state = JobState.NONE,
        workMode = WorkMode.TEST,
        reportRequest = Report(
            reportId = stub.reportId,
            applicationId = ApplicationId("\n\t\t\t\t"),
            event = stub.event,
            timestamp = stub.timestamp,
            content = stub.content,
        ),
    )
    processor.exec(context)
    assertEquals(1, context.errors.size)
    assertEquals(JobState.FAILING, context.state)
    val error = context.errors.firstOrNull()
    assertEquals("applicationId", error?.field)
}
