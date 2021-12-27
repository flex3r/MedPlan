package com.flxrs.medplan.common.root.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.flxrs.medplan.common.database.MedPlanSharedDatabase
import com.flxrs.medplan.common.main.MedPlanMain
import com.flxrs.medplan.common.main.integration.MedPlanMainComponent
import com.flxrs.medplan.common.profiles.MedPlanProfiles
import com.flxrs.medplan.common.profiles.integration.MedPlanProfilesComponent
import com.flxrs.medplan.common.root.MedPlanRoot
import com.flxrs.medplan.common.root.MedPlanRoot.Child

class MedPlanRootComponent internal constructor(
    componentContext: ComponentContext,
    private val medPlanMain: (ComponentContext, profileId: Long, (MedPlanMain.Output) -> Unit) -> MedPlanMain,
    private val medPlanProfiles: (ComponentContext, (MedPlanProfiles.Output) -> Unit) -> MedPlanProfiles,
) : MedPlanRoot, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        database: MedPlanSharedDatabase,
    ) : this(
        componentContext = componentContext,
        medPlanMain = { childContext, profileId, output ->
            MedPlanMainComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                database = database,
                profileId = profileId,
                output = output,
            )
        },
        medPlanProfiles = { childContext, output ->
            MedPlanProfilesComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                database = database,
                output = output,
            )
        }
    )

    private val router =
        router<Configuration, Child>(
            initialConfiguration = Configuration.Profiles,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override val routerState: Value<RouterState<*, Child>> = router.state

    private fun createChild(configuration: Configuration, componentContext: ComponentContext): Child =
        when (configuration) {
            is Configuration.Main -> Child.Main(medPlanMain(componentContext, configuration.profileId, ::onMainOutput))
            is Configuration.Profiles -> Child.Profiles(medPlanProfiles(componentContext, ::onProfilesOutput))
        }

    private fun onMainOutput(output: MedPlanMain.Output) =
        when (output) {
            is MedPlanMain.Output.Finished -> router.pop()
        }

    private fun onProfilesOutput(output: MedPlanProfiles.Output) =
        when (output) {
            is MedPlanProfiles.Output.ProfileSelected -> router.push(Configuration.Main(output.profileId))
        }

    private sealed class Configuration : Parcelable {
        @Parcelize
        data class Main(val profileId: Long) : Configuration()

        @Parcelize
        object Profiles : Configuration()
    }
}