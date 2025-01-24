package ru.otus.otuskotlin.herodotus.common

import ru.otus.otuskotlin.herodotus.logging.common.LoggerProvider

data class ReportCorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
) {
    companion object {
        val NONE = ReportCorSettings()
    }
}
