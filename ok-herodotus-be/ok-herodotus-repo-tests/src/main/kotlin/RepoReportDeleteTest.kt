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
import kotlin.test.assertNotNull

abstract class RepoReportDeleteTest {
    abstract val repo: IRepoReport
    protected open val deleteSuccess = initObjects[0]
    protected open val notFoundId = ReportId("repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteReport(DbReportIdRequest(deleteSuccess.reportId))
        assertIs<DbReportResponseOk>(result)
        assertEquals(deleteSuccess.applicationId, result.data.applicationId)
        assertEquals(deleteSuccess.event, result.data.event)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.deleteReport(DbReportIdRequest(notFoundId))

        assertIs<DbReportResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    companion object : BaseInitReports("delete") {
        override val initObjects: List<Report> = listOf(
            createInitTestModel("delete"),
        )
    }
}
