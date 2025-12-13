package com.application.data.repository

import com.application.data.local.db.dao.ReservationDao
import com.application.data.local.db.entity.ReservationEntity
import com.application.domain.model.Reservation
import com.application.domain.model.Zone
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationRepository @Inject constructor(
    private val reservationDao: ReservationDao
) {
    fun getAllReservations(): Flow<List<Reservation>> = 
        reservationDao.getAllReservations().map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun getReservationById(id: String): Reservation? {
        return reservationDao.getReservationById(id)?.toDomain()
    }

    suspend fun getReservationByReservationId(reservationId: String): Reservation? {
        return reservationDao.getReservationByReservationId(reservationId)?.toDomain()
    }

    suspend fun saveReservation(reservation: Reservation) {
        reservationDao.insertReservation(reservation.toEntity())
    }

    suspend fun deleteReservation(id: String) {
        reservationDao.deleteReservationById(id)
    }

    private fun ReservationEntity.toDomain(): Reservation {
        return Reservation(
            id = id,
            reservationId = reservationId,
            name = name,
            phone = phone,
            zone = Zone.valueOf(zone),
            seatNumber = seatNumber,
            date = date,
            time = time,
            timestamp = timestamp
        )
    }

    private fun Reservation.toEntity(): ReservationEntity {
        return ReservationEntity(
            id = id,
            reservationId = reservationId,
            name = name,
            phone = phone,
            zone = zone.name,
            seatNumber = seatNumber,
            date = date,
            time = time,
            timestamp = timestamp
        )
    }
}

