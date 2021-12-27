package com.flxrs.desktop

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.lightColors
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.flxrs.medplan.common.database.MedPlanDatabaseDriver
import com.flxrs.medplan.common.database.MedPlanSharedDatabase
import com.flxrs.medplan.common.root.MedPlanRoot
import com.flxrs.medplan.common.root.integration.MedPlanRootComponent
import com.flxrs.medplan.common.ui.MedPlanRootContent

fun main() {
    val lifecycle = LifecycleRegistry()
    val root = medPlanRoot(DefaultComponentContext(lifecycle = lifecycle))

    application {
        val windowState = rememberWindowState(width = 1280.dp, height = 720.dp, position = WindowPosition(Alignment.Center))
        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "MedPlan",
        ) {
            MaterialTheme(
                colors = lightColors() //if (isSystemInDarkTheme()) darkColors() else lightColors()
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    CompositionLocalProvider(
                        LocalScrollbarStyle provides ScrollbarStyle(
                            minimalHeight = 16.dp,
                            thickness = 8.dp,
                            shape = MaterialTheme.shapes.small,
                            hoverDurationMillis = 300,
                            unhoverColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                            hoverColor = MaterialTheme.colors.onSurface.copy(alpha = 0.50f)
                        )
                    ) {
                        MedPlanRootContent(root)
                    }
                }
            }
        }
    }
}

private fun medPlanRoot(componentContext: ComponentContext): MedPlanRoot =
    MedPlanRootComponent(
        componentContext = componentContext,
        storeFactory = DefaultStoreFactory(),
        database = MedPlanSharedDatabase(MedPlanDatabaseDriver())
    )