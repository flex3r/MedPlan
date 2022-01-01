package com.flxrs.medplan.common.profiles.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.flxrs.medplan.common.database.MedPlanSharedDatabase
import com.flxrs.medplan.common.profiles.MedPlanProfiles
import com.flxrs.medplan.common.profiles.MedPlanProfiles.Model
import com.flxrs.medplan.common.profiles.MedPlanProfiles.Output
import com.flxrs.medplan.common.profiles.store.MedPlanProfilesStore.Intent
import com.flxrs.medplan.common.profiles.store.MedPlanProfilesStoreProvider
import com.flxrs.medplan.common.utils.asValue
import com.flxrs.medplan.common.utils.getStore

class MedPlanProfilesComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: MedPlanSharedDatabase,
    private val output: (Output) -> Unit,
) : MedPlanProfiles, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        MedPlanProfilesStoreProvider(
            storeFactory = storeFactory,
            database = MedPlanProfilesStoreDatabase(database = database),
        ).provide()
    }

    override val models: Value<Model> = store.asValue().map(stateToModel)

    override fun onAddProfileClicked(name: String) {
        store.accept(Intent.AddProfile(name = name))
    }

    override fun onDeleteProfileClicked(id: Long) {
        store.accept(Intent.DeleteProfile(id = id))
    }

    override fun onProfileClicked(id: Long) {
        output(Output.ProfileSelected(profileId = id))
    }

    override fun onUpdateProfileClicked(id: Long, name: String) {
        store.accept(Intent.UpdateProfile(id = id, name = name))
    }
}