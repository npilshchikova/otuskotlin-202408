package ru.otus.otuskotlin.herodotus.app.base

import io.ktor.websocket.*
import ru.otus.otuskotlin.herodotus.api.v1.apiV1ResponseSerialize
import ru.otus.otuskotlin.herodotus.api.v1.models.IResponse
import ru.otus.otuskotlin.herodotus.common.ws.WsSession

data class KtorWsSessionV1(
    private val session: WebSocketSession
) : WsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is IResponse)
        session.send(Frame.Text(apiV1ResponseSerialize(obj)))
    }
}
