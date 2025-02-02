package ru.otus.otuskotlin.herodotus.app

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettings
import ru.otus.otuskotlin.herodotus.app.plugins.initAppSettings

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(
    appSettings: HerodotusAppSettings = initAppSettings(),
) {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        /* TODO
            Это временное решение, оно опасно.
            В боевом приложении здесь должны быть конкретные настройки
        */
        anyHost()
    }
    configureHTTP()
    configureSerialization()

    configureRouting(appSettings)
}
