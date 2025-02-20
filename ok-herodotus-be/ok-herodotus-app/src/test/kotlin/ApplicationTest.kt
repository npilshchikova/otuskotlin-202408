package ru.otus.otuskotlin.herodotus.app

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.herodotus.app.common.HerodotusAppSettingsData
import ru.otus.otuskotlin.herodotus.common.ReportCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module(HerodotusAppSettingsData(corSettings = ReportCorSettings()))
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

}
