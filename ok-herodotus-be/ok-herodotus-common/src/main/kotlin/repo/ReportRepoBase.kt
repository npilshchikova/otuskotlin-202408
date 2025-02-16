package ru.otus.otuskotlin.herodotus.common.repo

import ru.otus.otuskotlin.herodotus.common.helpers.errorSystem

abstract class ReportRepoBase: IRepoReport {

    protected suspend fun tryReportMethod(block: suspend () -> IDbReportResponse) = try {
        block()
    } catch (e: Throwable) {
        DbReportResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryReportsMethod(block: suspend () -> IDbReportsResponse) = try {
        block()
    } catch (e: Throwable) {
        DbReportsResponseErr(errorSystem("methodException", e = e))
    }

}
