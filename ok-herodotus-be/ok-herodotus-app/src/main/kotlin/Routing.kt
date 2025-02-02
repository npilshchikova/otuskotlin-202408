package ru.otus.otuskotlin.herodotus.app

import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettings
import ru.otus.otuskotlin.herodotus.app.v1.v1Report
import ru.otus.otuskotlin.herodotus.app.v1.wsHandlerV1

fun Application.configureRouting(appSettings: HerodotusAppSettings) {
    install(AutoHeadResponse)
    install(WebSockets)

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("v1") {
            v1Report(appSettings)

            webSocket("/ws") {
                wsHandlerV1(appSettings)
            }
        }
    }
}
