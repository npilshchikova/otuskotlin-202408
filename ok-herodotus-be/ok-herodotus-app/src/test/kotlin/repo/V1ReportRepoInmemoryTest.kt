package ru.otus.otuskotlin.herodotus.app.repo

import ru.otus.otuskotlin.herodotus.api.v1.models.DebugMode
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettings
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettingsData
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import ru.otus.otuskotlin.herodotus.common.repo.IRepoReport
import ru.otus.otuskotlin.herodotus.repo.common.ReportRepoInitialized
import ru.otus.otuskotlin.herodotus.repo.inmemory.ReportRepoInMemory

class V1ReportRepoInmemoryTest : V1ReportRepoBaseTest() {
    override val workMode: DebugMode = DebugMode.TEST
    private fun appSettings(repo: IRepoReport) = HerodotusAppSettingsData(
        corSettings = ReportCorSettings(
            repoTest = repo
        )
    )

    override val appSettingsCreate: HerodotusAppSettings = appSettings(
        repo = ReportRepoInitialized(
            ReportRepoInMemory(randomUuid = { uuidNew })
        )
    )
    override val appSettingsRead: HerodotusAppSettings = appSettings(
        repo = ReportRepoInitialized(
            ReportRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initReport),
        )
    )
    override val appSettingsDelete: HerodotusAppSettings = appSettings(
        repo = ReportRepoInitialized(
            ReportRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initReport),
        )
    )
    override val appSettingsSearch: HerodotusAppSettings = appSettings(
        repo = ReportRepoInitialized(
            ReportRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initReport),
        )
    )
    override val appSettingsResume: HerodotusAppSettings = appSettings(
        repo = ReportRepoInitialized(
            ReportRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initReport),
        )
    )
}
