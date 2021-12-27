plugins {
    id("multiplatform-setup")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("MedPlanDatabase") {
        packageName = "com.flxrs.medplan.database"
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:utils"))
                implementation(Deps.JetBrains.Coroutines.core)
                implementation(Deps.Squareup.SQLDelight.coroutines)
            }
        }

        desktopMain {
            dependencies {
                implementation(Deps.Squareup.SQLDelight.sqliteDriver)
            }
        }
    }
}