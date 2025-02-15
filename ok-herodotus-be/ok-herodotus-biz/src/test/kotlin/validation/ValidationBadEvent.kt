package ru.otus.otuskotlin.herodotus.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.herodotus.biz.ReportProcessor
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.*
import ru.otus.otuskotlin.herodotus.stubs.ReportStub
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = ReportStub.get()

fun validationEventCorrect(command: ReportCommand, processor: ReportProcessor) = runTest {
    val context = ReportContext(
        command = command,
        state = JobState.NONE,
        workMode = WorkMode.TEST,
        reportRequest = Report(
            reportId = stub.reportId,
            applicationId = stub.applicationId,
            event = Event("Test"),
            timestamp = stub.timestamp,
            content = stub.content,
        ),
    )
    processor.exec(context)
    assertEquals(0, context.errors.size)
    assertNotEquals(JobState.FAILING, context.state)
    assertEquals(Event("Test"), context.reportValidated.event)
}

fun validationEventTrim(command: ReportCommand, processor: ReportProcessor) = runTest {
    val context = ReportContext(
        command = command,
        state = JobState.NONE,
        workMode = WorkMode.TEST,
        reportRequest = Report(
            reportId = stub.reportId,
            applicationId = stub.applicationId,
            event = Event("\t\tTest    "),
            timestamp = stub.timestamp,
            content = stub.content,
        ),
    )
    processor.exec(context)
    assertEquals(0, context.errors.size)
    assertNotEquals(JobState.FAILING, context.state)
    assertEquals(Event("Test"), context.reportValidated.event)
}

fun validationEventEmpty(command: ReportCommand, processor: ReportProcessor) = runTest {
    val context = ReportContext(
        command = command,
        state = JobState.NONE,
        workMode = WorkMode.TEST,
        reportRequest = Report(
            reportId = stub.reportId,
            applicationId = stub.applicationId,
            event = Event("    "),
            timestamp = stub.timestamp,
            content = stub.content,
        ),
    )
    processor.exec(context)
    assertEquals(1, context.errors.size)
    assertEquals(JobState.FAILING, context.state)
    val error = context.errors.firstOrNull()
    assertEquals("event", error?.field)
}
