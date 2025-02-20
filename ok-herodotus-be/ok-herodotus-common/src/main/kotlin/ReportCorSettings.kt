package ru.otus.otuskotlin.herodotus.common

import ru.otus.otuskotlin.herodotus.common.repo.IRepoReport
import ru.otus.otuskotlin.herodotus.common.ws.WsSessionRepo
import ru.otus.otuskotlin.herodotus.logging.common.LoggerProvider

data class ReportCorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val wsSessions: WsSessionRepo = WsSessionRepo.NONE,
    val repoStub: IRepoReport = IRepoReport.NONE,
    val repoTest: IRepoReport = IRepoReport.NONE,
    val repoProd: IRepoReport = IRepoReport.NONE,
) {
    companion object {
        val NONE = ReportCorSettings()
    }
}
