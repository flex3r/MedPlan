package com.flxrs.medplan.common.main.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.flxrs.medplan.common.main.MedPlanMainItem
import com.flxrs.medplan.common.main.Usage
import com.flxrs.medplan.common.main.UsageTime
import com.flxrs.medplan.common.main.displayName
import com.flxrs.medplan.common.main.store.MedPlanMainStore.Intent
import com.flxrs.medplan.common.main.store.MedPlanMainStore.State
import com.flxrs.medplan.common.pdf.PdfCreator
import com.flxrs.medplan.common.utils.replace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class MedPlanMainStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database,
    private val profileId: Long,
) {

    fun provide(): MedPlanMainStore =
        object : MedPlanMainStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = "MedPlanMainStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Result {
        data class ItemsLoaded(val profileName: String, val items: List<MedPlanMainItem>) : Result()
        data class ItemNameChanged(val id: Long, val name: String) : Result()
        data class ItemUsageChanged(val id: Long, val usage: Usage) : Result()
        data class ItemDeleted(val id: Long) : Result()
        object ItemAdded : Result()
        object ItemsPrinted : Result()
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Result, Nothing>() {
        override fun executeAction(action: Unit, getState: () -> State) {
            scope.launch {
                val profileName = database.getProfileName(profileId)
                database
                    .observeItems(profileId)
                    .map { Result.ItemsLoaded(profileName, it) }
                    .collectLatest(::dispatch)
            }
        }

        override fun executeIntent(intent: Intent, getState: () -> State) =
            when (intent) {
                is Intent.AddItem    -> addItem(profileId, intent.name)
                is Intent.DeleteItem -> deleteItem(intent.id)
                is Intent.SetName    -> setName(intent.id, intent.name)
                is Intent.SetUsage   -> setUsage(intent.id, intent.usage)
                is Intent.Print      -> printItems(getState())
            }

        private fun addItem(profileId: Long, name: String) {
            if (name.isNotBlank()) {
                dispatch(Result.ItemAdded)
                scope.launch {
                    database.addItem(profileId, name)
                }
            }
        }

        private fun deleteItem(id: Long) {
            dispatch(Result.ItemDeleted(id))
            scope.launch {
                database.deleteItem(id)
            }
        }

        private fun setName(id: Long, name: String) {
            dispatch(Result.ItemNameChanged(id, name))
            scope.launch {
                database.setName(id, name)
            }
        }

        private fun setUsage(id: Long, usage: Usage) {
            dispatch(Result.ItemUsageChanged(id, usage))
            scope.launch {
                database.setUsage(id, usage)
            }
        }

        private fun printItems(state: State) {
            dispatch(Result.ItemsPrinted)
            scope.launch {
                val headerContent = listOf("") + UsageTime.values().map { it.displayName }
                val content = state.items.map { item ->
                    val usagesMap = item.usages.associateBy { it.time }
                    buildList {
                        add(item.name)
                        val usages = UsageTime.values().map {
                            usagesMap[it]?.amount ?: ""
                        }
                        addAll(usages)
                    }
                }

                val title = "Medikamentenplan - ${state.profile}"
                val total = listOf(headerContent) + content
                PdfCreator.createPdfWithTable(title, total)
            }
        }
    }

    private object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State =
            when (result) {
                is Result.ItemsLoaded      -> copy(profile = result.profileName, items = result.items)
                is Result.ItemAdded,
                is Result.ItemsPrinted     -> this
                is Result.ItemDeleted      -> copy(items = items.filterNot { it.id == result.id })
                is Result.ItemNameChanged  -> update(id = result.id) { copy(name = result.name) }
                is Result.ItemUsageChanged -> update(id = result.id) {
                    copy(usages = usages.replace(result.usage, Usage::time))
                }
            }

        private inline fun State.update(id: Long, block: MedPlanMainItem.() -> MedPlanMainItem): State {
            val item = items.find { it.id == id } ?: return this

            return copy(items = items.replace(item.block(), MedPlanMainItem::id))
        }
    }

    interface Database {
        fun observeItems(profileId: Long): Flow<List<MedPlanMainItem>>
        suspend fun getProfileName(profileId: Long): String
        suspend fun addItem(profileId: Long, name: String)
        suspend fun deleteItem(id: Long)
        suspend fun setName(id: Long, name: String)
        suspend fun setUsage(id: Long, usage: Usage)
    }
}