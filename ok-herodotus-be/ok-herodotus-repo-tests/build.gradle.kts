plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    api(libs.coroutines.core)
    api(libs.coroutines.test)
    implementation(project(":ok-herodotus-common"))
    implementation(project(":ok-herodotus-repo-common"))
    implementation(kotlin("test-junit"))

    testImplementation(project(":ok-herodotus-stubs"))
}
