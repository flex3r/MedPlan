package com.flxrs.medplan.common.main.integration

import com.flxrs.medplan.common.database.ItemEntity
import com.flxrs.medplan.common.database.MedPlanSharedDatabase
import com.flxrs.medplan.common.database.UsageEntity
import com.flxrs.medplan.common.main.MedPlanMainItem
import com.flxrs.medplan.common.main.Usage
import com.flxrs.medplan.common.main.UsageTime
import com.flxrs.medplan.common.main.store.MedPlanMainStoreProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MedPlanMainStoreDatabase(
    private val database: MedPlanSharedDatabase
) : MedPlanMainStoreProvider.Database {

    override fun observeItems(profileId: Long): Flow<List<MedPlanMainItem>> =
        database
            .observeItems(profileId)
            .map { it.toItems() }

    override suspend fun addItem(profileId: Long, name: String) =
        database.addItem(name, profileId)

    override suspend fun deleteItem(id: Long) =
        database.deleteItem(id)

    override suspend fun setName(id: Long, name: String) =
        database.setItemName(name, id)

    override suspend fun setUsage(id: Long, usage: Usage) =
        database.setItemUsage(usage.toEntity(), id)

    override suspend fun getProfileName(profileId: Long): String =
        database.getProfileName(profileId)

    private fun List<ItemEntity>.toItems(): List<MedPlanMainItem> =
        map {
            MedPlanMainItem(
                id = it.id,
                profileId = it.profileId,
                name = it.name,
                usages = it.usages.toUsages()
            )
        }


    private fun List<UsageEntity>.toUsages(): List<Usage> =
        map {
            Usage(
                time = UsageTime.valueOf(it.time),
                amount = it.amount,
            )
        }

    private fun Usage.toEntity(): UsageEntity =
        UsageEntity(
            time = time.toString(),
            amount = amount
        )
}