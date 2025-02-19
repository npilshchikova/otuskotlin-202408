plugins {
    id("build-jvm")
    alias(libs.plugins.ktor)
    alias(libs.plugins.muschko.remote)
}

group = rootProject.group
version = rootProject.version

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

ktor {
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(JavaVersion.VERSION_21)
    }
}

jib {
    container.mainClass = application.mainClass.get()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.yaml)
    implementation(libs.ktor.server.negotiation)
    implementation(libs.ktor.server.headers.default)
    implementation(libs.ktor.server.headers.response)
    implementation(libs.ktor.server.headers.caching)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.calllogging)
    implementation(libs.ktor.server.websocket)

    // transport models
    implementation(project(":ok-herodotus-common"))
    implementation(project(":ok-herodotus-api-v1"))
    implementation(project(":ok-herodotus-api-v1-mappers"))

    // logic
    implementation(project(":ok-herodotus-biz"))

    // stubs
    implementation(project(":ok-herodotus-stubs"))

    // database
    implementation(project(":ok-herodotus-repo-stubs"))
    implementation(project(":ok-herodotus-repo-inmemory"))
    implementation(project(":ok-herodotus-repo-clickhouse"))

    // logging
    implementation(project(":ok-herodotus-api-log1"))
    implementation(libs.logging)

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.ktor.server.test)
    testImplementation(libs.ktor.client.negotiation)
    testImplementation(project(":ok-herodotus-repo-common"))
}
