package com.flxrs.medplan.common.profiles.store

import com.arkivanov.mvikotlin.core.store.Store
import com.flxrs.medplan.common.profiles.MedPlanProfilesItem
import com.flxrs.medplan.common.profiles.store.MedPlanProfilesStore.Intent
import com.flxrs.medplan.common.profiles.store.MedPlanProfilesStore.State

internal interface MedPlanProfilesStore : Store<Intent, State, Nothing> {
    sealed class Intent {
        data class AddProfile(val name: String) : Intent()
        data class UpdateProfile(val id: Long, val name: String): Intent()
        data class DeleteProfile(val id: Long) : Intent()
    }

    data class State(
        val items: List<MedPlanProfilesItem> = emptyList(),
    )
}