package ru.otus.otuskotlin.herodotus.repo.common

import ru.otus.otuskotlin.herodotus.common.models.Report

class ReportRepoInitialized(
    private val repo: IRepoReportInitializable,
    initObjects: Collection<Report> = emptyList(),
) : IRepoReportInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<Report> = save(initObjects).toList()
}
