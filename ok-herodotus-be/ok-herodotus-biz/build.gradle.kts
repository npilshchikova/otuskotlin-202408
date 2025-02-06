plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.coroutines.core)
    implementation(project(":ok-herodotus-common"))
    implementation(project(":ok-herodotus-stubs"))
    implementation(libs.cor)

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.coroutines.test)
}
