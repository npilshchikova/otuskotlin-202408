package ru.otus.otuskotlin.herodotus.api.v1.mappers

import kotlinx.datetime.Instant
import org.junit.Test
import ru.otus.otuskotlin.herodotus.api.v1.models.*
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.*
import ru.otus.otuskotlin.herodotus.common.stubs.Stubs
import java.math.BigDecimal
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MapperResumeTest {
    @Test
    fun fromTransport() {
        val request = ReportResumeRequest(
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS,
            ),
            searchParams = ReportSearchParams(
                applicationId = "TestApp",
                events = listOf("New sample", "New run"),
                searchFields = listOf(
                    ReportSearchField(
                        fieldName = "Analyte",
                        searchString = "DNA",
                        action = SearchAction.EQUALS
                    )
                )
            ),
            resumeParams = ReportResumeParams(
                fieldName = "Organization"
            )
        )

        val context = ReportContext()
        context.fromTransport(request)

        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(WorkMode.STUB, context.workMode)
        assertEquals(ApplicationId("TestApp"), context.reportFilterRequest.applicationId)
        assertEquals(2, context.reportFilterRequest.events.size)
        assertContains(context.reportFilterRequest.events, Event("New sample"))
        assertEquals(1, context.reportFilterRequest.searchFields.size)
        assertEquals(ReportSearchFilter.SearchAction.EQUALS, context.reportFilterRequest.searchFields.first().action)
        assertEquals("Organization", context.reportResumeRequest.fieldName)
    }

    @Test
    fun toTransport() {
        val context = ReportContext(
            requestId = RequestId("1234"),
            command = ReportCommand.RESUME,
            resumeResponse = ReportSummary(
                itemsNumber = 2,
                summary = listOf(
                    ReportSummary.SummaryValue(
                        fieldValue = "ParseqLab",
                        event = Event("New sample"),
                        timestamp = Instant.parse("2018-03-20T09:12:28Z")
                    ),
                    ReportSummary.SummaryValue(
                        fieldValue = "ParseqLab",
                        event = Event("New sample"),
                        timestamp = Instant.parse("2022-03-20T09:12:28Z")
                    )
                )
            ),
            errors = mutableListOf(),
            state = JobState.FINISHING,
        )

        val response = context.toTransportReport() as ReportResumeResponse

        assertEquals(BigDecimal(2), response.itemsNumber)
        assertEquals(2, response.summary?.size)
        assertContains(response.summary?.map { it.event } ?: emptyList(), "New sample")
        assertNull(response.errors)
        assertEquals(ResponseResult.SUCCESS, response.result)
    }
}