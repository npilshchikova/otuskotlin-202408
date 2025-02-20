package ru.otus.otuskotlin.herodotus.repo.tests

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import ru.otus.otuskotlin.herodotus.common.models.ApplicationId
import ru.otus.otuskotlin.herodotus.common.models.Event
import ru.otus.otuskotlin.herodotus.common.models.Report
import ru.otus.otuskotlin.herodotus.common.models.ReportSearchFilter
import ru.otus.otuskotlin.herodotus.common.repo.DbReportFilterRequest
import ru.otus.otuskotlin.herodotus.common.repo.DbReportsResponseOk
import ru.otus.otuskotlin.herodotus.common.repo.IRepoReport
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoReportSearchTest {
    abstract val repo: IRepoReport

    protected open val initializedObjects: List<Report> = initObjects

    @Test
    fun searchApplicationId() = runRepoTest {
        val result = repo.searchReport(
            DbReportFilterRequest(applicationId = ApplicationId("Hi!"))
        )
        assertIs<DbReportsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.reportId.asString() }
        assertEquals(expected, result.data.sortedBy { it.reportId.asString() })
    }

    @Test
    fun searchEvents() = runRepoTest {
        val result = repo.searchReport(
            DbReportFilterRequest(
                events = listOf(Event("New"), Event("Special"))
            )
        )
        assertIs<DbReportsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[2]).sortedBy { it.reportId.asString() }
        assertEquals(expected, result.data.sortedBy { it.reportId.asString() })
    }

    @Test
    fun searchContent() = runRepoTest {
        val result = repo.searchReport(
            DbReportFilterRequest(
                searchFields = listOf(
                    ReportSearchFilter.StringSearchField(
                        fieldName = "sample",
                        action = ReportSearchFilter.SearchAction.EQUALS,
                        stringValue = "Sample1",
                    ),
                    ReportSearchFilter.NumericSearchField(
                        fieldName = "totalSamples",
                        action = ReportSearchFilter.SearchAction.MORE,
                        numericValue = 1,
                    )
                )
            )
        )
        assertIs<DbReportsResponseOk>(result)
        val expected = listOf(initializedObjects[0], initializedObjects[4]).sortedBy { it.reportId.asString() }
        assertEquals(expected, result.data.sortedBy { it.reportId.asString() })
    }

    companion object: BaseInitReports("search") {
        private val searchApplicationId = ApplicationId("Hi!")
        private val searchEvent = Event("New")
        override val initObjects: List<Report> = listOf(
            createInitTestModel(
                "test1",
                applicationId = ApplicationId("Hmm"),
                event = Event("Old"),
                content = buildJsonObject {
                    put("sample", "Sample1")
                }
            ),
            createInitTestModel(
                "test2",
                applicationId = searchApplicationId,
                event = searchEvent,
                content = buildJsonObject {
                    put("sample", "Sample2")
                    put("totalSamples", "NA")
                }
            ),
            createInitTestModel(
                "test3",
                applicationId = ApplicationId("Eee!"),
                event = searchEvent,
                content = buildJsonObject {
                    put("sample", 211)
                    put("totalSamples", 0)
                }
            ),
            createInitTestModel(
                "test4",
                applicationId = searchApplicationId,
                event = Event("Create")
            ),
            createInitTestModel(
                "test5",
                applicationId = ApplicationId("Hmm"),
                event = Event("Update"),
                content = buildJsonObject {
                    put("totalSamples", 96)
                }
            ),
        )
    }
}
