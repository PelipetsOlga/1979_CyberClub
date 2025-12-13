package com.application.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.application.data.local.db.dao.CartDao
import com.application.data.local.db.dao.ReservationDao
import com.application.data.local.db.entity.CartItemEntity
import com.application.data.local.db.entity.ReservationEntity

@Database(
    entities = [CartItemEntity::class, ReservationEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun reservationDao(): ReservationDao
}

