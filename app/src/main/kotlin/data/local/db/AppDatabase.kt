package com.application.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.application.data.local.db.dao.CartDao
import com.application.data.local.db.entity.CartItemEntity

@Database(
    entities = [CartItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}

