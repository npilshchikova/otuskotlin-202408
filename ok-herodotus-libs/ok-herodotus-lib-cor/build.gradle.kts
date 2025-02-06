plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.coroutines.core)

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.coroutines.test)
}
