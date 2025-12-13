package com.application.domain.model

data class ReservationHistoryItem(
    val reservationId: String,
    val name: String,
    val date: String, // "Oct 10"
    val time: String, // "18:30"
    val zone: String, // "PC", "Console", "Table"
    val seatNumber: String,
    val status: ReservationStatus,
    val timestamp: Long
)

data class OrderHistoryItem(
    val orderId: String,
    val items: List<OrderItem>,
    val total: Double,
    val date: String, // "Oct 12"
    val time: String, // "17:40"
    val status: OrderStatus,
    val timestamp: Long
)

data class OrderItem(
    val title: String,
    val quantity: Int
)

enum class ReservationStatus {
    COMPLETED,
    CANCELLED
}

enum class OrderStatus {
    COMPLETED,
    CANCELLED
}

