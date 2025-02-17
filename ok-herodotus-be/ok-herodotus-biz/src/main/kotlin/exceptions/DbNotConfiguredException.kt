package ru.otus.otuskotlin.herodotus.biz.exceptions

import ru.otus.otuskotlin.herodotus.common.models.WorkMode

class DbNotConfiguredException(val workMode: WorkMode): Exception(
    "Database is not configured properly for work mode $workMode"
)
