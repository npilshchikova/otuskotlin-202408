package ru.otus.otuskotlin.herodotus.biz.stubs

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.helpers.fail
import ru.otus.otuskotlin.herodotus.common.models.HerodotusError
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.stubs.ReportStubs
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.stubValidationUnknownApplication(title: String) = worker {
    this.title = title
    this.description = """
        Validation error: application is not registered
    """.trimIndent()

    on { stubCase == ReportStubs.UNKNOWN_APPLICATION && state == JobState.RUNNING }

    handle {
        fail(
            HerodotusError(
                group = "validation",
                code = "unknown-app",
                field = "applicationId",
                message = "Event ${reportRequest.applicationId.asString()} is not registered"
            )
        )
    }
}
