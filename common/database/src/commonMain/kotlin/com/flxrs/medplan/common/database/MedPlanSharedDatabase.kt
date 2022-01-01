package com.flxrs.medplan.common.database

import com.flxrs.medplan.common.utils.replace
import com.flxrs.medplan.database.MedPlanDatabase
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MedPlanSharedDatabase(driver: SqlDriver) {
    private val usagesAdapter = object : ColumnAdapter<List<UsageEntity>, String> {
        override fun decode(databaseValue: String): List<UsageEntity> = when {
            databaseValue.isEmpty() -> emptyList()
            else                    -> databaseValue.split(",").map {
                val (time, amount) = it.split("-", limit = 2)
                UsageEntity(time, amount)
            }
        }

        override fun encode(value: List<UsageEntity>): String =
            value.joinToString(",") {
                "${it.time}-${it.amount}"
            }
    }
    private val database = MedPlanDatabase(driver, ItemEntity.Adapter(usagesAdapter))


    fun observeProfiles(): Flow<List<ProfileEntity>> =
        database.medPlanProfileQueries
            .selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)

    suspend fun addProfile(name: String) = withContext(Dispatchers.Default) {
        database.medPlanProfileQueries.insertProfile(name)
    }

    suspend fun deleteProfile(id: Long) = withContext(Dispatchers.Default) {
        database.medPlanProfileQueries.removeProfile(id)
    }

    suspend fun getProfileName(id: Long) = withContext(Dispatchers.Default) {
        database.medPlanProfileQueries.getName(id).executeAsOne()
    }

    suspend fun updateProfile(id: Long, name: String) = withContext(Dispatchers.Default) {
        database.medPlanProfileQueries.updateProfile(name, id)
    }

    fun observeItems(profileId: Long): Flow<List<ItemEntity>> =
        database.medPlanItemQueries
            .getItemsForProfile(profileId)
            .asFlow()
            .mapToList(Dispatchers.Default)

    suspend fun addItem(name: String, profileId: Long) = withContext(Dispatchers.Default) {
        database.medPlanItemQueries.addItem(name, profileId, emptyList())
    }

    suspend fun setItemName(name: String, id: Long) = withContext(Dispatchers.Default) {
        database.medPlanItemQueries.setName(name, id)
    }

    suspend fun setItemUsage(usage: UsageEntity, id: Long) = withContext(Dispatchers.Default) {
        val usages = database.medPlanItemQueries
            .getUsages(id)
            .executeAsOneOrNull()
            .orEmpty()
            .replace(usage, UsageEntity::time)

        database.medPlanItemQueries.setUsages(usages, id)
    }

    suspend fun deleteItem(id: Long) = withContext(Dispatchers.Default) {
        database.medPlanItemQueries.delete(id)
    }
}