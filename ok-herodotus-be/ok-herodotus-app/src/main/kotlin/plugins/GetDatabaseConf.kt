package ru.otus.otuskotlin.herodotus.app.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.herodotus.app.configs.ClickHouseConfig
import ru.otus.otuskotlin.herodotus.app.configs.ConfigPaths
import ru.otus.otuskotlin.herodotus.common.repo.IRepoReport
import ru.otus.otuskotlin.herodotus.repo.clickhouse.DbProperties
import ru.otus.otuskotlin.herodotus.repo.clickhouse.ReportRepoClickHouse
import ru.otus.otuskotlin.herodotus.repo.inmemory.ReportRepoInMemory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

fun Application.getDatabaseConf(type: DbType): IRepoReport {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "db", "clickhouse" -> initClickHouse()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: 'inmemory', 'clickhouse'"
        )
    }
}

enum class DbType(val confName: String) {
    PROD("prod"),
    TEST("test")
}

fun Application.initInMemory(): IRepoReport {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return ReportRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}

fun Application.initClickHouse(): IRepoReport {
    val config = ClickHouseConfig(environment.config)
    return ReportRepoClickHouse(
        properties = DbProperties(
            host = config.host,
            port = config.port,
            user = config.user,
            password = config.password,
        )
    )
}
