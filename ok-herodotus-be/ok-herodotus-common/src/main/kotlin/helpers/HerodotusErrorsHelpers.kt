package ru.otus.otuskotlin.herodotus.common.helpers

import ru.otus.otuskotlin.herodotus.common.models.HerodotusError
import ru.otus.otuskotlin.herodotus.logging.common.LogLevel

fun Throwable.asHerodotusError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = HerodotusError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

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
