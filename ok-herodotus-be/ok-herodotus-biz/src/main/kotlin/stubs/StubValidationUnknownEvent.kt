package ru.otus.otuskotlin.herodotus.biz.stubs

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.helpers.fail
import ru.otus.otuskotlin.herodotus.common.models.HerodotusError
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.stubs.ReportStubs
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.stubValidationUnknownEvent(title: String) = worker {
    this.title = title
    this.description = """
        Validation error: event is not known
    """.trimIndent()

    on { stubCase == ReportStubs.UNKNOWN_EVENT && state == JobState.RUNNING }

    handle {
        fail(
            HerodotusError(
                group = "validation",
                code = "unknown-event",
                field = "event",
                message = "Event ${reportRequest.event.asString()} is not defined for application ${reportRequest.applicationId.asString()}"
            )
        )
    }
}
