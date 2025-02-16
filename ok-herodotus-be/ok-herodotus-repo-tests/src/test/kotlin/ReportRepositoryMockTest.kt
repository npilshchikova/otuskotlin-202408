package ru.otus.otuskotlin.herodotus.backend.repo.tests

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.herodotus.common.models.ApplicationId
import ru.otus.otuskotlin.herodotus.common.models.Report
import ru.otus.otuskotlin.herodotus.common.repo.*
import ru.otus.otuskotlin.herodotus.repo.tests.ReportRepositoryMock
import ru.otus.otuskotlin.herodotus.stubs.ReportStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ReportRepositoryMockTest {
    private val repo = ReportRepositoryMock(
        invokeCreateReport = { DbReportResponseOk(
            ReportStub.prepareReport(applicationId = ApplicationId("create"))
        ) },
        invokeReadReport = { DbReportResponseOk(
            ReportStub.prepareReport(applicationId = ApplicationId("read"))
        ) },
        invokeDeleteReport = { DbReportResponseOk(
            ReportStub.prepareReport(applicationId = ApplicationId("delete"))
        ) },
        invokeSearchReport = { DbReportsResponseOk(
            listOf(ReportStub.prepareReport(applicationId = ApplicationId("search")))
        ) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createReport(DbReportRequest(Report()))
        assertIs<DbReportResponseOk>(result)
        assertEquals("create", result.data.applicationId.asString())
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readReport(DbReportIdRequest(Report()))
        assertIs<DbReportResponseOk>(result)
        assertEquals("read", result.data.applicationId.asString())
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteReport(DbReportIdRequest(Report()))
        assertIs<DbReportResponseOk>(result)
        assertEquals("delete", result.data.applicationId.asString())
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchReport(DbReportFilterRequest())
        assertIs<DbReportsResponseOk>(result)
        assertEquals("search", result.data.first().applicationId.asString())
    }

}
