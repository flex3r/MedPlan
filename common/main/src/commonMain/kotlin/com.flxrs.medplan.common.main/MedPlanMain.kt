package com.flxrs.medplan.common.main

import com.arkivanov.decompose.value.Value

interface MedPlanMain {

    val models: Value<Model>

    fun onItemNameChanged(id: Long, name: String)

    fun onItemUsageChanged(id: Long, usage: Usage)

    fun onItemDeleteClicked(id: Long)

    fun onAddItemClicked(name: String)

    fun onBackClicked()

    fun onPrintClicked()

    data class Model(
        val profile: String,
        val items: List<MedPlanMainItem>,
    )

    sealed class Output {
        object Finished : Output()
    }
}