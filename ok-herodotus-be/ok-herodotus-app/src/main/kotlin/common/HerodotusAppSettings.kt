package ru.otus.otuskotlin.herodotus.app.common

import ru.otus.otuskotlin.herodotus.biz.ReportProcessor
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings

interface HerodotusAppSettings {
    val processor: ReportProcessor
    val corSettings: ReportCorSettings
}
