package ru.otus.otuskotlin.herodotus.biz.validation

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.herodotus.biz.ReportProcessor
import ru.otus.otuskotlin.herodotus.common.NONE
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.models.ReportCommand
import ru.otus.otuskotlin.herodotus.common.models.WorkMode
import ru.otus.otuskotlin.herodotus.stubs.ReportStub
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationTimestampCorrect(command: ReportCommand, processor: ReportProcessor) = runTest {
    val context = ReportContext(
        command = command,
        state = JobState.NONE,
        workMode = WorkMode.TEST,
        reportRequest = ReportStub.prepareReport(
            timestamp = Instant.parse("2018-03-20T09:12:28Z"),
        ),
    )
    processor.exec(context)
    assertEquals(0, context.errors.size)
    assertNotEquals(JobState.FAILING, context.state)
    assertEquals(Instant.parse("2018-03-20T09:12:28Z"), context.reportValidated.timestamp)
}

fun validationTimestampEmpty(command: ReportCommand, processor: ReportProcessor) = runTest {
    val context = ReportContext(
        command = command,
        state = JobState.NONE,
        workMode = WorkMode.TEST,
        reportRequest = ReportStub.prepareReport(
            timestamp = Instant.NONE,
        ),
    )
    processor.exec(context)
    assertEquals(1, context.errors.size)
    assertEquals(JobState.FAILING, context.state)
    val error = context.errors.firstOrNull()
    assertEquals("timestamp", error?.field)
}
