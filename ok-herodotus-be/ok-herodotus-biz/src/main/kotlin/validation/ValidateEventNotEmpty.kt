package ru.otus.otuskotlin.herodotus.biz.validation

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.helpers.errorValidation
import ru.otus.otuskotlin.herodotus.common.helpers.fail
import ru.otus.otuskotlin.herodotus.common.models.Event
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.validateEventNotEmpty(title: String) = worker {
    this.title = title
    on { reportValidating.event == Event.NONE || reportValidating.event.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "event",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
