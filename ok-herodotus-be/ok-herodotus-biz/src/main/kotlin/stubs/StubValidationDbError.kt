package ru.otus.otuskotlin.herodotus.biz.stubs

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.helpers.fail
import ru.otus.otuskotlin.herodotus.common.models.HerodotusError
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.stubs.ReportStubs
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.stubDbError(title: String) = worker {
    this.title = title
    this.description = """
        Database error case
    """.trimIndent()

    on { stubCase == ReportStubs.DB_ERROR && state == JobState.RUNNING }

    handle {
        fail(
            HerodotusError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
