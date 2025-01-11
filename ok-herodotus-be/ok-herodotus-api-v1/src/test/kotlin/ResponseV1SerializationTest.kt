import kotlinx.datetime.Clock
import ru.otus.otuskotlin.herodotus.api.v1.apiV1ResponseDeserialize
import ru.otus.otuskotlin.herodotus.api.v1.apiV1ResponseSerialize
import ru.otus.otuskotlin.herodotus.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = ReportCreateResponse(
        responseType = "create",
        result = ResponseResult.SUCCESS,
        report = ReportResponseObject(
            applicationId = "TestApp",
            event = "New sample",
            timestamp = Clock.System.now(),
            reportId = "0001",
        )
    )

    @Test
    fun serialize() {
        val json = apiV1ResponseSerialize(response)

        assertContains(json, Regex("\"applicationId\":\\s*\"${response.report?.applicationId}\""))
        assertContains(json, Regex("\"reportId\":\\s*\"${response.report?.reportId}\""))
        assertContains(json, Regex("\"responseType\":\\s*\"${response.responseType}\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1ResponseSerialize(response)
        val obj = apiV1ResponseDeserialize<ReportCreateResponse>(json)

        assertEquals(response, obj)
    }
}
