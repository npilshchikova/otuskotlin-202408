plugins {
    alias(libs.plugins.kotlin.jvm) apply false
}

group = "ru.otus.otuskotlin.herodotus"
version = "0.0.1"

tasks {
    create("build") {
        group = "build"
        dependsOn(project(":ok-herodotus-tmp").getTasksByName("build",false))
    }

    create("check") {
        group = "verification"
        subprojects.forEach { proj ->
            println("$proj")
            proj.getTasksByName("check", false).also {
                this@create.dependsOn(it)
            }
        }
    }
}
