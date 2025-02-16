package ru.otus.otuskotlin.herodotus.common.repo

import ru.otus.otuskotlin.herodotus.common.models.HerodotusError
import ru.otus.otuskotlin.herodotus.common.models.ReportId

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(reportId: ReportId) = DbReportResponseErr(
    HerodotusError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "reportId",
        message = "Object with ID ${reportId.asString()} is not found",
    )
)

val errorEmptyReportId = DbReportResponseErr(
    HerodotusError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "reportId",
        message = "ID must not be null or blank"
    )
)
