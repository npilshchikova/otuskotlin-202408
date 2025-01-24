package ru.otus.otuskotlin.herodotus.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class ReportId(private val id: String) {
    constructor(
        applicationId: ApplicationId,
        event: Event,
        reportNumber: Int
    ) : this("${applicationId}_${event}_${reportNumber}")

    fun asString() = id

    companion object {
        val NONE = ReportId("")
    }
}
