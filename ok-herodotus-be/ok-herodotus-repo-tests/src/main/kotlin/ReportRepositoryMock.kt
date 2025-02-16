package ru.otus.otuskotlin.herodotus.repo.tests

import ru.otus.otuskotlin.herodotus.common.models.Report
import ru.otus.otuskotlin.herodotus.common.repo.*

class ReportRepositoryMock(
    private val invokeCreateReport: (DbReportRequest) -> IDbReportResponse = { DEFAULT_REPORT_SUCCESS_EMPTY_MOCK },
    private val invokeReadReport: (DbReportIdRequest) -> IDbReportResponse = { DEFAULT_REPORT_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteReport: (DbReportIdRequest) -> IDbReportResponse = { DEFAULT_REPORT_SUCCESS_EMPTY_MOCK },
    private val invokeSearchReport: (DbReportFilterRequest) -> IDbReportsResponse = { DEFAULT_REPORTS_SUCCESS_EMPTY_MOCK },
): IRepoReport {
    override suspend fun createReport(request: DbReportRequest): IDbReportResponse {
        return invokeCreateReport(request)
    }

    override suspend fun readReport(request: DbReportIdRequest): IDbReportResponse {
        return invokeReadReport(request)
    }

    override suspend fun deleteReport(request: DbReportIdRequest): IDbReportResponse {
        return invokeDeleteReport(request)
    }

    override suspend fun searchReport(request: DbReportFilterRequest): IDbReportsResponse {
        return invokeSearchReport(request)
    }

    companion object {
        val DEFAULT_REPORT_SUCCESS_EMPTY_MOCK = DbReportResponseOk(Report())
        val DEFAULT_REPORTS_SUCCESS_EMPTY_MOCK = DbReportsResponseOk(emptyList())
    }
}
