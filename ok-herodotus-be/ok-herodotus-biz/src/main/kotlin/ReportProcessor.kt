package ru.otus.otuskotlin.herodotus.biz

import ru.otus.otuskotlin.herodotus.biz.general.initStatus
import ru.otus.otuskotlin.herodotus.biz.general.operation
import ru.otus.otuskotlin.herodotus.biz.general.stubs
import ru.otus.otuskotlin.herodotus.biz.general.validation
import ru.otus.otuskotlin.herodotus.biz.stubs.*
import ru.otus.otuskotlin.herodotus.biz.validation.*
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import ru.otus.otuskotlin.herodotus.common.models.ApplicationId
import ru.otus.otuskotlin.herodotus.common.models.Event
import ru.otus.otuskotlin.herodotus.common.models.ReportCommand
import ru.otus.otuskotlin.herodotus.common.models.ReportId
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
        }

    }
}
