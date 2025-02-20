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

    testImplementation(kotlin("test-junit"))
    testImplementation(project(":ok-herodotus-repo-tests"))
}
