package ru.otus.otuskotlin.herodotus.common.models

import kotlinx.datetime.Instant

data class ReportSummary(
    val itemsNumber: Int = 0,
    val summary: List<SummaryValue> = emptyList(),
) {
    data class SummaryValue(
        val fieldValue: String,
        val event: Event,
        val timestamp: Instant,
    )

    companion object {
        val NONE = ReportSummary()
    }
}
