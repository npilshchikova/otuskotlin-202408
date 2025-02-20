package ru.otus.otuskotlin.herodotus.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.herodotus.common.models.*
import ru.otus.otuskotlin.herodotus.common.repo.IRepoReport
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

    // objects from request
    var reportRequest: Report = Report(),
    var reportFilterRequest: ReportSearchFilter = ReportSearchFilter.NONE,
    var reportResumeRequest: ReportResumeFilter = ReportResumeFilter.NONE,

    // objects during validation process
    var reportValidating: Report = Report(),
    var reportFilterValidating: ReportSearchFilter = ReportSearchFilter.NONE,
    var reportResumeValidating: ReportResumeFilter = ReportResumeFilter.NONE,

    // objects after validation
    var reportValidated: Report = Report(),
    var reportFilterValidated: ReportSearchFilter = ReportSearchFilter.NONE,
    var reportResumeValidated: ReportResumeFilter = ReportResumeFilter.NONE,

    // objects during requests to DB
    var reportRepo: IRepoReport = IRepoReport.NONE,
    var reportRepoRead: Report = Report(),  // object, read from repo
    var reportRepoPrepare: Report = Report(), // prepare to save to DB
    var reportRepoDone: Report = Report(),  // result from DB
    var reportsRepoDone: MutableList<Report> = mutableListOf(),
    var resumeRepoDone: ReportSummary = ReportSummary(),

    // objects to send to client
    var reportResponse: Report = Report(),
    var reportsResponse: MutableList<Report> = mutableListOf(),
    var resumeResponse: ReportSummary = ReportSummary(),
)
