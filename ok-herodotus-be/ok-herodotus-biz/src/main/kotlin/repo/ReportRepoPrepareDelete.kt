package ru.otus.otuskotlin.herodotus.biz.repo

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = "Prepare data to delete from DB".trimIndent()
    on { state == JobState.RUNNING }
    handle {
        reportRepoPrepare = reportValidated.deepCopy()
    }
}
