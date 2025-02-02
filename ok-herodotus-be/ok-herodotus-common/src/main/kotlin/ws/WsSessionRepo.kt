package ru.otus.otuskotlin.herodotus.common.ws

interface WsSessionRepo {
    fun add(session: WsSession)
    fun clearAll()
    fun remove(session: WsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : WsSessionRepo {
            override fun add(session: WsSession) {}
            override fun clearAll() {}
            override fun remove(session: WsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}
