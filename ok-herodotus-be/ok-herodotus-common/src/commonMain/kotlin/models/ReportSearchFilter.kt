package ru.otus.otuskotlin.herodotus.common.models

data class ReportSearchFilter(
    val applicationId: ApplicationId = ApplicationId.NONE,
    val events: List<Event> = emptyList(),
    val searchFields: List<SearchField> = emptyList(),
) {
    interface SearchField {
        val fieldName: String
        val action: SearchAction
    }

    data class NumericSearchField  (
        override val fieldName: String,
        override val action: SearchAction = SearchAction.EQUALS,
        val numericValue: Int,
    ) : SearchField

    data class StringSearchField(
        override val fieldName: String,
        override val action: SearchAction = SearchAction.CONTAINS,
        val stringValue: String,
    ) : SearchField

    enum class SearchAction {
        CONTAINS,
        EQUALS,
        MORE,
        LESS
    }

    companion object {
        val NONE = ReportSearchFilter()
    }
}
