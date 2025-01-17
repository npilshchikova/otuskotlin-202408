package ru.otus.otuskotlin.herodotus.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class Event(private val event: String) {
    fun asString() = event

    companion object {
        val NONE = Event("")
    }
}