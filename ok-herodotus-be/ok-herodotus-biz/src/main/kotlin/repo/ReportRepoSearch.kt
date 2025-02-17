package ru.otus.otuskotlin.herodotus.biz.repo

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.helpers.fail
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.repo.DbReportFilterRequest
import ru.otus.otuskotlin.herodotus.common.repo.DbReportsResponseErr
import ru.otus.otuskotlin.herodotus.common.repo.DbReportsResponseOk
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Search for reports in DB using filters"
    on { state == JobState.RUNNING }
    handle {
        val request = DbReportFilterRequest(
            applicationId = reportFilterValidated.applicationId,
            events = reportFilterValidated.events,
            searchFields = reportFilterValidated.searchFields,
        )
        when (val result = reportRepo.searchReport(request)) {
            is DbReportsResponseOk -> reportsRepoDone = result.data.toMutableList()
            is DbReportsResponseErr -> fail(result.errors)
        }
    }
}
