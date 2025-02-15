package ru.otus.otuskotlin.herodotus.biz.validation

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.helpers.errorValidation
import ru.otus.otuskotlin.herodotus.common.helpers.fail
import ru.otus.otuskotlin.herodotus.common.models.ApplicationId
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.validateApplicationIdNotEmpty(title: String) = worker {
    this.title = title
    on { reportValidating.applicationId == ApplicationId.NONE || reportValidating.applicationId.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "applicationId",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
