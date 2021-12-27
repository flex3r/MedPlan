plugins {
    id("multiplatform-compose-setup")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":common:main"))
                implementation(project(":common:profiles"))
                implementation(project(":common:root"))
                implementation(Deps.ArkIvanov.Decompose.decompose)
                implementation(Deps.ArkIvanov.Decompose.extensionsCompose)
            }
        }
        all {
            languageSettings {
                optIn("com.arkivanov.decompose.ExperimentalDecomposeApi")
                optIn("kotlin.RequiresOptIn")
            }
        }
    }
}