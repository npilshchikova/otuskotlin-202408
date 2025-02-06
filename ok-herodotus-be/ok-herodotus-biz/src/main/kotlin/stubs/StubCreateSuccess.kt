package ru.otus.otuskotlin.herodotus.biz.stubs

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.models.ReportId
import ru.otus.otuskotlin.herodotus.common.stubs.ReportStubs
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker
import ru.otus.otuskotlin.herodotus.logging.common.LogLevel
import ru.otus.otuskotlin.herodotus.stubs.ReportStub

fun ICorChainDsl<ReportContext>.stubCreateSuccess(title: String, corSettings: ReportCorSettings) = worker {
    this.title = title
    this.description = """
        Report creation success
    """.trimIndent()

    on { stubCase == ReportStubs.SUCCESS && state == JobState.RUNNING }

    val logger = corSettings.loggerProvider.logger("stubCreateSuccess")

    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = JobState.FINISHING
            reportResponse = ReportStub.prepareReport(
                reportId = ReportId(
                    reportRequest.applicationId,
                    reportRequest.event,
                    (0..1e9.toInt()).shuffled().first()  // random Int
                ),
                applicationId = reportRequest.applicationId,
                event = reportRequest.event,
                timestamp = reportRequest.timestamp,
                content = reportRequest.content,
            )
        }
    }
}
