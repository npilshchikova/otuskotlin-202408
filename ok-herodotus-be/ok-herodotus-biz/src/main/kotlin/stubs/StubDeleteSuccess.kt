package ru.otus.otuskotlin.herodotus.biz.stubs

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.stubs.ReportStubs
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker
import ru.otus.otuskotlin.herodotus.logging.common.LogLevel
import ru.otus.otuskotlin.herodotus.stubs.ReportStub


fun ICorChainDsl<ReportContext>.stubDeleteSuccess(title: String, corSettings: ReportCorSettings) = worker {
    this.title = title
    this.description = """
        Successful report delete operation case
    """.trimIndent()

    on { stubCase == ReportStubs.SUCCESS && state == JobState.RUNNING }

    val logger = corSettings.loggerProvider.logger("stubDeleteSuccess")

    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = JobState.FINISHING
            reportResponse = ReportStub.prepareReport (
                reportId = reportRequest.reportId,
            )
        }
    }
}
