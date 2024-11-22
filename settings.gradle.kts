plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "otuskotlin-202408"

includeBuild("lessons")
includeBuild("ok-herodotus-be")
