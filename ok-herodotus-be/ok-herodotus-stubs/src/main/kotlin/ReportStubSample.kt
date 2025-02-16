package ru.otus.otuskotlin.herodotus.stubs

import kotlinx.datetime.Instant
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import ru.otus.otuskotlin.herodotus.common.models.ApplicationId
import ru.otus.otuskotlin.herodotus.common.models.Event
import ru.otus.otuskotlin.herodotus.common.models.Report
import ru.otus.otuskotlin.herodotus.common.models.ReportId
import kotlin.time.Duration.Companion.hours

object ReportStubSample {
    val applicationId = ApplicationId("BamAnalyzer")
    val event = Event("NewSample")
    val timestamp = Instant.parse("2016-10-31T01:30:00.000-05:00")

    val REPORT_SAMPLE_1: Report
        get() = Report(
            reportId = ReportId(applicationId, event, 1),
            applicationId = applicationId,
            event = event,
            timestamp = timestamp,
            content = buildJsonObject {
                put("sampleId", "B18")
                put("organization", "ParseqLab")
                put("readsNumber", 5e9)
                put("qcStatus", "PASSED")
            }
        )

    val REPORT_SAMPLE_2: Report
        get() = Report(
        reportId = ReportId(applicationId, event, 2),
        applicationId = applicationId,
        event = event,
        timestamp = timestamp.plus(1.hours),
        content = buildJsonObject {
            put("sampleId", "CONTROL")
            put("organization", "ParseqLab")
            put("qcStatus", "FAILED")
        }
    )
}
