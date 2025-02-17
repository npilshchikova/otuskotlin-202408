package ru.otus.otuskotlin.herodotus.biz.repo

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.helpers.fail
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.repo.DbReportRequest
import ru.otus.otuskotlin.herodotus.common.repo.DbReportResponseErr
import ru.otus.otuskotlin.herodotus.common.repo.DbReportResponseErrWithData
import ru.otus.otuskotlin.herodotus.common.repo.DbReportResponseOk
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Add report to DB"
    on { state == JobState.RUNNING }
    handle {
        val request = DbReportRequest(reportRepoPrepare)
        when(val result = reportRepo.createReport(request)) {
            is DbReportResponseOk -> reportRepoDone = result.data
            is DbReportResponseErr -> fail(result.errors)
            is DbReportResponseErrWithData -> fail(result.errors)
        }
    }
}
