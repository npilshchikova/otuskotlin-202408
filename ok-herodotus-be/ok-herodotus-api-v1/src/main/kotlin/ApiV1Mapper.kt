package ru.otus.otuskotlin.herodotus.api.v1

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.otus.otuskotlin.herodotus.api.v1.models.IRequest
import ru.otus.otuskotlin.herodotus.api.v1.models.IResponse

val apiV1Mapper = Json {
    ignoreUnknownKeys = true
}

@Suppress("unused")
fun apiV1RequestSerialize(request: IRequest): String = apiV1Mapper.encodeToString(request)

@Suppress("UNCHECKED_CAST", "unused")
fun <T : IRequest> apiV1RequestDeserialize(json: String): T =
    apiV1Mapper.decodeFromString<IRequest>(json) as T

@Suppress("unused")
fun apiV1ResponseSerialize(response: IResponse): String = apiV1Mapper.encodeToString(response)

@Suppress("UNCHECKED_CAST", "unused")
fun <T : IResponse> apiV1ResponseDeserialize(json: String): T =
    apiV1Mapper.decodeFromString<IResponse>(json) as T
