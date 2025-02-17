package ru.otus.otuskotlin.herodotus.biz.validation

import ru.otus.otuskotlin.herodotus.biz.ReportProcessor
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import ru.otus.otuskotlin.herodotus.common.models.ReportCommand
import ru.otus.otuskotlin.herodotus.repo.common.ReportRepoInitialized
import ru.otus.otuskotlin.herodotus.repo.inmemory.ReportRepoInMemory
import ru.otus.otuskotlin.herodotus.stubs.ReportStub

abstract class BaseBizValidationTest {
    protected abstract val command: ReportCommand
    private val repo = ReportRepoInitialized(
        repo = ReportRepoInMemory(),
        initObjects = listOf(
            ReportStub.get(),
        ),
    )
    private val settings by lazy { ReportCorSettings() }
    protected val processor by lazy { ReportProcessor(settings) }
}
