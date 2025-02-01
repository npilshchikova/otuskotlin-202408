package ru.otus.otuskotlin.herodotus.app

import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettings
import ru.otus.otuskotlin.herodotus.app.v1.v1Report

fun Application.configureRouting(appSettings: HerodotusAppSettings) {
    install(AutoHeadResponse)

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("v1") {
            v1Report(appSettings)
        }
    }
}
