package ru.otus.otuskotlin.herodotus.api.v1.mappers

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.herodotus.api.v1.models.ReportCreateObject
import ru.otus.otuskotlin.herodotus.api.v1.models.ReportDeleteObject
import ru.otus.otuskotlin.herodotus.api.v1.models.ReportReadObject
import ru.otus.otuskotlin.herodotus.common.NONE
import ru.otus.otuskotlin.herodotus.common.models.ApplicationId
import ru.otus.otuskotlin.herodotus.common.models.Event
import ru.otus.otuskotlin.herodotus.common.models.Report
import ru.otus.otuskotlin.herodotus.common.models.ReportId

fun Report.toTransportCreate() = ReportCreateObject(
    applicationId = applicationId.takeIf { it != ApplicationId.NONE }?.asString(),
    event = event.takeIf { it != Event.NONE }?.asString(),
    timestamp = timestamp.takeIf { it != Instant.NONE }?.toString(),
    content = content.toString()
)

fun Report.toTransportRead() = ReportReadObject(
    reportId = reportId.takeIf { it != ReportId.NONE }?.asString(),
)

fun Report.toTransportDelete() = ReportDeleteObject(
    reportId = reportId.takeIf { it != ReportId.NONE }?.asString(),
)
