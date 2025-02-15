package ru.otus.otuskotlin.herodotus.biz.validation

import ru.otus.otuskotlin.herodotus.common.models.ReportCommand
import kotlin.test.Test

class BizValidationDeleteTest: BaseBizValidationTest() {
    override val command = ReportCommand.DELETE

    @Test fun correctReportId() = validationReportIdCorrect(command, processor)
    @Test fun trimReportId() = validationReportIdTrim(command, processor)
    @Test fun emptyReportId() = validationReportIdEmpty(command, processor)
}
