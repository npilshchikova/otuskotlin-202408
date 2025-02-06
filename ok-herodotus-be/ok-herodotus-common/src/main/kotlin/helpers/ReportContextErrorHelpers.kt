package ru.otus.otuskotlin.herodotus.common.helpers

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.HerodotusError
import ru.otus.otuskotlin.herodotus.common.models.JobState


fun ReportContext.addError(vararg error: HerodotusError) = errors.addAll(error)

fun ReportContext.fail(error: HerodotusError) {
    addError(error)
    state = JobState.FAILING
}
