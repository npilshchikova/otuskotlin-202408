package ru.otus.otuskotlin.herodotus.common.models

data class ReportResumeFilter(
    val fieldName: String,
) {
    fun deepCopy(): ReportResumeFilter = copy()

    companion object {
        val NONE = ReportResumeFilter(fieldName = "")
    }
}
