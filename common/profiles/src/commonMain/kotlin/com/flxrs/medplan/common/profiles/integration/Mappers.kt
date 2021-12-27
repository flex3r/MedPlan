package com.flxrs.medplan.common.profiles.integration

import com.flxrs.medplan.common.profiles.MedPlanProfiles.Model
import com.flxrs.medplan.common.profiles.store.MedPlanProfilesStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        profiles = it.items
    )
}