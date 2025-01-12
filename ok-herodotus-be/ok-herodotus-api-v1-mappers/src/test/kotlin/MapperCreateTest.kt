package ru.otus.otuskotlin.herodotus.api.v1.mappers

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.Test
import ru.otus.otuskotlin.herodotus.api.v1.models.*
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.*
import ru.otus.otuskotlin.herodotus.common.stubs.Stubs
import kotlin.test.assertEquals

class MapperCreateTest {
    @Test
    fun fromTransport() {
        val request = ReportCreateRequest(
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS,
            ),
            report = ReportCreateObject(
                applicationId = "TestApp",
                event = "New sample",
                timestamp = "2018-03-20T09:12:28Z",
                content = """
                    { 
                        "organization": "ParseqLab",
                        "sampleName": "B26",
                        "analyte": "DNA"
                    }
                """.trimIndent()
            ),
        )

        val context = ReportContext()
        context.fromTransport(request)

        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(WorkMode.STUB, context.workMode)
        assertEquals(ApplicationId("TestApp"), context.reportRequest.applicationId)
        assertEquals(Event("New sample"), context.reportRequest.event)
        assertEquals(Instant.parse("2018-03-20T09:12:28Z"), context.reportRequest.timestamp)
    }

    @Test
    fun toTransport() {
        val context = ReportContext(
            requestId = RequestId("1234"),
            command = ReportCommand.CREATE,
            reportResponse = Report(
                reportId = ReportId(
                    applicationId = ApplicationId("TestApp"),
                    event = Event("New sample"),
                    reportNumber = 1
                ),
                applicationId = ApplicationId("TestApp"),
                event = Event("New sample"),
                timestamp = Clock.System.now(),
            ),
            errors = mutableListOf(
                HerodotusError(
                    code = "err",
                    group = "request",
                    field = "applicationId",
                    message = "unknown application for userId",
                )
            ),
            state = JobState.RUNNING,
        )

        val response = context.toTransportReport() as ReportCreateResponse

        assertEquals("TestApp", response.report?.applicationId)
        assertEquals("New sample", response.report?.event)
        assertEquals(1, response.errors?.size)
        assertEquals("err", response.errors?.firstOrNull()?.code)
        assertEquals("request", response.errors?.firstOrNull()?.group)
        assertEquals("applicationId", response.errors?.firstOrNull()?.fieldName)
        assertEquals("unknown application for userId", response.errors?.firstOrNull()?.message)
    }
}
