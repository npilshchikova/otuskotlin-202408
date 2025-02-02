package ru.otus.otuskotlin.herodotus.app.base

import ru.otus.otuskotlin.herodotus.common.ws.WsSession
import ru.otus.otuskotlin.herodotus.common.ws.WsSessionRepo

class KtorWsSessionRepo: WsSessionRepo {
    private val sessions: MutableSet<WsSession> = mutableSetOf()
    override fun add(session: WsSession) {
        sessions.add(session)
    }

    override fun clearAll() {
        sessions.clear()
    }

    override fun remove(session: WsSession) {
        sessions.remove(session)
    }

    override suspend fun <T> sendAll(obj: T) {
        sessions.forEach { it.send(obj) }
    }
}
