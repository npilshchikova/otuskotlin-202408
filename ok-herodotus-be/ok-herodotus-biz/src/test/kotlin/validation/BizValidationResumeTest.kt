package ru.otus.otuskotlin.herodotus.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationResumeTest: BaseBizValidationTest() {
    override val command = ReportCommand.RESUME

    @Test
    fun correctEmptySearch() = runTest {
        val context = ReportContext(
            command = command,
            state = JobState.NONE,
            workMode = WorkMode.TEST,
            reportFilterRequest = ReportSearchFilter(),
            reportResumeRequest = ReportResumeFilter("test"),
        )
        processor.exec(context)
        assertEquals(0, context.errors.size)
        assertNotEquals(JobState.FAILING, context.state)
    }
}
