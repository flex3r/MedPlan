package com.flxrs.medplan.common.profiles.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.flxrs.medplan.common.profiles.MedPlanProfilesItem
import com.flxrs.medplan.common.profiles.store.MedPlanProfilesStore.Intent
import com.flxrs.medplan.common.profiles.store.MedPlanProfilesStore.State
import com.flxrs.medplan.common.utils.replace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class MedPlanProfilesStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database,
) {

    fun provide(): MedPlanProfilesStore =
        object : MedPlanProfilesStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = "MedPlanProfilesStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Result {
        data class ProfilesLoaded(val items: List<MedPlanProfilesItem>) : Result()
        data class ProfileDeleted(val id: Long) : Result()
        data class ProfileNameChanged(val id: Long, val name: String) : Result()
        object ProfileAdded : Result()
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Result, Nothing>() {
        override fun executeAction(action: Unit, getState: () -> State) {
            scope.launch {
                database
                    .observeProfiles()
                    .map(Result::ProfilesLoaded)
                    .collectLatest(::dispatch)
            }
        }

        override fun executeIntent(intent: Intent, getState: () -> State) =
            when (intent) {
                is Intent.AddProfile    -> addProfile(intent.name)
                is Intent.DeleteProfile -> deleteProfile(intent.id)
                is Intent.UpdateProfile -> updateProfile(intent.id, intent.name)
            }

        private fun addProfile(name: String) {
            if (name.isNotBlank()) {
                dispatch(Result.ProfileAdded)
                scope.launch {
                    database.addProfile(name)
                }
            }
        }

        private fun deleteProfile(id: Long) {
            dispatch(Result.ProfileDeleted(id))
            scope.launch {
                database.deleteProfile(id)
            }
        }

        private fun updateProfile(id: Long, name: String) {
            dispatch(Result.ProfileNameChanged(id, name))
            scope.launch {
                database.updateProfile(id, name)
            }
        }

    }

    private object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State =
            when (result) {
                is Result.ProfilesLoaded     -> copy(items = result.items)
                is Result.ProfileDeleted     -> copy(items = items.filterNot { it.profileId == result.id })
                is Result.ProfileAdded       -> this
                is Result.ProfileNameChanged -> update(id = result.id) { copy(name = result.name) }
            }

        private inline fun State.update(id: Long, block: MedPlanProfilesItem.() -> MedPlanProfilesItem): State {
            val profile = items.find { it.profileId == id } ?: return this

            return copy(items = items.replace(profile.block(), MedPlanProfilesItem::profileId))
        }
    }

    interface Database {
        fun observeProfiles(): Flow<List<MedPlanProfilesItem>>
        suspend fun addProfile(name: String)
        suspend fun deleteProfile(id: Long)
        suspend fun updateProfile(id: Long, name: String)
    }
}