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

fun ICorChainDsl<ReportContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Report reading from DB"
    on { state == JobState.RUNNING }
    handle {
        val request = DbReportIdRequest(reportValidated)
        when(val result = reportRepo.readReport(request)) {
            is DbReportResponseOk -> reportRepoRead = result.data
            is DbReportResponseErr -> fail(result.errors)
            is DbReportResponseErrWithData -> {
                fail(result.errors)
                reportRepoRead = result.data
            }
        }
    }
}
