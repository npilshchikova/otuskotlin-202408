package ru.otus.otuskotlin.herodotus.biz.general

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.models.WorkMode
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.prepareResponse() = worker {
    this.title = "Prepare response"
    description = "Prepare data to send response to client"
    on { workMode != WorkMode.STUB }
    handle {
        reportResponse = reportRepoDone
        reportsResponse = reportsRepoDone
        resumeResponse = resumeRepoDone
        state = when (val st = state) {
            JobState.RUNNING -> JobState.FINISHING
            else -> st
        }
    }
}
