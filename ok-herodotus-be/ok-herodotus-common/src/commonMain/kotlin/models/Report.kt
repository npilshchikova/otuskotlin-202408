package ru.otus.otuskotlin.herodotus.common.models

import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import ru.otus.otuskotlin.herodotus.common.NONE

data class Report(
    val reportId: ReportId = ReportId.NONE,
    val applicationId: ApplicationId = ApplicationId.NONE,
    val event: Event = Event.NONE,
    val timestamp: Instant = Instant.NONE,
    val content: JsonObject = buildJsonObject {}
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = Report()
    }
}
