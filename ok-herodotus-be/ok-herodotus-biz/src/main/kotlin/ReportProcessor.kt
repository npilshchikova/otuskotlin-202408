package ru.otus.otuskotlin.herodotus.biz

import ru.otus.otuskotlin.herodotus.biz.general.*
import ru.otus.otuskotlin.herodotus.biz.repo.*
import ru.otus.otuskotlin.herodotus.biz.stubs.*
import ru.otus.otuskotlin.herodotus.biz.validation.*
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import ru.otus.otuskotlin.herodotus.common.models.*
import ru.otus.otuskotlin.herodotus.cor.chain
import ru.otus.otuskotlin.herodotus.cor.rootChain
import ru.otus.otuskotlin.herodotus.cor.worker

class ReportProcessor(
    private val corSettings: ReportCorSettings = ReportCorSettings.NONE
) {
    suspend fun exec(context: ReportContext) = businessChain.exec(
        context.also { it.corSettings = corSettings }
    )

    private val businessChain = rootChain {
        initStatus("Initialize status")
        initRepo("Initialize repo")

        operation("Create report", ReportCommand.CREATE) {
            stubs {
                stubCreateSuccess("Successful report creation", corSettings)
                stubValidationUnknownApplication("Unknown application error simulation")
                stubValidationUnknownEvent("Unknown event error simulation")
                stubDbError("DB error simulation")
                stubNoCase()
            }
            validation {
                worker("Copy input fields to reportValidating") {
                    reportValidating = reportRequest.deepCopy()
                }
                worker("Clear report Id and trim string parameters") {
                    reportValidating = reportValidating.copy(
                        reportId = ReportId.NONE,
                        applicationId = ApplicationId(reportValidating.applicationId.asString().trim()),
                        event = Event(reportValidating.event.asString().trim())
                    )
                }
                validateApplicationIdNotEmpty("Check, that applicationId is not empty")
                validateEventNotEmpty("Check, that event is not empty")
                validateTimestampNotEmpty("Check, that timestamp is not empty")
                finishReportValidation()
            }
            chain {
                title = "Save new report to DB"
                repoPrepareCreate("Prepare object to save")
                repoCreate("Create new report in DB")
            }
            prepareResponse()
        }

        operation("Read report", ReportCommand.READ) {
            stubs {
                stubReadSuccess("Successful report reading", corSettings)
                stubNotFound("Report not found simulation")
                stubDbError("DB error simulation")
                stubNoCase()
            }
            validation {
                worker("Copy input fields to reportValidating") {
                    reportValidating = reportRequest.deepCopy()
                }
                worker("Trim id") {
                    reportValidating = reportValidating.copy(
                        reportId = ReportId(reportValidating.reportId.asString().trim()),
                    )
                }
                validateReportIdNotEmpty("Check that reportId is not empty")
                finishReportValidation()
            }
            chain {
                title = "Read report from DB"
                repoRead("Report reading")
                worker {
                    title = "Prepare result"
                    on { state == JobState.RUNNING }
                    handle { reportRepoDone = reportRepoRead }
                }
            }
            prepareResponse()
        }

        operation("Delete report", ReportCommand.DELETE) {
            stubs {
                stubDeleteSuccess("Successful report delete", corSettings)
                stubNotFound("Report not found simulation")
                stubCannotDelete("Report delete operation error simulation")
                stubDbError("DB error simulation")
                stubNoCase()
            }
            validation {
                worker("Copy input fields to reportValidating") {
                    reportValidating = reportRequest.deepCopy()
                }
                worker("Trim report id") {
                    reportValidating = reportValidating.copy(
                        reportId = ReportId(reportValidating.reportId.asString().trim()),
                    )
                }
                validateReportIdNotEmpty("Check that reportId is not empty")
                finishReportValidation()
            }
            chain {
                title = "Delete report"
                repoRead("Read report from DB")
                repoPrepareDelete("Prepare object to delete")
                repoDelete("Delete report from DB")
            }
            prepareResponse()
        }

        operation("Search for reports", ReportCommand.SEARCH) {
            stubs {
                stubSearchSuccess("Successful search", corSettings)
                stubValidationUnknownApplication("Unknown application error simulation")
                stubValidationUnknownEvent("Unknown event error simulation")
                stubDbError("DB error simulation")
                stubNoCase()
            }
            validation {
                worker("Copy input fields to reportFilterValidating") {
                    reportFilterValidating = reportFilterRequest.deepCopy()
                }
                validateSearchFilters("Search filter parameters validation")
                finishReportFilterValidation()
            }
            repoSearch("Search for reports in DB")
            prepareResponse()
        }

        operation("Search and summary for reports", ReportCommand.RESUME) {
            stubs {
                stubResumeSuccess("Successful search and resume", corSettings)
                stubValidationUnknownApplication("Unknown application error simulation")
                stubValidationUnknownEvent("Unknown event error simulation")
                stubDbError("DB error simulation")
                stubNoCase()
            }
            validation {
                worker("Copy input fields to reportFilterValidating") {
                    reportFilterValidating = reportFilterRequest.deepCopy()
                }
                worker("Copy input fields to reportResumeValidating") {
                    reportResumeValidating = reportResumeRequest.deepCopy()
                }
                validateSearchFilters("Search filter parameters validation")
                validateResumeParameters("Resume parameters validation")
                finishReportResumeValidation()
            }
            chain {
                title = "Search for reports in DB and resume results"
                repoSearch("Search for reports in DB")
                reportsResume("Resume search results")
            }
            prepareResponse()
        }

    }
}
