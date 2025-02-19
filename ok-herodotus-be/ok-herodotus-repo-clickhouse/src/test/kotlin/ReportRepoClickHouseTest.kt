package ru.otus.otuskotlin.herodotus.repo.clickhouse

import io.mockk.coEvery
import io.mockk.mockk
import ru.otus.otuskotlin.herodotus.common.repo.*
import ru.otus.otuskotlin.herodotus.stubs.ReportStub
import kotlin.test.Test

class ReportRepoClickHouseTest {
    private val repo = mockk<ReportRepoClickHouse>()
    private val stub = ReportStub.get()

    @Test
    fun testCreate() {
        coEvery { repo.createReport(DbReportRequest(stub)) } returns DbReportResponseOk(stub)
    }

    @Test
    fun testRead() {
        coEvery { repo.readReport(DbReportIdRequest(stub.reportId)) } returns DbReportResponseOk(stub)
    }

    @Test
    fun testDelete() {
        coEvery { repo.deleteReport(DbReportIdRequest(stub.reportId)) } returns DbReportResponseOk(stub)
    }

    @Test
    fun testSearch() {
        coEvery { repo.searchReport(DbReportFilterRequest()) } returns DbReportsResponseOk(listOf(stub))
    }
}
