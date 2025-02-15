package ru.otus.otuskotlin.herodotus.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.models.ReportCommand
import ru.otus.otuskotlin.herodotus.common.models.ReportSearchFilter
import ru.otus.otuskotlin.herodotus.common.models.WorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest: BaseBizValidationTest() {
    override val command = ReportCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val context = ReportContext(
            command = command,
            state = JobState.NONE,
            workMode = WorkMode.TEST,
            reportFilterRequest = ReportSearchFilter()
        )
        processor.exec(context)
        assertEquals(0, context.errors.size)
        assertNotEquals(JobState.FAILING, context.state)
    }
}
