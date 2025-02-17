package ru.otus.otuskotlin.herodotus.biz.general

import ru.otus.otuskotlin.herodotus.biz.exceptions.DbNotConfiguredException
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.helpers.errorSystem
import ru.otus.otuskotlin.herodotus.common.helpers.fail
import ru.otus.otuskotlin.herodotus.common.models.WorkMode
import ru.otus.otuskotlin.herodotus.common.repo.IRepoReport
import ru.otus.otuskotlin.herodotus.cor.ICorChainDsl
import ru.otus.otuskotlin.herodotus.cor.worker

fun ICorChainDsl<ReportContext>.initRepo(title: String) = worker {
    this.title = title
    description = "Estimate main working repo depending on work mode".trimIndent()
    handle {
        reportRepo = when (workMode) {
            WorkMode.TEST -> corSettings.repoTest
            WorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != WorkMode.STUB && reportRepo == IRepoReport.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = DbNotConfiguredException(workMode)
            )
        )
    }
}
