package com.flxrs.medplan.common.main.integration

import com.flxrs.medplan.common.main.MedPlanMain.Model
import com.flxrs.medplan.common.main.store.MedPlanMainStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        items = it.items,
        profile = it.profile,
    )
}