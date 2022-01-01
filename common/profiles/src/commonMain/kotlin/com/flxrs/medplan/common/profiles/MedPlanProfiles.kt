package com.flxrs.medplan.common.profiles

import com.arkivanov.decompose.value.Value

interface MedPlanProfiles {

    val models: Value<Model>

    fun onProfileClicked(id: Long)

    fun onAddProfileClicked(name: String)

    fun onDeleteProfileClicked(id: Long)

    fun onUpdateProfileClicked(id: Long, name: String)

    data class Model(
        val profiles: List<MedPlanProfilesItem>
    )

    sealed class Output {
        data class ProfileSelected(val profileId: Long) : Output()
    }
}