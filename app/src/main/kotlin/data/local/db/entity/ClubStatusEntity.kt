package com.application.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "club_status")
data class ClubStatusEntity(
    @PrimaryKey
    val id: String = "club_status",
    val pcFree: Int = 0,
    val pcBusy: Int = 0,
    val zoneAStatus: String = "FREE", // FREE, BUSY
    val zoneBStatus: String = "FREE",
    val zoneCStatus: String = "FREE",
    val estimatedWaitTimeMinutes: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)

