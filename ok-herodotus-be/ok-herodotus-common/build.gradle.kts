plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

sourceSets {
    main {
        java.srcDir("src/commonMain/kotlin")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(kotlin("test-junit"))
}
