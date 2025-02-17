package ru.otus.otuskotlin.herodotus.app.repo

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.herodotus.api.v1.mappers.toTransportCreate
import ru.otus.otuskotlin.herodotus.api.v1.mappers.toTransportDelete
import ru.otus.otuskotlin.herodotus.api.v1.mappers.toTransportRead
import ru.otus.otuskotlin.herodotus.api.v1.models.*
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettings
import ru.otus.otuskotlin.herodotus.app.module
import ru.otus.otuskotlin.herodotus.common.models.ReportId
import ru.otus.otuskotlin.herodotus.stubs.ReportStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class V1ReportRepoBaseTest {
    abstract val workMode: DebugMode
    abstract val appSettingsCreate: HerodotusAppSettings
    abstract val appSettingsRead:   HerodotusAppSettings
    abstract val appSettingsDelete: HerodotusAppSettings
    abstract val appSettingsSearch: HerodotusAppSettings
    abstract val appSettingsResume: HerodotusAppSettings

    protected val uuidOld = "10000000-0000-0000-0000-000000000001"
    protected val uuidNew = "10000000-0000-0000-0000-000000000002"
    protected val initReport = ReportStub.prepareReport(
        reportId = ReportId(uuidOld)
    )

    @Test
    fun create() {
        val report = initReport.toTransportCreate()
        v1TestApplication(
            settings = appSettingsCreate,
            endpoint = "create",
            request = ReportCreateRequest(
                report = report,
                debug = Debug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<ReportCreateResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidNew, responseObj.report?.reportId)
            assertEquals(report.applicationId, responseObj.report?.applicationId)
            assertEquals(report.event, responseObj.report?.event)
            assertEquals(report.timestamp, responseObj.report?.timestamp)
        }
    }

    @Test
    fun read() {
        val report = initReport.toTransportRead()
        v1TestApplication(
            settings = appSettingsRead,
            endpoint = "read",
            request = ReportReadRequest(
                report = report,
                debug = Debug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<IResponse>() as ReportReadResponse
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.report?.reportId)
        }
    }

    @Test
    fun delete() {
        val report = initReport.toTransportDelete()
        v1TestApplication(
            settings = appSettingsDelete,
            endpoint = "delete",
            request = ReportDeleteRequest(
                report = report,
                debug = Debug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<ReportDeleteResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.report?.reportId)
        }
    }

    @Test
    fun search() = v1TestApplication(
        settings = appSettingsSearch,
        endpoint = "search",
        request = ReportSearchRequest(
            params = ReportSearchParams(),
            debug = Debug(mode = workMode),
        ),
    ) { response ->
        val responseObj = response.body<ReportSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.reports?.size)
        assertEquals(uuidOld, responseObj.reports?.first()?.reportId)
    }

    @Test
    fun resume() = v1TestApplication(
        settings = appSettingsResume,
        endpoint = "offers",
        request = ReportResumeRequest(
            searchParams = ReportSearchParams(),
            resumeParams = ReportResumeParams("sampleId"),
            debug = Debug(mode = workMode),
        ),
    ) { response ->
        val responseObj = response.body<ReportResumeResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.itemsNumber?.toInt())
        assertNotEquals(0, responseObj.summary?.size)
    }

    private inline fun <reified T: IRequest> v1TestApplication(
        settings: HerodotusAppSettings,
        endpoint: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(appSettings = settings) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson()
            }
        }
        val response = client.post("/v1/report/$endpoint") {
            contentType(ContentType.Application.Json)
            header("X-Trace-Id", "12345")
            setBody(request)
        }
        function(response)
    }
}
