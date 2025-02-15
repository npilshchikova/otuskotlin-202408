package ru.otus.otuskotlin.herodotus.biz.validation

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.helpers.errorValidation
import ru.otus.otuskotlin.herodotus.common.helpers.fail
import ru.otus.otuskotlin.herodotus.common.models.ReportId
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.validateReportIdNotEmpty(title: String) = worker {
    this.title = title
    on { reportValidating.reportId.asString().isEmpty() || reportValidating.reportId == ReportId.NONE }
    handle {
        fail(
            errorValidation(
                field = "reportId",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
