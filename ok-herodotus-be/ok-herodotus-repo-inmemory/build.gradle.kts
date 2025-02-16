plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.db.cache4k)
    implementation(libs.uuid)
    implementation(project(":ok-herodotus-common"))
    api(project(":ok-herodotus-repo-common"))

    testImplementation(kotlin("test-junit"))
    testImplementation(project(":ok-herodotus-repo-tests"))
}
