package com.application.domain.model

data class ClubStatus(
    val id: String = "club_status",
    val pcFree: Int = 0,
    val pcBusy: Int = 0,
    val zoneAStatus: ZoneStatus = ZoneStatus.FREE,
    val zoneBStatus: ZoneStatus = ZoneStatus.FREE,
    val zoneCStatus: ZoneStatus = ZoneStatus.FREE,
    val estimatedWaitTimeMinutes: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)

enum class ZoneStatus {
    FREE,
    BUSY
}

