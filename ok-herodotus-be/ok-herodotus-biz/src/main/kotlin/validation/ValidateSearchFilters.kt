package ru.otus.otuskotlin.herodotus.biz.validation

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.helpers.errorValidation
import ru.otus.otuskotlin.herodotus.common.helpers.fail
import ru.otus.otuskotlin.herodotus.common.models.ApplicationId
import ru.otus.otuskotlin.herodotus.common.models.Event
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.chain
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.validateSearchFilters(title: String) = chain {
    this.title = title
    on { state == JobState.RUNNING }

    worker("Trim strings in search fields") {
        reportFilterValidating = reportFilterValidating.copy(
            applicationId = ApplicationId(reportFilterValidating.applicationId.asString().trim()),
            events = reportFilterValidating.events.map {
                Event(it.asString().trim())
            },
            searchFields = reportFilterValidating.searchFields.map {
                it.deepCopy(it.fieldName.trim())
            }
        )
    }

    worker {
        this.title = "Validate applicationId is not empty"
        on { reportFilterValidating.applicationId.asString().isEmpty() }
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

    worker {
        this.title = "Validate events is not empty"
        on { state == JobState.RUNNING && reportFilterValidating.events.any { it == Event.NONE || it.asString().isEmpty() } }
        handle {
            fail(
                errorValidation(
                    field = "events",
                    violationCode = "empty",
                    description = "field must not be empty"
                )
            )
        }
    }

    worker {
        this.title = "Validate searchFields is not empty"
        on { state == JobState.RUNNING && reportFilterValidating.searchFields.any { it.fieldName.isEmpty() } }
        handle {
            fail(
                errorValidation(
                    field = "searchFieldName",
                    violationCode = "empty",
                    description = "Search field names must not be empty"
                )
            )
        }
    }
}
