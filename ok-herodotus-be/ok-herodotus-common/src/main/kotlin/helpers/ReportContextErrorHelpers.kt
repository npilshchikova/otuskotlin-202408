package ru.otus.otuskotlin.herodotus.common.helpers

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.HerodotusError
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.logging.common.LogLevel

fun ReportContext.addError(error: HerodotusError) = errors.add(error)
fun ReportContext.addErrors(error: Collection<HerodotusError>) = errors.addAll(error)

fun ReportContext.fail(error: HerodotusError) {
    addError(error)
    state = JobState.FAILING
}

fun ReportContext.fail(errors: Collection<HerodotusError>) {
    addErrors(errors)
    state = JobState.FAILING
}

fun errorValidation(
    field: String,
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = HerodotusError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = HerodotusError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)
