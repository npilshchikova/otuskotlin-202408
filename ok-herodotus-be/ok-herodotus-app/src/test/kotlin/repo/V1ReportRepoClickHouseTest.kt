package ru.otus.otuskotlin.herodotus.app.repo

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import ru.otus.otuskotlin.herodotus.api.v1.models.DebugMode
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettings
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettingsData
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import ru.otus.otuskotlin.herodotus.common.models.ReportId
import ru.otus.otuskotlin.herodotus.common.repo.DbReportResponseOk
import ru.otus.otuskotlin.herodotus.common.repo.DbReportsResponseOk
import ru.otus.otuskotlin.herodotus.common.repo.IRepoReport
import ru.otus.otuskotlin.herodotus.repo.clickhouse.ReportRepoClickHouse
import ru.otus.otuskotlin.herodotus.repo.common.ReportRepoInitialized
import ru.otus.otuskotlin.herodotus.stubs.ReportStub

class V1ReportRepoClickHouseTest : V1ReportRepoBaseTest() {
    override val workMode: DebugMode = DebugMode.TEST
    private fun appSettings(repo: IRepoReport) = HerodotusAppSettingsData(
        corSettings = ReportCorSettings(
            repoTest = repo
        )
    )
    private val mockkRepo = mockk<ReportRepoClickHouse> {
        every { save(any()) } returnsArgument(0)
        coEvery { createReport(any()) } returns DbReportResponseOk(
            ReportStub.prepareReport(
                reportId = ReportId(uuidNew)
            )
        )
        coEvery { readReport(any()) } returns DbReportResponseOk(
            ReportStub.prepareReport(
                reportId = ReportId(uuidOld)
            )
        )
        coEvery { deleteReport(any()) } returns DbReportResponseOk(
            ReportStub.prepareReport(
                reportId = ReportId(uuidOld)
            )
        )
        coEvery { searchReport(any()) } returns DbReportsResponseOk(
            listOf(
                ReportStub.prepareReport(reportId = ReportId(uuidOld))
            )
        )
    }

    override val appSettingsCreate: HerodotusAppSettings = appSettings(
        repo = ReportRepoInitialized(
            mockkRepo
        )
    )
    override val appSettingsRead: HerodotusAppSettings = appSettings(
        repo = ReportRepoInitialized(
            mockkRepo,
            initObjects = listOf(initReport),
        )
    )
    override val appSettingsDelete: HerodotusAppSettings = appSettings(
        repo = ReportRepoInitialized(
            mockkRepo,
            initObjects = listOf(initReport),
        )
    )
    override val appSettingsSearch: HerodotusAppSettings = appSettings(
        repo = ReportRepoInitialized(
            mockkRepo,
            initObjects = listOf(initReport),
        )
    )
    override val appSettingsResume: HerodotusAppSettings = appSettings(
        repo = ReportRepoInitialized(
            mockkRepo,
            initObjects = listOf(initReport),
        )
    )
}
