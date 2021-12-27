package com.flxrs.medplan.common.main.store

import com.arkivanov.mvikotlin.core.store.Store
import com.flxrs.medplan.common.main.MedPlanMainItem
import com.flxrs.medplan.common.main.Usage
import com.flxrs.medplan.common.main.store.MedPlanMainStore.Intent
import com.flxrs.medplan.common.main.store.MedPlanMainStore.State

internal interface MedPlanMainStore : Store<Intent, State, Nothing> {
    sealed class Intent {
        data class AddItem(val name: String) : Intent()
        data class DeleteItem(val id: Long) : Intent()
        data class SetName(val id: Long, val name: String) : Intent()
        data class SetUsage(val id: Long, val usage: Usage) : Intent()
        object Print : Intent()
    }

    data class State(
        val profile: String = "",
        val items: List<MedPlanMainItem> = emptyList(),
    )
}