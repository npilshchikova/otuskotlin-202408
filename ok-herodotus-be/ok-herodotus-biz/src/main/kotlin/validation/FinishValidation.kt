package ru.otus.otuskotlin.herodotus.biz.validation

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.finishReportValidation() = worker {
    this.title = "Finish report validation"
    on { state == JobState.RUNNING }
    handle {
        reportValidated = reportValidating.deepCopy()
    }
}

fun ICorChainDsl<ReportContext>.finishReportFilterValidation() = worker {
    this.title = "Finish search filter validation"
    on { state == JobState.RUNNING }
    handle {
        reportFilterValidated = reportFilterValidating.deepCopy()
    }
}

fun ICorChainDsl<ReportContext>.finishReportResumeValidation() = worker {
    this.title = "Finish resume parameters validation"
    on { state == JobState.RUNNING }
    handle {
        reportFilterValidated = reportFilterValidating.deepCopy()
        reportResumeValidated = reportResumeValidating.deepCopy()
    }
}
