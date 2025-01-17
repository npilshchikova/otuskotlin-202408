package ru.otus.otuskotlin.herodotus.api.v1.mappers

import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import ru.otus.otuskotlin.herodotus.api.v1.models.*
import ru.otus.otuskotlin.herodotus.api.v1.mappers.exceptions.UnknownRequestClass
import ru.otus.otuskotlin.herodotus.common.NONE
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.*
import ru.otus.otuskotlin.herodotus.common.stubs.Stubs

fun ReportContext.fromTransport(request: IRequest) = when (request) {
    is ReportCreateRequest -> fromTransport(request)
    is ReportReadRequest -> fromTransport(request)
    is ReportDeleteRequest -> fromTransport(request)
    is ReportSearchRequest -> fromTransport(request)
    is ReportResumeRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toReportId() = this?.let { ReportId(it) } ?: ReportId.NONE
private fun String?.toApplicationId() = this?.let { ApplicationId(it) } ?: ApplicationId.NONE

private fun Debug?.transportToWorkMode(): WorkMode = when (this?.mode) {
    DebugMode.PROD -> WorkMode.PROD
    DebugMode.TEST -> WorkMode.TEST
    DebugMode.STUB -> WorkMode.STUB
    null -> WorkMode.PROD
}

private fun Debug?.transportToStubCase(): Stubs = when (this?.stub) {
    DebugStubs.SUCCESS -> Stubs.SUCCESS
    DebugStubs.NOT_FOUND -> Stubs.NOT_FOUND
    DebugStubs.VALUE_ERROR -> Stubs.VALUE_ERROR
    DebugStubs.MISSING_DATA -> Stubs.MISSING_DATA
    DebugStubs.CANNOT_DELETE -> Stubs.CANNOT_DELETE
    null -> Stubs.NONE
}

fun ReportContext.fromTransport(request: ReportCreateRequest) {
    command = ReportCommand.CREATE
    reportRequest = request.report?.toInternal() ?: Report()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun ReportCreateObject.toInternal(): Report = Report(
    reportId = ReportId.NONE,
    applicationId = this.applicationId.toApplicationId(),
    event = this.event?.let { Event(it) } ?: Event.NONE,
    timestamp = this.timestamp?.let { Instant.parse(it) } ?: Instant.NONE,
    content = this.content?.let { Json.parseToJsonElement(it).jsonObject } ?: buildJsonObject {}
)

fun ReportContext.fromTransport(request: ReportReadRequest) {
    command = ReportCommand.READ
    reportRequest = request.report.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun ReportReadObject?.toInternal(): Report {
    this?.let {
        return Report(it.reportId.toReportId())
    }
    return Report()
}

fun ReportContext.fromTransport(request: ReportDeleteRequest) {
    command = ReportCommand.DELETE
    reportRequest = request.report.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun ReportDeleteObject?.toInternal(): Report {
    this?.let {
        return Report(it.reportId.toReportId())
    }
    return Report()
}

fun ReportContext.fromTransport(request: ReportSearchRequest) {
    command = ReportCommand.SEARCH
    reportFilterRequest = request.params.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun ReportContext.fromTransport(request: ReportResumeRequest) {
    command = ReportCommand.RESUME
    reportFilterRequest = request.searchParams.toInternal()
    reportResumeRequest = request.resumeParams.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun SearchAction?.transportToSearchFilter(): ReportSearchFilter.SearchAction = when(this) {
    SearchAction.EQUALS -> ReportSearchFilter.SearchAction.EQUALS
    SearchAction.CONTAINS -> ReportSearchFilter.SearchAction.CONTAINS
    SearchAction.LESS -> ReportSearchFilter.SearchAction.LESS
    SearchAction.MORE -> ReportSearchFilter.SearchAction.MORE
    null -> ReportSearchFilter.SearchAction.EQUALS
}

private fun ReportSearchParams?.toInternal(): ReportSearchFilter = ReportSearchFilter(
    applicationId = this?.applicationId.toApplicationId(),
    events = this?.events?.map { Event(it) } ?: emptyList(),
    searchFields = this?.searchFields?.mapNotNull { searchField ->
        if (searchField.searchString != null && searchField.value == null) {
            ReportSearchFilter.StringSearchField(
                fieldName = searchField.fieldName ?: "",
                action = searchField.action.transportToSearchFilter(),
                stringValue = searchField.searchString ?: error("should not be possible"),
            )
        } else if (searchField.searchString == null && searchField.value != null) {
            ReportSearchFilter.NumericSearchField(
                fieldName = searchField.fieldName ?: "",
                action = searchField.action.transportToSearchFilter(),
                numericValue = searchField.value?.toInt() ?: error("should not be possible"),
            )
        } else {
            null
        }
    } ?: emptyList()
)

private fun ReportResumeParams?.toInternal(): ReportResumeFilter = ReportResumeFilter(
    fieldName = this?.fieldName ?: ""
)
