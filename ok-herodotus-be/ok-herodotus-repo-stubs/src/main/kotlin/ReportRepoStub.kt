package ru.otus.otuskotlin.herodotus.repo.stub

import ru.otus.otuskotlin.herodotus.common.models.ApplicationId
import ru.otus.otuskotlin.herodotus.common.repo.*
import ru.otus.otuskotlin.herodotus.stubs.ReportStub

class ReportRepoStub : IRepoReport {
    override suspend fun createReport(request: DbReportRequest): IDbReportResponse {
        return DbReportResponseOk(
            data = ReportStub.get(),
        )
    }

    override suspend fun readReport(request: DbReportIdRequest): IDbReportResponse {
        return DbReportResponseOk(
            data = ReportStub.get(),
        )
    }

    override suspend fun deleteReport(request: DbReportIdRequest): IDbReportResponse {
        return DbReportResponseOk(
            data = ReportStub.get(),
        )
    }

    override suspend fun searchReport(request: DbReportFilterRequest): IDbReportsResponse {
        return DbReportsResponseOk(
            data = ReportStub.prepareSearchList(
                applicationId = ApplicationId.NONE,
                events = listOf(),
            ),
        )
    }
}
