package ru.otus.otuskotlin.herodotus.app.websocket

import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import kotlinx.coroutines.withTimeout
import ru.otus.otuskotlin.herodotus.api.v1.models.*
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettingsData
import ru.otus.otuskotlin.herodotus.app.module
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class V1WebsocketStubTest {

    @Test
    fun createStub() {
        val request = ReportCreateRequest(
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
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun readStub() {
        val request = ReportReadRequest(
            report = ReportReadObject(
                reportId = "BamAnalyzer_NewSample_1"
            ),
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun deleteStub() {
        val request = ReportDeleteRequest(
            report = ReportDeleteObject(
                reportId = "BamAnalyzer_NewSample_1",
            ),
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun searchStub() {
        val request = ReportSearchRequest(
            params = ReportSearchParams(
                applicationId = "BamAnalyzer"
            ),
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun resumeStub() {
        val request = ReportResumeRequest(
            searchParams = ReportSearchParams(),
            resumeParams = ReportResumeParams(),
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    private inline fun <reified T> testMethod(
        request: IRequest,
        crossinline assertBlock: (T) -> Unit
    ) = testApplication {
        application { module(HerodotusAppSettingsData(corSettings = ReportCorSettings())) }
        val client = createClient {
            install(WebSockets) {
                contentConverter = JacksonWebsocketContentConverter()
            }
        }

        client.webSocket("/v1/ws") {
            withTimeout(3000) {
                val response = receiveDeserialized<IResponse>() as T
                assertIs<ReportInitResponse>(response)
            }
            sendSerialized(request)
            withTimeout(3000) {
                val response = receiveDeserialized<IResponse>() as T
                assertBlock(response)
            }
        }
    }
}
