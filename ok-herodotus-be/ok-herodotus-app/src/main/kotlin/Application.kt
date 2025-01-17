package ru.otus.otuskotlin.herodotus.app

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureMonitoring()
    configureRouting()
}
