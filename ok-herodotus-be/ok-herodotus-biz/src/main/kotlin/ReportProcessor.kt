package ru.otus.otuskotlin.herodotus.biz

import ru.otus.otuskotlin.herodotus.biz.general.initStatus
import ru.otus.otuskotlin.herodotus.biz.general.operation
import ru.otus.otuskotlin.herodotus.biz.general.stubs
import ru.otus.otuskotlin.herodotus.biz.stubs.*
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import ru.otus.otuskotlin.herodotus.common.models.ReportCommand
import ru.otus.otuskotlin.herodotus.cor.rootChain

class ReportProcessor(
    private val corSettings: ReportCorSettings = ReportCorSettings.NONE
) {
    suspend fun exec(context: ReportContext) = businessChain.exec(
        context.also { it.corSettings = corSettings }
    )

    private val businessChain = rootChain<ReportContext> {
        initStatus("Initialize status")

        operation("Create report", ReportCommand.CREATE) {
            stubs {
                stubCreateSuccess("Successful report creation", corSettings)
                stubValidationUnknownApplication("Unknown application error simulation")
                stubValidationUnknownEvent("Unknown event error simulation")
                stubDbError("DB error simulation")
                stubNoCase()
            }
        }

        operation("Read report", ReportCommand.READ) {
            stubs {
                stubReadSuccess("Successful report reading", corSettings)
                stubNotFound("Report not found simulation")
                stubDbError("DB error simulation")
                stubNoCase()
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
        }
        operation("Search for reports", ReportCommand.SEARCH) {
            stubs {
                stubSearchSuccess("Successful search", corSettings)
                stubValidationUnknownApplication("Unknown application error simulation")
                stubValidationUnknownEvent("Unknown event error simulation")
                stubDbError("DB error simulation")
                stubNoCase()
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
        }
    }
}
