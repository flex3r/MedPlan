package com.flxrs.medplan.common.profiles.integration

import com.flxrs.medplan.common.database.MedPlanSharedDatabase
import com.flxrs.medplan.common.database.ProfileEntity
import com.flxrs.medplan.common.profiles.MedPlanProfilesItem
import com.flxrs.medplan.common.profiles.store.MedPlanProfilesStoreProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MedPlanProfilesStoreDatabase(
    private val database: MedPlanSharedDatabase
) : MedPlanProfilesStoreProvider.Database {
    override fun observeProfiles(): Flow<List<MedPlanProfilesItem>> =
        database
            .observeProfiles()
            .map { it.toProfiles() }

    override suspend fun addProfile(name: String) =
        database.addProfile(name)

    override suspend fun deleteProfile(id: Long) =
        database.deleteProfile(id)

    private fun List<ProfileEntity>.toProfiles(): List<MedPlanProfilesItem> =
        map {
            MedPlanProfilesItem(
                name = it.name,
                profileId = it.id,
            )
        }
}