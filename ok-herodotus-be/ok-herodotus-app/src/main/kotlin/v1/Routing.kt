package ru.otus.otuskotlin.herodotus.app.v1

import io.ktor.server.routing.*
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettings

fun Route.v1Report(appSettings: HerodotusAppSettings) {
    route("report") {
        post("create") {
            call.createReport(appSettings)
        }
        post("read") {
            call.readReport(appSettings)
        }
        post("delete") {
            call.deleteReport(appSettings)
        }
        post("search") {
            call.searchReport(appSettings)
        }
        post("resume") {
            call.resumeReports(appSettings)
        }
    }
}