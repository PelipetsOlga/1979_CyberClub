package com.application.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.application.data.local.db.AppDatabase
import com.application.data.local.db.dao.CartDao
import com.application.data.local.db.dao.ClubStatusDao
import com.application.data.local.db.dao.ReservationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
        .fallbackToDestructiveMigration() // For development only
        .build()
    }

    @Provides
    fun provideCartDao(database: AppDatabase): CartDao {
        return database.cartDao()
    }

    @Provides
    fun provideReservationDao(database: AppDatabase): ReservationDao {
        return database.reservationDao()
    }

    @Provides
    fun provideClubStatusDao(database: AppDatabase): ClubStatusDao {
        return database.clubStatusDao()
    }

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS reservations (
                    id TEXT NOT NULL PRIMARY KEY,
                    reservationId TEXT NOT NULL,
                    name TEXT NOT NULL,
                    phone TEXT NOT NULL,
                    zone TEXT NOT NULL,
                    seatNumber TEXT NOT NULL,
                    date TEXT NOT NULL,
                    time TEXT NOT NULL,
                    timestamp INTEGER NOT NULL
                )
            """.trimIndent())
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS club_status (
                    id TEXT NOT NULL PRIMARY KEY,
                    pcFree INTEGER NOT NULL,
                    pcBusy INTEGER NOT NULL,
                    zoneAStatus TEXT NOT NULL,
                    zoneBStatus TEXT NOT NULL,
                    zoneCStatus TEXT NOT NULL,
                    estimatedWaitTimeMinutes INTEGER NOT NULL,
                    lastUpdated INTEGER NOT NULL
                )
            """.trimIndent())
        }
    }
}

