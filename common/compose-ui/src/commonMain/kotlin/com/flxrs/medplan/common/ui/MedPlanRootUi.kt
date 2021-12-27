package com.flxrs.medplan.common.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import com.flxrs.medplan.common.root.MedPlanRoot
import com.flxrs.medplan.common.root.MedPlanRoot.Child

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun MedPlanRootContent(component: MedPlanRoot) {
    Children(routerState = component.routerState, animation = crossfadeScale()) {
        when (val child = it.instance) {
            is Child.Main     -> MedPlanMainContent(child.component)
            is Child.Profiles -> MedPlanProfilesContent(child.component)
        }
    }
}