package ru.otus.otuskotlin.herodotus.biz.validation

import ru.otus.otuskotlin.herodotus.biz.ReportProcessor
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import ru.otus.otuskotlin.herodotus.common.models.ReportCommand

abstract class BaseBizValidationTest {
    protected abstract val command: ReportCommand
    private val settings by lazy { ReportCorSettings() }
    protected val processor by lazy { ReportProcessor(settings) }
}
