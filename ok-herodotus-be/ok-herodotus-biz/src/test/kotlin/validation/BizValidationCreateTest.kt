package ru.otus.otuskotlin.herodotus.biz.validation

import ru.otus.otuskotlin.herodotus.common.models.ReportCommand
import kotlin.test.Test

class BizValidationCreateTest: BaseBizValidationTest() {
    override val command: ReportCommand = ReportCommand.CREATE

    @Test fun correctApplicationId() = validationApplicationIdCorrect(command, processor)
    @Test fun trimApplicationId() = validationApplicationIdTrim(command, processor)
    @Test fun emptyApplicationId() = validationApplicationIdEmpty(command, processor)

    @Test fun correctEvent() = validationEventCorrect(command, processor)
    @Test fun trimEvent() = validationEventTrim(command, processor)
    @Test fun emptyEvent() = validationEventEmpty(command, processor)

    @Test fun correctTimestamp() = validationTimestampCorrect(command, processor)
    @Test fun emptyTimestamp() = validationTimestampEmpty(command, processor)
}
