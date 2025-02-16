package ru.otus.otuskotlin.herodotus.common.repo

interface IRepoReport {
    suspend fun createReport(request: DbReportRequest): IDbReportResponse
    suspend fun readReport(request: DbReportIdRequest): IDbReportResponse
    suspend fun deleteReport(request: DbReportIdRequest): IDbReportResponse
    suspend fun searchReport(request: DbReportFilterRequest): IDbReportsResponse
    companion object {
        val NONE = object : IRepoReport {
            override suspend fun createReport(request: DbReportRequest): IDbReportResponse {
                throw NotImplementedError("Still not implemented. Wait")
            }

            override suspend fun readReport(request: DbReportIdRequest): IDbReportResponse {
                throw NotImplementedError("Still not implemented. Wait")
            }

            override suspend fun deleteReport(request: DbReportIdRequest): IDbReportResponse {
                throw NotImplementedError("Still not implemented. Wait")
            }

            override suspend fun searchReport(request: DbReportFilterRequest): IDbReportsResponse {
                throw NotImplementedError("Still not implemented. Wait")
            }
        }
    }
}
