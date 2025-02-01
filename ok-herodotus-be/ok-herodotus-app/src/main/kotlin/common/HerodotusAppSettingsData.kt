package ru.otus.otuskotlin.herodotus.app.common

import ru.otus.otuskotlin.herodotus.biz.ReportProcessor
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings

data class HerodotusAppSettingsData(
    val appUrls: List<String> = emptyList(),
    override val corSettings: ReportCorSettings = ReportCorSettings(),
    override val processor: ReportProcessor = ReportProcessor(corSettings),
): HerodotusAppSettings
