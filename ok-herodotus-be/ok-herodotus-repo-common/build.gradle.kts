plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.coroutines.core)
    implementation(project(":ok-herodotus-common"))

    testImplementation(kotlin("test-junit"))
}
