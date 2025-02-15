package ru.otus.otuskotlin.herodotus.biz.stub

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import ru.otus.otuskotlin.herodotus.biz.ReportProcessor
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.*
import ru.otus.otuskotlin.herodotus.common.stubs.ReportStubs
import ru.otus.otuskotlin.herodotus.stubs.ReportStub
import kotlin.test.Test
import kotlin.test.assertEquals

class ReportCreateStubTest {

    private val processor = ReportProcessor()
    private val applicationId = ApplicationId("TestApp")
    private val event = Event("New sample")
    private val timestamp = Instant.parse("2018-03-20T09:12:28Z")
    private val content = buildJsonObject {
        put("organization", "ParseqLab")
        put("sampleName", "B26")
        put("analyte", "DNA")
    }

    @Test
    fun create() = runTest {
        val context = ReportContext(
            command = ReportCommand.CREATE,
            state = JobState.NONE,
            workMode = WorkMode.STUB,
            stubCase = ReportStubs.SUCCESS,
            reportRequest = Report(
                applicationId = applicationId,
                event = event,
                timestamp = timestamp,
                content = content,
            ),
        )
        processor.exec(context)
        assertEquals(ReportStub.get().reportId, context.reportResponse.reportId)
        assertEquals(applicationId, context.reportResponse.applicationId)
        assertEquals(event, context.reportResponse.event)
        assertEquals(timestamp, context.reportResponse.timestamp)
        assertEquals(content, context.reportResponse.content)
    }

    @Test
    fun unknownApp() = runTest {
        val context = ReportContext(
            command = ReportCommand.CREATE,
            state = JobState.NONE,
            workMode = WorkMode.STUB,
            stubCase = ReportStubs.UNKNOWN_APPLICATION,
            reportRequest = Report(
                applicationId = applicationId,
                event = event,
                timestamp = timestamp,
                content = content,
            ),
        )
        processor.exec(context)
        assertEquals(Report(), context.reportResponse)
        assertEquals("applicationId", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }
    @Test
    fun unknownEvent() = runTest {
        val context = ReportContext(
            command = ReportCommand.CREATE,
            state = JobState.NONE,
            workMode = WorkMode.STUB,
            stubCase = ReportStubs.UNKNOWN_EVENT,
            reportRequest = Report(
                applicationId = applicationId,
                event = event,
                timestamp = timestamp,
                content = content,
            ),
        )
        processor.exec(context)
        assertEquals(Report(), context.reportResponse)
        assertEquals("event", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val context = ReportContext(
            command = ReportCommand.CREATE,
            state = JobState.NONE,
            workMode = WorkMode.STUB,
            stubCase = ReportStubs.DB_ERROR,
            reportRequest = Report(
                applicationId = applicationId,
                event = event,
                timestamp = timestamp,
                content = content,
            ),
        )
        processor.exec(context)
        assertEquals(Report(), context.reportResponse)
        assertEquals("internal", context.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val context = ReportContext(
            command = ReportCommand.CREATE,
            state = JobState.NONE,
            workMode = WorkMode.STUB,
            stubCase = ReportStubs.NOT_FOUND,
            reportRequest = Report(
                applicationId = applicationId,
                event = event,
                timestamp = timestamp,
                content = content,
            ),
        )
        processor.exec(context)
        assertEquals(Report(), context.reportResponse)
        assertEquals("stub", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }
}
