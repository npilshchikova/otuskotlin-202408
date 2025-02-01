package ru.otus.otuskotlin.herodotus.app.common

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.herodotus.api.log1.mapper.toLog
import ru.otus.otuskotlin.herodotus.common.ReportContext
import ru.otus.otuskotlin.herodotus.common.helpers.asHerodotusError
import ru.otus.otuskotlin.herodotus.common.models.ReportCommand
import ru.otus.otuskotlin.herodotus.common.models.JobState
import kotlin.reflect.KClass

suspend inline fun <T> HerodotusAppSettings.controllerHelper(
    crossinline getRequest: suspend ReportContext.() -> Unit,
    crossinline toResponse: suspend ReportContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = ReportContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.state = JobState.FAILING
        ctx.errors.add(e.asHerodotusError())
        processor.exec(ctx)
        if (ctx.command == ReportCommand.NONE) {
            ctx.command = ReportCommand.READ
        }
        ctx.toResponse()
    }
}
