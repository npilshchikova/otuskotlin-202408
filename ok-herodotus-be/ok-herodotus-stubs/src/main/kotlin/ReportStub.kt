package ru.otus.otuskotlin.herodotus.stubs

import ru.otus.otuskotlin.herodotus.common.models.*
import ru.otus.otuskotlin.herodotus.stubs.ReportStubSample.REPORT_SAMPLE_1
import ru.otus.otuskotlin.herodotus.stubs.ReportStubSample.REPORT_SAMPLE_2

object ReportStub {
    fun get(): Report = REPORT_SAMPLE_1.copy()

    fun prepareResult(block: Report.() -> Unit): Report = get().apply(block)

    fun prepareSearchList(
        applicationId: ApplicationId,
        event: Event,
        searchFields: List<ReportSearchFilter.SearchField>
    ) = listOf(
        REPORT_SAMPLE_1,
        REPORT_SAMPLE_2
    ).filter { it.applicationId == applicationId && it.event == event }

    fun prepareSummary(fieldName: String) = ReportSummary(
        itemsNumber = 2,
        summary = listOf(REPORT_SAMPLE_1, REPORT_SAMPLE_2).map {
            ReportSummary.SummaryValue(
                fieldValue = REPORT_SAMPLE_1.content[fieldName]?.toString() ?: "",
                event = REPORT_SAMPLE_1.event,
                timestamp = REPORT_SAMPLE_1.timestamp,
            )
        }.filter { it.fieldValue != "" }
    )
}