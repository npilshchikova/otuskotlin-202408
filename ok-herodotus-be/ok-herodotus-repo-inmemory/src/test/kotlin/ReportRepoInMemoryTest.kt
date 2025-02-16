package ru.otus.otuskotlin.herodotus.repo.inmemory

import ru.otus.otuskotlin.herodotus.repo.common.ReportRepoInitialized
import ru.otus.otuskotlin.herodotus.repo.tests.RepoReportCreateTest
import ru.otus.otuskotlin.herodotus.repo.tests.RepoReportDeleteTest
import ru.otus.otuskotlin.herodotus.repo.tests.RepoReportReadTest
import ru.otus.otuskotlin.herodotus.repo.tests.RepoReportSearchTest

class ReportRepoInMemoryCreateTest : RepoReportCreateTest() {
    override val repo = ReportRepoInitialized(
        ReportRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class ReportRepoInMemoryDeleteTest : RepoReportDeleteTest() {
    override val repo = ReportRepoInitialized(
        ReportRepoInMemory(),
        initObjects = initObjects,
    )
}

class ReportRepoInMemoryReadTest : RepoReportReadTest() {
    override val repo = ReportRepoInitialized(
        ReportRepoInMemory(),
        initObjects = initObjects,
    )
}

class ReportRepoInMemorySearchTest : RepoReportSearchTest() {
    override val repo = ReportRepoInitialized(
        ReportRepoInMemory(),
        initObjects = initObjects,
    )
}
