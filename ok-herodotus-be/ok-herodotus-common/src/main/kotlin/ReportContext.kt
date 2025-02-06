package ru.otus.otuskotlin.herodotus.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.herodotus.common.models.*
import ru.otus.otuskotlin.herodotus.common.stubs.ReportStubs
import ru.otus.otuskotlin.herodotus.common.ws.WsSession

data class ReportContext(
    var command: ReportCommand = ReportCommand.NONE,
    var state: JobState = JobState.NONE,
    val errors: MutableList<HerodotusError> = mutableListOf(),

    var corSettings: ReportCorSettings = ReportCorSettings(),
    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: ReportStubs = ReportStubs.NONE,
    var wsSession: WsSession = WsSession.NONE,

    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var reportRequest: Report = Report(),
    var reportFilterRequest: ReportSearchFilter = ReportSearchFilter.NONE,
    var reportResumeRequest: ReportResumeFilter = ReportResumeFilter.NONE,

    var reportResponse: Report = Report(),
    var reportsResponse: MutableList<Report> = mutableListOf(),
    var resumeResponse: ReportSummary = ReportSummary(),
)
