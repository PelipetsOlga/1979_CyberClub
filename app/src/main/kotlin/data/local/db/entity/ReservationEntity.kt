package com.application.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey
    val id: String,
    val reservationId: String,
    val name: String,
    val phone: String,
    val zone: String, // PC, CONSOLE, TABLE
    val seatNumber: String,
    val date: String,
    val time: String,
    val timestamp: Long
)

