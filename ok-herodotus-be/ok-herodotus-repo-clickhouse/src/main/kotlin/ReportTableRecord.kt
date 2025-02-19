package ru.otus.otuskotlin.herodotus.repo.clickhouse

import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import ru.otus.otuskotlin.herodotus.common.NONE
import ru.otus.otuskotlin.herodotus.common.models.ApplicationId
import ru.otus.otuskotlin.herodotus.common.models.Event
import ru.otus.otuskotlin.herodotus.common.models.Report
import ru.otus.otuskotlin.herodotus.common.models.ReportId

data class ReportTableRecord(
    val reportId: String?,
    val applicationId: String?,
    val event: String?,
    val timestamp: java.time.Instant?,
    val content: String?,  // to use JSONAsString format
) {
    constructor(model: Report): this(
        reportId = model.reportId.asString().takeIf { it.isNotBlank() },
        applicationId = model.applicationId.asString().takeIf { it.isNotBlank() },
        event = model.event.asString().takeIf { it.isNotBlank() },
        timestamp = if (model.timestamp != Instant.NONE) model.timestamp.toJavaInstant() else null,
        content = model.content.toString(),
    )

    fun toInternal() = Report(
        reportId = reportId?.let { ReportId(it) } ?: ReportId.NONE,
        applicationId = applicationId?.let { ApplicationId(it) } ?: ApplicationId.NONE,
        event = event?.let { Event(it) } ?: Event.NONE,
        timestamp = timestamp?.let { Instant.fromEpochMilliseconds(timestamp.toEpochMilli()) } ?: Instant.NONE,
        content = content?.let { Json.parseToJsonElement(content).jsonObject } ?: buildJsonObject {},
    )
}
