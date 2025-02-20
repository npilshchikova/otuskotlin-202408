package ru.otus.otuskotlin.herodotus.repo.common

import ru.otus.otuskotlin.herodotus.common.models.Report
import ru.otus.otuskotlin.herodotus.common.repo.IRepoReport

interface IRepoReportInitializable: IRepoReport {
    fun save(reports: Collection<Report>) : Collection<Report>
}
