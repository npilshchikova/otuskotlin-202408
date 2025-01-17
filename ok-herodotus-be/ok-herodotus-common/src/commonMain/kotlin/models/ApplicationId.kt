package ru.otus.otuskotlin.herodotus.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class ApplicationId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = ApplicationId("")
    }
}
