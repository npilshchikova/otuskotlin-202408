package ru.otus.otuskotlin.herodotus.biz.validation

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.herodotus.common.NONE
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.helpers.errorValidation
import ru.otus.otuskotlin.herodotus.common.helpers.fail
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.validateTimestampNotEmpty(title: String) = worker {
    this.title = title
    on { reportValidating.timestamp == Instant.NONE }
    handle {
        fail(
            errorValidation(
                field = "timestamp",
                violationCode = "empty",
                description = "field must contain value"
            )
        )
    }
}
