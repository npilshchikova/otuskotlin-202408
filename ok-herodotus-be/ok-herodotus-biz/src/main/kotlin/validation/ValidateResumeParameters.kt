package ru.otus.otuskotlin.herodotus.biz.validation

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.helpers.errorValidation
import ru.otus.otuskotlin.herodotus.common.helpers.fail
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.chain
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.validateResumeParameters(title: String) = chain {
    this.title = title
    on { state == JobState.RUNNING }

    worker("Trim fieldName string") {
        reportResumeValidating = reportResumeValidating.copy(
            fieldName = reportResumeValidating.fieldName.trim()
        )
    }

    worker {
        this.title = "Validate fieldName is not empty"
        on { reportResumeValidating.fieldName.isEmpty() }
        handle {
            fail(
                errorValidation(
                    field = "fieldName",
                    violationCode = "empty",
                    description = "Resume field name must not be empty"
                )
            )
        }
    }
}
