import ru.otus.otuskotlin.herodotus.api.v1.apiV1RequestDeserialize
import ru.otus.otuskotlin.herodotus.api.v1.apiV1RequestSerialize
import ru.otus.otuskotlin.herodotus.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = ReportCreateRequest(
        debug = Debug(
            mode = DebugMode.STUB,
            stub = DebugStubs.UNKNOWN_EVENT
        ),
        requestType = "create",
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
        )
    )

    @Test
    fun serialize() {
        val json = apiV1RequestSerialize(request)

        assertContains(json, Regex("\"applicationId\":\\s*\"${request.report?.applicationId}\""))
        assertContains(json, Regex("\"mode\":\\s*\"${request.debug?.mode}\""))
        assertContains(json, Regex("\"stub\":\\s*\"${request.debug?.stub}\""))
        assertContains(json, Regex("\"requestType\":\\s*\"${request.requestType}\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1RequestSerialize(request)
        val obj = apiV1RequestDeserialize<ReportCreateRequest>(json)

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            { "requestType": "create" }
        """.trimIndent()
        val obj = apiV1RequestDeserialize<ReportCreateRequest>(jsonString)

        assertEquals(null, obj.report?.event)
    }
}
