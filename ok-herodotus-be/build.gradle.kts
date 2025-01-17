plugins {
    alias(libs.plugins.kotlin.jvm) apply false
}

group = "ru.otus.otuskotlin.herodotus"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("spec-v1", specDir.file("specs-v1.yaml").toString())
}

tasks {
    arrayOf("build", "clean", "check").forEach { task ->
        create(task) {
            group = "build"
            dependsOn(
                subprojects.map { it.getTasksByName(task, false) }
            )
        }
    }
}
