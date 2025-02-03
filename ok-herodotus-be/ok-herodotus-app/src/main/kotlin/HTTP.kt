package ru.otus.otuskotlin.herodotus.app

import io.ktor.server.application.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.defaultheaders.*

fun Application.configureHTTP() {
    install(CachingHeaders)
    install(DefaultHeaders)
}
