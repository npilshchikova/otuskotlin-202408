package ru.otus.otuskotlin.herodotus.common.models

import ru.otus.otuskotlin.herodotus.logging.common.LogLevel

data class HerodotusError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)
