rootProject.name = "ok-herodotus-be"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":ok-herodotus-api-v1")
include(":ok-herodotus-api-v1-mappers")
include(":ok-herodotus-api-log1")
include(":ok-herodotus-common")
include(":ok-herodotus-app")
include(":ok-herodotus-stubs")
