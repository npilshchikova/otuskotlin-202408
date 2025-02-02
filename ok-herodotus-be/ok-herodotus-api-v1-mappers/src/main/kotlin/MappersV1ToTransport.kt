package ru.otus.otuskotlin.herodotus.api.v1.mappers

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.herodotus.api.v1.models.*
import ru.otus.otuskotlin.herodotus.common.NONE
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.exceptions.UnknownReportCommand
import ru.otus.otuskotlin.herodotus.common.models.*

fun ReportContext.toTransportReport(): IResponse = when (val cmd = command) {
    ReportCommand.CREATE -> toTransportCreate()
    ReportCommand.READ -> toTransportRead()
    ReportCommand.DELETE -> toTransportDelete()
    ReportCommand.SEARCH -> toTransportSearch()
    ReportCommand.RESUME -> toTransportResume()
    ReportCommand.INIT -> toTransportInit()
    ReportCommand.FINISH -> object: IResponse {
        override val responseType: String? = null
        override val result: ResponseResult? = null
        override val errors: List<Error>? = null
    }
    ReportCommand.NONE -> throw UnknownReportCommand(cmd)
}

fun ReportContext.toTransportCreate() = ReportCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    report = reportResponse.toTransportReport()
)

fun ReportContext.toTransportRead() = ReportReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    report = reportResponse.toTransportReport(),
    content = reportResponse.content.toString(),
)

fun ReportContext.toTransportDelete() = ReportDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    report = reportResponse.toTransportReport()
)

fun ReportContext.toTransportSearch() = ReportSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    reports = reportsResponse.toTransportReport()
)

fun ReportContext.toTransportResume() = ReportResumeResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    itemsNumber = resumeResponse.itemsNumber.toBigDecimal(),
    summary = resumeResponse.summary.map { it.toTransportSummary() },
)

fun ReportContext.toTransportInit() = ReportInitResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

private fun ReportSummary.SummaryValue.toTransportSummary(): ReportResumeSummaryValue = ReportResumeSummaryValue(
    fieldValue = this.fieldValue,
    event = this.event.takeIf { it != Event.NONE }?.asString(),
    timestamp = this.timestamp.takeIf { it != Instant.NONE }?.toString(),
)

fun List<Report>.toTransportReport(): List<ReportResponseObject>? = this
    .map { it.toTransportReport() }
    .takeIf { it.isNotEmpty() }

private fun Report.toTransportReport(): ReportResponseObject = ReportResponseObject(
    reportId = reportId.takeIf { it != ReportId.NONE }?.asString(),
    applicationId = applicationId.takeIf { it != ApplicationId.NONE }?.asString(),
    event = event.takeIf { it != Event.NONE }?.asString(),
    timestamp = timestamp.takeIf { it != Instant.NONE }?.toString(),
)

private fun List<HerodotusError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportReport() }
    .takeIf { it.isNotEmpty() }

private fun HerodotusError.toTransportReport() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    fieldName = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun JobState.toResult(): ResponseResult? = when (this) {
    JobState.RUNNING -> ResponseResult.SUCCESS
    JobState.FAILING -> ResponseResult.ERROR
    JobState.FINISHING -> ResponseResult.SUCCESS
    JobState.NONE -> null
}
