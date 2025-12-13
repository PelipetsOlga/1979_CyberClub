package com.application.data.repository

import com.application.data.local.db.dao.ClubStatusDao
import com.application.data.local.db.entity.ClubStatusEntity
import com.application.domain.model.ClubStatus
import com.application.domain.model.ZoneStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClubStatusRepository @Inject constructor(
    private val clubStatusDao: ClubStatusDao
) {
    fun getClubStatus(): Flow<ClubStatus?> = 
        clubStatusDao.getClubStatus().map { entity ->
            entity?.toDomain()
        }

    suspend fun saveClubStatus(clubStatus: ClubStatus) {
        clubStatusDao.insertClubStatus(clubStatus.toEntity())
    }

    suspend fun updateClubStatus(clubStatus: ClubStatus) {
        clubStatusDao.updateClubStatus(clubStatus.toEntity())
    }

    private fun ClubStatusEntity.toDomain(): ClubStatus {
        return ClubStatus(
            id = id,
            pcFree = pcFree,
            pcBusy = pcBusy,
            zoneAStatus = ZoneStatus.valueOf(zoneAStatus),
            zoneBStatus = ZoneStatus.valueOf(zoneBStatus),
            zoneCStatus = ZoneStatus.valueOf(zoneCStatus),
            estimatedWaitTimeMinutes = estimatedWaitTimeMinutes,
            lastUpdated = lastUpdated
        )
    }

    private fun ClubStatus.toEntity(): ClubStatusEntity {
        return ClubStatusEntity(
            id = id,
            pcFree = pcFree,
            pcBusy = pcBusy,
            zoneAStatus = zoneAStatus.name,
            zoneBStatus = zoneBStatus.name,
            zoneCStatus = zoneCStatus.name,
            estimatedWaitTimeMinutes = estimatedWaitTimeMinutes,
            lastUpdated = lastUpdated
        )
    }
}

