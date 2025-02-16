package ru.otus.otuskotlin.herodotus.common.repo.exceptions

import ru.otus.otuskotlin.herodotus.common.models.ReportId

open class RepoReportException(
    @Suppress("unused")
    val reportId: ReportId,
    msg: String,
): RepoException(msg)
