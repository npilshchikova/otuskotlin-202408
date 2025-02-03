package ru.otus.otuskotlin.herodotus.common.exceptions

import ru.otus.otuskotlin.herodotus.common.models.ReportCommand


class UnknownReportCommand(command: ReportCommand) : Throwable("Wrong command $command at mapping to transport stage")
