package ru.otus.otuskotlin.herodotus.common.repo

import ru.otus.otuskotlin.herodotus.common.models.ApplicationId
import ru.otus.otuskotlin.herodotus.common.models.Event
import ru.otus.otuskotlin.herodotus.common.models.ReportSearchFilter

data class DbReportFilterRequest(
    val applicationId: ApplicationId = ApplicationId.NONE,
    val events: List<Event> = emptyList(),
    val searchFields: List<ReportSearchFilter.SearchField> = emptyList(),
)
