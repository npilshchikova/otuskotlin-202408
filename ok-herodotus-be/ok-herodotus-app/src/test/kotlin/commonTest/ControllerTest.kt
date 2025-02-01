package ru.otus.otuskotlin.herodotus.app.commonTest

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.herodotus.api.v1.mappers.fromTransport
import ru.otus.otuskotlin.herodotus.api.v1.mappers.toTransportReport
import ru.otus.otuskotlin.herodotus.api.v1.models.*
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettings
import ru.otus.otuskotlin.herodotus.app.common.controllerHelper
import ru.otus.otuskotlin.herodotus.biz.ReportProcessor
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerTest {

    private val request = ReportCreateRequest(
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
        debug = Debug(mode = DebugMode.STUB, stub = DebugStubs.SUCCESS)
    )

    private val appSettings: HerodotusAppSettings = object : HerodotusAppSettings {
        override val corSettings: ReportCorSettings = ReportCorSettings()
        override val processor: ReportProcessor = ReportProcessor(corSettings)
    }

    class TestApplicationCall(private val request: IRequest) {
        var response: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(response: IResponse) {
            this.response = response
        }
    }

    private suspend fun TestApplicationCall.createReport(appSettings: HerodotusAppSettings) {
        val response = appSettings.controllerHelper(
            { fromTransport(receive<ReportCreateRequest>()) },
            { toTransportReport() },
            ControllerTest::class,
            "controller-v2-test"
        )
        respond(response)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createReport(appSettings) }
        val response = testApp.response as ReportCreateResponse
        assertEquals(ResponseResult.SUCCESS, response.result)
    }
}
