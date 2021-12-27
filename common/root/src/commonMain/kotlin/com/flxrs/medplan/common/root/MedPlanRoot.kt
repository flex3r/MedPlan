package com.flxrs.medplan.common.root

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import com.flxrs.medplan.common.main.MedPlanMain
import com.flxrs.medplan.common.profiles.MedPlanProfiles

interface MedPlanRoot {

    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        data class Main(val component: MedPlanMain) : Child()
        data class Profiles(val component: MedPlanProfiles) : Child()
    }
}