object Deps {
    object JetBrains {
        object Kotlin {
            private const val VERSION = "1.6.10"
            const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$VERSION"
        }

        object Compose {
            private const val VERSION = "1.0.1"
            const val gradlePlugin = "org.jetbrains.compose:compose-gradle-plugin:$VERSION"
        }

        object Coroutines {
            private const val VERSION = "1.6.0"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$VERSION"
        }
    }

    object ArkIvanov {
        object MVIKotlin {
            private const val VERSION = "3.0.0-alpha02"
            const val rx = "com.arkivanov.mvikotlin:rx:$VERSION"
            const val mvikotlin = "com.arkivanov.mvikotlin:mvikotlin:$VERSION"
            const val mvikotlinMain = "com.arkivanov.mvikotlin:mvikotlin-main:$VERSION"
            const val mvikotlinLogging = "com.arkivanov.mvikotlin:mvikotlin-logging:$VERSION"
            const val mvikotlinTimeTravel = "com.arkivanov.mvikotlin:mvikotlin-timetravel:$VERSION"
            const val mvikotlinExtensionsCoroutines = "com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:$VERSION"
        }

        object Decompose {
            private const val VERSION = "0.4.0"
            const val decompose = "com.arkivanov.decompose:decompose:$VERSION"
            const val extensionsCompose = "com.arkivanov.decompose:extensions-compose-jetbrains:$VERSION"
        }
    }

    object Squareup {
        object SQLDelight {
            private const val VERSION = "1.5.3"
            const val gradlePlugin = "com.squareup.sqldelight:gradle-plugin:$VERSION"
            const val sqliteDriver = "com.squareup.sqldelight:sqlite-driver:$VERSION"
            const val coroutines = "com.squareup.sqldelight:coroutines-extensions:$VERSION"
        }
    }

    object Apache {
        object PdfBox {
            private const val VERSION = "2.0.25"
            const val pdfbox = "org.apache.pdfbox:pdfbox:$VERSION"
        }
    }

    object Vandeseer {
        object Easytable {
            private const val VERSION = "0.8.5"
            const val easytable = "com.github.vandeseer:easytable:$VERSION"
        }
    }
}