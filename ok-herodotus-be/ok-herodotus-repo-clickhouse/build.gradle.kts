plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.coroutines.core)
    implementation(libs.uuid)
    implementation(libs.clickhouse.client)
    implementation(project(":ok-herodotus-common"))
    api(project(":ok-herodotus-repo-common"))

    testImplementation(kotlin("test-junit"))
    testImplementation(project(":ok-herodotus-repo-tests"))
    testImplementation(project(":ok-herodotus-stubs"))
    testImplementation(libs.mockk)
}
