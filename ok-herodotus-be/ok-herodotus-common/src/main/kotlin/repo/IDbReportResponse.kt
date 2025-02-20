package ru.otus.otuskotlin.herodotus.common.repo

import ru.otus.otuskotlin.herodotus.common.models.HerodotusError
import ru.otus.otuskotlin.herodotus.common.models.Report

sealed interface IDbReportResponse: IDbResponse<Report>

data class DbReportResponseOk(
    val data: Report
): IDbReportResponse

data class DbReportResponseErr(
    val errors: List<HerodotusError> = emptyList()
): IDbReportResponse {
    constructor(error: HerodotusError): this(listOf(error))
}

data class DbReportResponseErrWithData(
    val data: Report,
    val errors: List<HerodotusError> = emptyList()
): IDbReportResponse {
    constructor(report: Report, error: HerodotusError): this(report, listOf(error))
}
