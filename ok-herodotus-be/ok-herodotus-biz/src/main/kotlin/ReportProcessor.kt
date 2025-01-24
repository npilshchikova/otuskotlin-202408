package ru.otus.otuskotlin.herodotus.biz

import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.stubs.ReportStub
import ru.otus.otuskotlin.herodotus.stubs.ReportStubSample

@Suppress("unused", "RedundantSuspendModifier")
class ReportProcessor(val corSettings: ReportCorSettings) {
    suspend fun exec(context: ReportContext) {
        context.reportResponse = ReportStub.get()
        context.reportsResponse = ReportStub.prepareSearchList(
            applicationId = ReportStubSample.applicationId,
            event = ReportStubSample.event,
            searchFields = emptyList(),
        ).toMutableList()
        context.state = JobState.RUNNING
    }
}
