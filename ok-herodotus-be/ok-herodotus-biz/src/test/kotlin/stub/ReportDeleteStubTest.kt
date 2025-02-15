package ru.otus.otuskotlin.herodotus.biz.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.herodotus.biz.ReportProcessor
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.*
import ru.otus.otuskotlin.herodotus.common.stubs.ReportStubs
import ru.otus.otuskotlin.herodotus.stubs.ReportStub
import kotlin.test.Test
import kotlin.test.assertEquals

class ReportDeleteStubTest {

    private val processor = ReportProcessor()
    private val reportId = ReportId("test")

    @Test
    fun delete() = runTest {
        val context = ReportContext(
            command = ReportCommand.DELETE,
            state = JobState.NONE,
            workMode = WorkMode.STUB,
            stubCase = ReportStubs.SUCCESS,
            reportRequest = Report(
                reportId = reportId,
            ),
        )
        processor.exec(context)

        val stub = ReportStub.get()
        assertEquals(reportId, context.reportResponse.reportId)
        assertEquals(stub.applicationId, context.reportResponse.applicationId)
        assertEquals(stub.event, context.reportResponse.event)
        assertEquals(stub.timestamp, context.reportResponse.timestamp)
        assertEquals(stub.content, context.reportResponse.content)
    }

    @Test
    fun notFound() = runTest {
        val context = ReportContext(
            command = ReportCommand.DELETE,
            state = JobState.NONE,
            workMode = WorkMode.STUB,
            stubCase = ReportStubs.NOT_FOUND,
            reportRequest = Report(
                reportId = reportId,
            ),
        )
        processor.exec(context)
        assertEquals(Report(), context.reportResponse)
        assertEquals("internal", context.errors.firstOrNull()?.group)
    }

    @Test
    fun cannotDelete() = runTest {
        val context = ReportContext(
            command = ReportCommand.DELETE,
            state = JobState.NONE,
            workMode = WorkMode.STUB,
            stubCase = ReportStubs.CANNOT_DELETE,
            reportRequest = Report(
                reportId = reportId,
            ),
        )
        processor.exec(context)
        assertEquals(Report(), context.reportResponse)
        assertEquals("internal", context.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val context = ReportContext(
            command = ReportCommand.DELETE,
            state = JobState.NONE,
            workMode = WorkMode.STUB,
            stubCase = ReportStubs.DB_ERROR,
            reportRequest = Report(
                reportId = reportId,
            ),
        )
        processor.exec(context)
        assertEquals(Report(), context.reportResponse)
        assertEquals("internal", context.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val context = ReportContext(
            command = ReportCommand.DELETE,
            state = JobState.NONE,
            workMode = WorkMode.STUB,
            stubCase = ReportStubs.UNKNOWN_APPLICATION,
            reportRequest = Report(
                reportId = reportId,
            ),
        )
        processor.exec(context)
        assertEquals(Report(), context.reportResponse)
        assertEquals("stub", context.errors.firstOrNull()?.field)
    }
}
