package ru.otus.otuskotlin.herodotus.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.ApplicationId
import ru.otus.otuskotlin.herodotus.common.models.Event
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.models.ReportSearchFilter
import ru.otus.otuskotlin.herodotus.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchFiltersTest {
    @Test
    fun emptySearchParams() = runTest {
        val context = ReportContext(
            state = JobState.RUNNING,
            reportFilterValidating = ReportSearchFilter(),
        )
        chain.exec(context)
        assertEquals(JobState.RUNNING, context.state)
        assertEquals(0, context.errors.size)
    }

    @Test
    fun validApplicationId() = runTest {
        val context = ReportContext(
            state = JobState.RUNNING,
            reportFilterValidating = ReportSearchFilter(
                applicationId = ApplicationId("Test"),
            )
        )
        chain.exec(context)
        assertEquals(JobState.RUNNING, context.state)
        assertEquals(0, context.errors.size)
    }

    @Test
    fun trimApplicationId() = runTest {
        val context = ReportContext(
            state = JobState.RUNNING,
            reportFilterValidating = ReportSearchFilter(
                applicationId = ApplicationId("Test    "),
            )
        )
        chain.exec(context)
        assertEquals(JobState.RUNNING, context.state)
        assertEquals(0, context.errors.size)
    }

    @Test
    fun validEvents() = runTest {
        val context = ReportContext(
            state = JobState.RUNNING,
            reportFilterValidating = ReportSearchFilter(
                events = listOf(Event("Test1"), Event("test2"))
            )
        )
        chain.exec(context)
        assertEquals(JobState.RUNNING, context.state)
        assertEquals(0, context.errors.size)
    }

    @Test
    fun trimEvents() = runTest {
        val context = ReportContext(
            state = JobState.RUNNING,
            reportFilterValidating = ReportSearchFilter(
                events = listOf(Event("Test1\t"), Event("test2   "))
            )
        )
        chain.exec(context)
        assertEquals(JobState.RUNNING, context.state)
        assertEquals(0, context.errors.size)
    }

    @Test
    fun emptyEvents() = runTest {
        val context = ReportContext(
            state = JobState.RUNNING,
            reportFilterValidating = ReportSearchFilter(
                events = listOf(Event("Test1"), Event("   "), Event.NONE)
            )
        )
        chain.exec(context)
        assertEquals(JobState.FAILING, context.state)
        assertEquals(1, context.errors.size)
        assertEquals("events", context.errors.firstOrNull()?.field)
    }

    @Test
    fun validSearchFields() = runTest {
        val context = ReportContext(
            state = JobState.RUNNING,
            reportFilterValidating = ReportSearchFilter(
                searchFields = listOf(
                    ReportSearchFilter.StringSearchField(
                        fieldName = "Organization",
                        action = ReportSearchFilter.SearchAction.EQUALS,
                        stringValue = "Parseq",
                    ),
                    ReportSearchFilter.NumericSearchField(
                        fieldName = "Samples",
                        action = ReportSearchFilter.SearchAction.MORE,
                        numericValue = 2,
                    )
                )
            )
        )
        chain.exec(context)
        assertEquals(JobState.RUNNING, context.state)
        assertEquals(0, context.errors.size)
    }

    @Test
    fun trimSearchFields() = runTest {
        val context = ReportContext(
            state = JobState.RUNNING,
            reportFilterValidating = ReportSearchFilter(
                searchFields = listOf(
                    ReportSearchFilter.StringSearchField(
                        fieldName = "Organization\t",
                        action = ReportSearchFilter.SearchAction.EQUALS,
                        stringValue = "Parseq",
                    ),
                    ReportSearchFilter.NumericSearchField(
                        fieldName = "Samples\n   ",
                        action = ReportSearchFilter.SearchAction.MORE,
                        numericValue = 2,
                    )
                )
            )
        )
        chain.exec(context)
        assertEquals(JobState.RUNNING, context.state)
        assertEquals(0, context.errors.size)
    }

    @Test
    fun emptySearchFields() = runTest {
        val context = ReportContext(
            state = JobState.RUNNING,
            reportFilterValidating = ReportSearchFilter(
                searchFields = listOf(
                    ReportSearchFilter.StringSearchField(
                        fieldName = "\t",
                        action = ReportSearchFilter.SearchAction.EQUALS,
                        stringValue = "Parseq",
                    ),
                    ReportSearchFilter.NumericSearchField(
                        fieldName = "Samples\n   ",
                        action = ReportSearchFilter.SearchAction.MORE,
                        numericValue = 2,
                    )
                )
            )
        )
        chain.exec(context)
        assertEquals(JobState.FAILING, context.state)
        assertEquals(1, context.errors.size)
        assertEquals("searchFieldName", context.errors.firstOrNull()?.field)
    }

    companion object {
        val chain = rootChain {
            validateSearchFilters("")
        }
    }
}
