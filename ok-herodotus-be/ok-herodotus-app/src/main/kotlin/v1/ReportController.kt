package ru.otus.otuskotlin.herodotus.app.v1

import io.ktor.server.application.*
import ru.otus.otuskotlin.herodotus.api.v1.models.*
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettings
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createReport::class
suspend fun ApplicationCall.createReport(appSettings: HerodotusAppSettings) =
    processV1<ReportCreateRequest, ReportCreateResponse>(appSettings, clCreate,"create")

val clRead: KClass<*> = ApplicationCall::createReport::class
suspend fun ApplicationCall.readReport(appSettings: HerodotusAppSettings) =
    processV1<ReportReadRequest, ReportReadResponse>(appSettings, clRead, "read")

val clDelete: KClass<*> = ApplicationCall::createReport::class
suspend fun ApplicationCall.deleteReport(appSettings: HerodotusAppSettings) =
    processV1<ReportDeleteRequest, ReportDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::createReport::class
suspend fun ApplicationCall.searchReport(appSettings: HerodotusAppSettings) =
    processV1<ReportSearchRequest, ReportSearchResponse>(appSettings, clSearch, "search")

val clResume: KClass<*> = ApplicationCall::createReport::class
suspend fun ApplicationCall.resumeReports(appSettings: HerodotusAppSettings) =
    processV1<ReportResumeRequest, ReportResumeResponse>(appSettings, clResume, "resume")
