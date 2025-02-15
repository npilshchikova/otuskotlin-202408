package ru.otus.otuskotlin.herodotus.biz.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.herodotus.biz.ReportProcessor
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.*
import ru.otus.otuskotlin.herodotus.common.stubs.ReportStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class ReportSearchStubTest {

    private val processor = ReportProcessor()
    private val applicationId = ApplicationId("TestApp")
    private val event = Event("New sample")

    @Test
    fun read() = runTest {
        val context = ReportContext(
            command = ReportCommand.RESUME,
            state = JobState.NONE,
            workMode = WorkMode.STUB,
            stubCase = ReportStubs.SUCCESS,
            reportFilterRequest = ReportSearchFilter(
                applicationId = applicationId,
                events = listOf(event)
            ),
        )
        processor.exec(context)

        assertEquals(2, context.reportsResponse.size)
        context.reportsResponse.forEach {
            assertEquals(applicationId, it.applicationId)
            assertEquals(event, it.event)
        }
    }

    @Test
    fun unknownApplication() = runTest {
        val context = ReportContext(
            command = ReportCommand.RESUME,
            state = JobState.NONE,
            workMode = WorkMode.STUB,
            stubCase = ReportStubs.UNKNOWN_APPLICATION,
            reportFilterRequest = ReportSearchFilter(
                applicationId = applicationId,
                events = listOf(event)
            ),
        )
        processor.exec(context)
        assertEquals(0, context.reportsResponse.size)
        assertEquals("applicationId", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun unknownEvent() = runTest {
        val context = ReportContext(
            command = ReportCommand.RESUME,
            state = JobState.NONE,
            workMode = WorkMode.STUB,
            stubCase = ReportStubs.UNKNOWN_EVENT,
            reportFilterRequest = ReportSearchFilter(
                applicationId = applicationId,
                events = listOf(event)
            ),
        )
        processor.exec(context)
        assertEquals(0, context.reportsResponse.size)
        assertEquals("event", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val context = ReportContext(
            command = ReportCommand.RESUME,
            state = JobState.NONE,
            workMode = WorkMode.STUB,
            stubCase = ReportStubs.UNKNOWN_EVENT,
            reportFilterRequest = ReportSearchFilter(
                applicationId = applicationId,
                events = listOf(event)
            ),
        )
        processor.exec(context)
        assertEquals(0, context.reportsResponse.size)
        assertEquals("internal", context.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val context = ReportContext(
            command = ReportCommand.RESUME,
            state = JobState.NONE,
            workMode = WorkMode.STUB,
            stubCase = ReportStubs.NOT_FOUND,
            reportFilterRequest = ReportSearchFilter(
                applicationId = applicationId,
                events = listOf(event)
            ),
        )
        processor.exec(context)
        assertEquals(0, context.reportsResponse.size)
        assertEquals("stub", context.errors.firstOrNull()?.field)
    }
}
