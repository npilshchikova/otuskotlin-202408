package ru.otus.otuskotlin.herodotus.app.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.herodotus.api.v1.models.IRequest
import ru.otus.otuskotlin.herodotus.api.v1.models.IResponse
import ru.otus.otuskotlin.herodotus.app.common.controllerHelper
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettings
import ru.otus.otuskotlin.herodotus.api.v1.mappers.fromTransport
import ru.otus.otuskotlin.herodotus.api.v1.mappers.toTransportReport
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: HerodotusAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {
        fromTransport(receive<Q>())
    },
    { respond(toTransportReport()) },
    clazz,
    logId,
)
