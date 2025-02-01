package ru.otus.otuskotlin.herodotus.app

import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import ru.otus.otuskotlin.herodotus.api.v1.apiV1Mapper

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson {
            setConfig(apiV1Mapper.serializationConfig)
            setConfig(apiV1Mapper.deserializationConfig)
        }
    }
}
