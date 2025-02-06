package ru.otus.otuskotlin.herodotus.biz.stubs

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.helpers.fail
import ru.otus.otuskotlin.herodotus.common.models.HerodotusError
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.stubs.ReportStubs
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.stubCannotDelete(title: String) = worker {
    this.title = title
    this.description = """
        Impossible to delete report error
    """.trimIndent()

    on { stubCase == ReportStubs.CANNOT_DELETE && state == JobState.RUNNING }

    handle {
        fail(
            HerodotusError(
                group = "internal",
                code = "internal-db",
                message = "Cannot delete error"
            )
        )
    }
}
