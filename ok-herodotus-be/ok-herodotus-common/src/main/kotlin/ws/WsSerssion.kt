package ru.otus.otuskotlin.herodotus.common.ws

interface WsSession {
    suspend fun <T> send(obj: T)
    companion object {
        val NONE = object : WsSession {
            override suspend fun <T> send(obj: T) {

            }
        }
    }
}
