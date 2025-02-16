package ru.otus.otuskotlin.herodotus.biz.general

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.chain


fun ICorChainDsl<ReportContext>.validation(block: ICorChainDsl<ReportContext>.() -> Unit) = chain {
    block()
    title = "Validation"
    on { state == JobState.RUNNING }
}
