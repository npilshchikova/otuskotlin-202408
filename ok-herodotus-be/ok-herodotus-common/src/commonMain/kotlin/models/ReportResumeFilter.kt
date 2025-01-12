package ru.otus.otuskotlin.herodotus.common.models

data class ReportResumeFilter(
    val fieldName: String,
) {
    companion object {
        val NONE = ReportResumeFilter(fieldName = "")
    }
}
