package ru.otus.otuskotlin.herodotus.repo.tests

import ru.otus.otuskotlin.herodotus.common.models.Report
import ru.otus.otuskotlin.herodotus.common.models.ReportId
import ru.otus.otuskotlin.herodotus.common.repo.DbReportIdRequest
import ru.otus.otuskotlin.herodotus.common.repo.DbReportResponseErr
import ru.otus.otuskotlin.herodotus.common.repo.DbReportResponseOk
import ru.otus.otuskotlin.herodotus.common.repo.IRepoReport
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoReportReadTest {
    abstract val repo: IRepoReport
    protected open val readSuccess = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readReport(DbReportIdRequest(readSuccess.reportId))

        assertIs<DbReportResponseOk>(result)
        assertEquals(readSuccess, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readReport(DbReportIdRequest(notFoundId))

        assertIs<DbReportResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("reportId", error?.field)
    }

    companion object : BaseInitReports("read") {
        override val initObjects: List<Report> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = ReportId("repo-read-notFound")
    }
}
