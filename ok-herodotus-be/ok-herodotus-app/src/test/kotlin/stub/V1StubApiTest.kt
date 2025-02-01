package ru.otus.otuskotlin.herodotus.app.stub

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.herodotus.api.v1.models.*
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettingsData
import ru.otus.otuskotlin.herodotus.app.module
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class V1StubApiTest {
    @Test
    fun create() = v1TestApplication(
        func = "create",
        request = ReportCreateRequest(
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
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<ReportCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("TestApp", responseObj.report?.applicationId)
    }

    @Test
    fun read() = v1TestApplication(
        func = "read",
        request = ReportReadRequest(
            report = ReportReadObject(
                reportId = "Hmmm"
            ),
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<ReportReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("Hmmm", responseObj.report?.reportId)
    }

    @Test
    fun delete() = v1TestApplication(
        func = "delete",
        request = ReportDeleteRequest(
            report = ReportDeleteObject(
                reportId = "Hmmm",
            ),
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<ReportDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("Hmmm", responseObj.report?.reportId)
    }

    @Test
    fun search() = v1TestApplication(
        func = "search",
        request = ReportSearchRequest(
            params = ReportSearchParams(),
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<ReportSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("Hmmm", responseObj.reports?.first()?.reportId)
    }

    @Test
    fun offers() = v1TestApplication(
        func = "resume",
        request = ReportResumeRequest(
            searchParams = ReportSearchParams(),
            resumeParams = ReportResumeParams(),
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<ReportResumeResponse>()
        assertEquals(200, response.status.value)
        assertEquals(2.0.toBigDecimal(), responseObj.itemsNumber)
        assertEquals(2, responseObj.summary?.size)
    }

    private fun v1TestApplication(
        func: String,
        request: IRequest,
        function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(HerodotusAppSettingsData(corSettings = ReportCorSettings())) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                    enable(SerializationFeature.INDENT_OUTPUT)
                    writerWithDefaultPrettyPrinter()
                }
            }
        }
        val response = client.post("/v1/report/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}
