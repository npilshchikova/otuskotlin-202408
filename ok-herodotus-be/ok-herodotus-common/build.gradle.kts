plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    api("ru.otus.otuskotlin.herodotus.libs:ok-herodotus-lib-logging")

    testImplementation(kotlin("test-junit"))
}
