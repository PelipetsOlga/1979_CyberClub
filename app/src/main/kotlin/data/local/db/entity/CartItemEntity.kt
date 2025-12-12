package com.application.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey
    val itemId: String,
    val title: String,
    val description: String,
    val price: Double,
    val category: String, // PC_TIME, CONSOLE_TIME, DRINKS, SNACKS
    val iconResId: Int, // Store resource ID
    val quantity: Int
)

