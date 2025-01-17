package ru.otus.otuskotlin.herodotus.common.models

data class HerodotusError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)
