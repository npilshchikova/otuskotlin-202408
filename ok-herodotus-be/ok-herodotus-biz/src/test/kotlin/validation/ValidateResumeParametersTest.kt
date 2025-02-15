package ru.otus.otuskotlin.herodotus.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.models.ReportResumeFilter
import ru.otus.otuskotlin.herodotus.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateResumeParametersTest {
    @Test
    fun validResumeFieldName() = runTest {
        val context = ReportContext(
            state = JobState.RUNNING,
            reportResumeValidating = ReportResumeFilter("Test")
        )
        chain.exec(context)
        assertEquals(JobState.RUNNING, context.state)
        assertEquals(0, context.errors.size)
        assertEquals("Test", context.reportResumeValidating.fieldName)
    }

    @Test
    fun trimResumeFieldName() = runTest {
        val context = ReportContext(
            state = JobState.RUNNING,
            reportResumeValidating = ReportResumeFilter("Test\t\n")
        )
        chain.exec(context)
        assertEquals(JobState.RUNNING, context.state)
        assertEquals(0, context.errors.size)
        assertEquals("Test", context.reportResumeValidating.fieldName)
    }

    @Test
    fun emptyResumeFieldName() = runTest {
        val context = ReportContext(
            state = JobState.RUNNING,
            reportResumeValidating = ReportResumeFilter("   ")
        )
        chain.exec(context)
        assertEquals(JobState.FAILING, context.state)
        assertEquals(1, context.errors.size)
        assertEquals("fieldName", context.errors.firstOrNull()?.field)
    }

    companion object {
        val chain = rootChain {
            validateSearchFilters("")
        }
    }
}