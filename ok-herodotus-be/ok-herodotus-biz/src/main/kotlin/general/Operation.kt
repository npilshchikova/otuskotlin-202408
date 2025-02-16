package ru.otus.otuskotlin.herodotus.biz.general

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.models.ReportCommand
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.chain

fun ICorChainDsl<ReportContext>.operation(
    title: String,
    command: ReportCommand,
    block: ICorChainDsl<ReportContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == JobState.RUNNING }
}
