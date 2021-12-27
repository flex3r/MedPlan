plugins {
    id("multiplatform-setup")
}

kotlin {
    sourceSets {
        named("commonMain") {

        }

        named("desktopMain") {
            dependencies {
                implementation(Deps.JetBrains.Coroutines.core)
                implementation(Deps.Vandeseer.Easytable.easytable)
            }
        }
    }
}