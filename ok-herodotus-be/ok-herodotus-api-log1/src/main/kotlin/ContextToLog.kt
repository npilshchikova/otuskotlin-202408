package ru.otus.otuskotlin.herodotus.api.log1.mapper

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.herodotus.api.log1.models.*
import ru.otus.otuskotlin.herodotus.common.NONE
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.*

fun ReportContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-marketplace",
    report = toReportLog(),
    errors = errors.map { it.toLog() },
)

private fun ReportContext.toReportLog(): ReportLogModel? {
    val emptyReport = Report()
    return ReportLogModel(
        requestId = requestId.takeIf { it != RequestId.NONE }?.asString(),
        requestReport = reportRequest.takeIf { it != emptyReport }?.toLog(),
        requestSearch = reportFilterRequest.takeIf { it != ReportSearchFilter.NONE }?.toLog(),
        requestResume = reportResumeRequest.takeIf { it != ReportResumeFilter.NONE }?.toLog(),
        responseReport = reportResponse.takeIf { it != emptyReport }?.toLog(),
        responseReports = reportsResponse.takeIf { it.isNotEmpty() }?.filter { it != emptyReport }?.map { it.toLog() },
        responseSummary = resumeResponse.takeIf { it != ReportSummary.NONE }?.toLog(),
    ).takeIf { it != ReportLogModel() }
}

private fun ReportSearchFilter.toLog() = ReportSearchLog(
    applicationId = applicationId.takeIf { it != ApplicationId.NONE }?.asString(),
    events = events.map { it.takeIf { it != Event.NONE }?.asString() }.joinToString(","),
    searchFields = searchFields.joinToString("\t") { it.toString() },
)

private fun ReportResumeFilter.toLog() = ReportResumeLog(
    resumeFieldName = fieldName.takeIf { it.isNotEmpty() }
)

private fun ReportSummary.toLog() = ReportSummaryLog(
    itemsNumber = itemsNumber.toString(),
    summary = summary.map { value ->
        ReportSummaryLogItem(
            fieldValue = value.fieldValue,
            event = value.event.takeIf { it != Event.NONE }?.asString(),
            timestamp = value.timestamp.takeIf { it != Instant.NONE }?.toString()
        )
    }
)

private fun HerodotusError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun Report.toLog() = ReportLog(
    applicationId = applicationId.takeIf { it != ApplicationId.NONE }?.asString(),
    event = event.takeIf { it != Event.NONE }?.asString(),
    timestamp = timestamp.takeIf { it != Instant.NONE }?.toString(),
    content = content.toString(),
)
