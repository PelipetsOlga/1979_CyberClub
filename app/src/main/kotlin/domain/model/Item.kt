package com.application.domain.model

data class Item(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val category: ItemCategory,
    val iconResId: Int
)

enum class ItemCategory {
    PC_TIME,
    CONSOLE_TIME,
    DRINKS,
    SNACKS
}

