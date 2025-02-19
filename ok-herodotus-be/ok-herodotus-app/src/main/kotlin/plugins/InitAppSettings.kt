package ru.otus.otuskotlin.herodotus.app.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.herodotus.app.base.KtorWsSessionRepo
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettings
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettingsData
import ru.otus.otuskotlin.herodotus.biz.ReportProcessor
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import ru.otus.otuskotlin.herodotus.repo.stub.ReportRepoStub

fun Application.initAppSettings(): HerodotusAppSettings {
    val corSettings = ReportCorSettings(
        loggerProvider = getLoggerProviderConf(),
        wsSessions = KtorWsSessionRepo(),
        repoTest = getDatabaseConf(DbType.TEST),
        repoProd = getDatabaseConf(DbType.PROD),
        repoStub = ReportRepoStub(),
    )
    return HerodotusAppSettingsData(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = ReportProcessor(corSettings),
    )
}
