package com.flxrs.medplan.common.main.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.flxrs.medplan.common.database.MedPlanSharedDatabase
import com.flxrs.medplan.common.main.MedPlanMain
import com.flxrs.medplan.common.main.MedPlanMain.Model
import com.flxrs.medplan.common.main.MedPlanMain.Output
import com.flxrs.medplan.common.main.Usage
import com.flxrs.medplan.common.main.store.MedPlanMainStore.Intent
import com.flxrs.medplan.common.main.store.MedPlanMainStoreProvider
import com.flxrs.medplan.common.utils.asValue
import com.flxrs.medplan.common.utils.getStore

class MedPlanMainComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: MedPlanSharedDatabase,
    profileId: Long,
    private val output: (Output) -> Unit,
) : MedPlanMain, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        MedPlanMainStoreProvider(
            storeFactory = storeFactory,
            database = MedPlanMainStoreDatabase(database = database),
            profileId = profileId,
        ).provide()
    }

    override val models: Value<Model> = store.asValue().map(stateToModel)

    override fun onAddItemClicked(name: String) {
        store.accept(Intent.AddItem(name))
    }

    override fun onItemDeleteClicked(id: Long) {
        store.accept(Intent.DeleteItem(id))
    }

    override fun onItemNameChanged(id: Long, name: String) {
        store.accept(Intent.SetName(id, name))
    }

    override fun onItemUsageChanged(id: Long, usage: Usage) {
        store.accept(Intent.SetUsage(id, usage))
    }

    override fun onBackClicked() {
        output(Output.Finished)
    }

    override fun onPrintClicked() {
        store.accept(Intent.Print)
    }
}