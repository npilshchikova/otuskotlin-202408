package ru.otus.otuskotlin.herodotus.common.helpers

import ru.otus.otuskotlin.herodotus.common.models.HerodotusError

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
