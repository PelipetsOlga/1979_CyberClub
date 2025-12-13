package com.application.domain.model

enum class Zone {
    PC,
    CONSOLE,
    TABLE
}

data class Reservation(
    val id: String,
    val reservationId: String, // Format: R + 5 digits
    val name: String,
    val phone: String,
    val zone: Zone,
    val seatNumber: String,
    val date: String, // Format: "Fri 17"
    val time: String, // Format: "13:00"
    val timestamp: Long
)

