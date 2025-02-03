package ru.otus.otuskotlin.herodotus.common.models

@JvmInline
value class ReportId(private val id: String) {
    constructor(
        applicationId: ApplicationId,
        event: Event,
        reportNumber: Int
    ) : this("${applicationId.asString()}_${event.asString()}_${reportNumber}")

    fun asString() = id

    companion object {
        val NONE = ReportId("")
    }
}
