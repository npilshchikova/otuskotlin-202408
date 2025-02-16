package ru.otus.otuskotlin.herodotus.repo.inmemory

import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import ru.otus.otuskotlin.herodotus.common.NONE
import ru.otus.otuskotlin.herodotus.common.models.ApplicationId
import ru.otus.otuskotlin.herodotus.common.models.Event
import ru.otus.otuskotlin.herodotus.common.models.Report
import ru.otus.otuskotlin.herodotus.common.models.ReportId

data class ReportEntity(
    val reportId: String? = null,
    val applicationId: String? = null,
    val event: String? = null,
    val timestamp: Long? = null,
    val content: JsonObject? = null,
) {
    constructor(model: Report): this(
        reportId = model.reportId.asString().takeIf { it.isNotBlank() },
        applicationId = model.applicationId.asString().takeIf { it.isNotBlank() },
        event = model.event.asString().takeIf { it.isNotBlank() },
        timestamp = if (model.timestamp != Instant.NONE) model.timestamp.toEpochMilliseconds() else null,
        content = model.content,
    )

    fun toInternal() = Report(
        reportId = reportId?.let { ReportId(it) } ?: ReportId.NONE,
        applicationId = applicationId?.let { ApplicationId(it) } ?: ApplicationId.NONE,
        event = event?.let { Event(it) } ?: Event.NONE,
        timestamp = timestamp?.let { Instant.fromEpochMilliseconds(it) } ?: Instant.NONE,
        content = content?.let { content } ?: buildJsonObject {},
    )
}
