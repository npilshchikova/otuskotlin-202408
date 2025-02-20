package ru.otus.otuskotlin.herodotus.repo.tests

import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import ru.otus.otuskotlin.herodotus.common.models.ApplicationId
import ru.otus.otuskotlin.herodotus.common.models.Event
import ru.otus.otuskotlin.herodotus.common.models.Report
import ru.otus.otuskotlin.herodotus.common.models.ReportId

abstract class BaseInitReports(private val operation: String): IInitObjects<Report> {
    fun createInitTestModel(
        suffix: String,
        applicationId: ApplicationId = ApplicationId("Test"),
        event: Event = Event("Test"),
        content: JsonObject = buildJsonObject {
            put("sampleId", suffix)
            put("case", "one more really stupid stub")
            put("info", "stub-$operation-$suffix")
        }
    ) = Report(
        reportId = ReportId("report-repo-$operation-$suffix"),
        applicationId = applicationId,
        event = event,
        timestamp =  Instant.fromEpochMilliseconds(123456),
        content = content
    )
}
