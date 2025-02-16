package ru.otus.otuskotlin.herodotus.biz.stubs

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.helpers.fail
import ru.otus.otuskotlin.herodotus.common.models.HerodotusError
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.stubs.ReportStubs
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.stubNotFound(title: String) = worker {
    this.title = title
    this.description = """
        Entity of interest were not found
    """.trimIndent()

    on { stubCase == ReportStubs.NOT_FOUND && state == JobState.RUNNING }

    handle {
        fail(
            HerodotusError(
                group = "internal",
                code = "internal-db",
                message = "Entity not found error"
            )
        )
    }
}
