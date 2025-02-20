package ru.otus.otuskotlin.herodotus.biz.repo

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.helpers.fail
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.repo.DbReportIdRequest
import ru.otus.otuskotlin.herodotus.common.repo.DbReportResponseErr
import ru.otus.otuskotlin.herodotus.common.repo.DbReportResponseErrWithData
import ru.otus.otuskotlin.herodotus.common.repo.DbReportResponseOk
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Delete report from DB by reportId"
    on { state == JobState.RUNNING }
    handle {
        val request = DbReportIdRequest(reportRepoPrepare)
        when(val result = reportRepo.deleteReport(request)) {
            is DbReportResponseOk -> reportRepoDone = result.data
            is DbReportResponseErr -> {
                fail(result.errors)
                reportRepoDone = reportRepoRead
            }
            is DbReportResponseErrWithData -> {
                fail(result.errors)
                reportRepoDone = result.data
            }
        }
    }
}
