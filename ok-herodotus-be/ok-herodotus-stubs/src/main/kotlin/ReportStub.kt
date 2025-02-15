package ru.otus.otuskotlin.herodotus.stubs

import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonObject
import ru.otus.otuskotlin.herodotus.common.models.*
import ru.otus.otuskotlin.herodotus.stubs.ReportStubSample.REPORT_SAMPLE_1
import ru.otus.otuskotlin.herodotus.stubs.ReportStubSample.REPORT_SAMPLE_2

object ReportStub {
    private val samples = listOf(REPORT_SAMPLE_1, REPORT_SAMPLE_2)

    fun get(num: Int = 0): Report = samples[num.coerceAtMost(samples.size - 1)]

    fun prepareReport(
        reportId: ReportId? = null,
        applicationId: ApplicationId? = null,
        event: Event? = null,
        timestamp: Instant? = null,
        content: JsonObject? = null,
        num: Int = 0,
    ) = Report(
        reportId = reportId ?: this.get(num).reportId,
        applicationId = applicationId ?: this.get(num).applicationId,
        event = event ?: this.get(num).event,
        timestamp = timestamp ?: this.get(num).timestamp,
        content = content ?: this.get(num).content,
    )

    fun prepareSearchList(
        applicationId: ApplicationId,
        events: List<Event>,
    ) = (0..< this.samples.size).map {
        this.prepareReport(
            applicationId = applicationId,
            event = events.firstOrNull(),
            num = it
        )
    }

    fun prepareSummary(
        applicationId: ApplicationId,
        events: List<Event>,
    ) = ReportSummary(
        itemsNumber = this.samples.size,
        summary = this.prepareSearchList(applicationId, events).map {
            ReportSummary.SummaryValue(
                fieldValue = "unknown",
                event = it.event,
                timestamp = it.timestamp,
            )
        }
    )
}