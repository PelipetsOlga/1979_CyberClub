package com.application.data.local.db.dao

import androidx.room.*
import com.application.data.local.db.entity.ClubStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClubStatusDao {
    @Query("SELECT * FROM club_status WHERE id = :id LIMIT 1")
    fun getClubStatus(id: String = "club_status"): Flow<ClubStatusEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClubStatus(clubStatus: ClubStatusEntity)

    @Update
    suspend fun updateClubStatus(clubStatus: ClubStatusEntity)

    @Query("DELETE FROM club_status WHERE id = :id")
    suspend fun deleteClubStatus(id: String = "club_status")
}

