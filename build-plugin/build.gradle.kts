plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("build-jvm") {
            id = "build-jvm"
            implementationClass = "ru.otus.otuskotlin.plugin.BuildPluginJvm"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.plugin.kotlin)
}
