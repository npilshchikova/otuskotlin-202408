package ru.otus.otuskotlin.herodotus.biz.repo

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Prepare object to save to DB"
    on { state == JobState.RUNNING }
    handle {
        reportRepoPrepare = reportValidated.deepCopy()
    }
}
