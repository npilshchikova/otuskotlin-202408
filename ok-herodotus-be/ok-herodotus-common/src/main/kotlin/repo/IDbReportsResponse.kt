package ru.otus.otuskotlin.herodotus.common.repo

import ru.otus.otuskotlin.herodotus.common.models.HerodotusError
import ru.otus.otuskotlin.herodotus.common.models.Report

sealed interface IDbReportsResponse: IDbResponse<List<Report>>

data class DbReportsResponseOk(
    val data: List<Report>
): IDbReportsResponse

@Suppress("unused")
data class DbReportsResponseErr(
    val errors: List<HerodotusError> = emptyList()
): IDbReportsResponse {
    constructor(error: HerodotusError): this(listOf(error))
}
