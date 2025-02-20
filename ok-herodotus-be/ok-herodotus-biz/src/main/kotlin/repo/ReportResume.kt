package ru.otus.otuskotlin.herodotus.biz.repo

import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.models.JobState
import ru.otus.otuskotlin.herodotus.common.models.ReportSummary
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.reportsResume(title: String) = worker {
    this.title = title
    description = "Resume search results"
    on { state == JobState.RUNNING }
    handle {
        reportResumeValidated
        resumeRepoDone = ReportSummary(
            itemsNumber = reportsRepoDone.size,
            summary = reportsRepoDone.map {
                val fieldValue = it.content[reportResumeValidated.fieldName]

                ReportSummary.SummaryValue(
                    fieldValue = when (fieldValue) {
                        is JsonPrimitive -> {
                            fieldValue.contentOrNull ?: "NA"
                        }
                        else -> fieldValue.toString()
                    },
                    event = it.event,
                    timestamp = it.timestamp,
                )
            }
        )
    }
}
