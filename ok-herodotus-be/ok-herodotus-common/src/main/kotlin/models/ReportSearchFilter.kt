package ru.otus.otuskotlin.herodotus.common.models

data class ReportSearchFilter(
    val applicationId: ApplicationId = ApplicationId.NONE,
    val events: List<Event> = emptyList(),
    val searchFields: List<SearchField> = emptyList(),
) {
    fun deepCopy(): ReportSearchFilter = copy(
        events = events.toMutableList().toList(),
        searchFields = searchFields.toMutableList().toList()
    )

    interface SearchField {
        val fieldName: String
        val action: SearchAction
        fun deepCopy(fieldName: String? = null): SearchField
    }

    data class NumericSearchField  (
        override val fieldName: String,
        override val action: SearchAction = SearchAction.EQUALS,
        val numericValue: Int,
    ) : SearchField {
        override fun deepCopy(fieldName: String?): SearchField {
            fieldName?.let {
                return this.copy(fieldName = it)
            }
            return this.copy()
        }
    }

    data class StringSearchField(
        override val fieldName: String,
        override val action: SearchAction = SearchAction.CONTAINS,
        val stringValue: String,
    ) : SearchField {
        override fun deepCopy(fieldName: String?): SearchField {
            fieldName?.let {
                return this.copy(fieldName = it)
            }
            return this.copy()
        }
    }

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
