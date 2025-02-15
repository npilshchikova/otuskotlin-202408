package ru.otus.otuskotlin.herodotus.biz.general

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.WorkMode
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.chain

fun ICorChainDsl<ReportContext>.stubs(block: ICorChainDsl<ReportContext>.() -> Unit) = chain {
    block()
    title = "Handle stubs"
    on { workMode == WorkMode.STUB }
}
