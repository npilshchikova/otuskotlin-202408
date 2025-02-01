package ru.otus.otuskotlin.herodotus.app.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.herodotus.logging.common.LoggerProvider
import ru.otus.otuskotlin.herodotus.logging.loggerLogback

fun Application.getLoggerProviderConf(): LoggerProvider = LoggerProvider { loggerLogback(it) }
