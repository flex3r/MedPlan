plugins {
    id("kotlin-multiplatform")
}

kotlin {
    jvm("desktop")

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "16"
    }
}