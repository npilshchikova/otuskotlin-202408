package ru.otus.otuskotlin.herodotus.common.repo

import ru.otus.otuskotlin.herodotus.common.models.Report
import ru.otus.otuskotlin.herodotus.common.models.ReportId

data class DbReportIdRequest(
    val reportId: ReportId,
) {
    constructor(report: Report): this(report.reportId)
}
