package com.application.ui.feature_reservation_qr

import androidx.lifecycle.viewModelScope
import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState
import com.application.data.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class ReservationDetails(
    val name: String,
    val phone: String,
    val zone: String,
    val seatNumber: String,
    val date: String,
    val time: String,
    val formattedDateTime: String
)

data class ReservationQRState(
    val reservationId: String = "",
    val qrCodeData: String = "",
    val reservation: ReservationDetails? = null,
    val isLoading: Boolean = false
) : UiState

sealed class ReservationQREvent : UiEvent {
    data class OnScreenShown(val reservationId: String) : ReservationQREvent()
    object OnBackToHomeClicked : ReservationQREvent()
    object OnBackClicked : ReservationQREvent()
}

sealed class ReservationQREffect : UiEffect {
    object NavigateToHome : ReservationQREffect()
    object NavigateBack : ReservationQREffect()
}

@HiltViewModel
class ReservationQRViewModel @Inject constructor(
    private val reservationRepository: ReservationRepository
) : MviViewModel<ReservationQREvent, ReservationQRState, ReservationQREffect>() {

    override fun createInitialState(): ReservationQRState = ReservationQRState()

    override fun handleEvent(event: ReservationQREvent) {
        when (event) {
            is ReservationQREvent.OnScreenShown -> {
                loadReservation(event.reservationId)
            }
            is ReservationQREvent.OnBackToHomeClicked -> {
                // Очищаем резервацию после выхода на Home
                clearReservation()
                setEffect { ReservationQREffect.NavigateToHome }
            }
            is ReservationQREvent.OnBackClicked -> {
                setEffect { ReservationQREffect.NavigateBack }
            }
        }
    }

    private fun loadReservation(reservationId: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            
            try {
                val reservation = reservationRepository.getReservationByReservationId(reservationId)
                
                if (reservation != null) {
                    // QR содержит ReservationID + дата
                    val qrCodeData = "$reservationId|${reservation.date}"
                    
                    // Форматируем дату и время для отображения
                    val formattedDateTime = formatDateTime(reservation.date, reservation.time)
                    
                    setState {
                        copy(
                            reservationId = reservationId,
                            qrCodeData = qrCodeData,
                            reservation = ReservationDetails(
                                name = reservation.name,
                                phone = reservation.phone,
                                zone = reservation.zone.name,
                                seatNumber = reservation.seatNumber,
                                date = reservation.date,
                                time = reservation.time,
                                formattedDateTime = formattedDateTime
                            ),
                            isLoading = false
                        )
                    }
                } else {
                    // Если резервация не найдена, используем текущую дату
                    val currentDate = SimpleDateFormat("EEE d", Locale.ENGLISH).format(Date())
                    val qrCodeData = "$reservationId|$currentDate"
                    setState { 
                        copy(
                            isLoading = false,
                            reservationId = reservationId,
                            qrCodeData = qrCodeData
                        ) 
                    }
                }
            } catch (e: Exception) {
                val currentDate = SimpleDateFormat("EEE d", Locale.ENGLISH).format(Date())
                val qrCodeData = "$reservationId|$currentDate"
                setState { 
                    copy(
                        isLoading = false,
                        reservationId = reservationId,
                        qrCodeData = qrCodeData
                    ) 
                }
            }
        }
    }
    
    private fun formatDateTime(dateStr: String, timeStr: String): String {
        return try {
            // Парсим дату "Fri 17" и время "13:00"
            val parts = dateStr.split(" ")
            val dayNumber = parts.getOrElse(1) { "" }
            
            // Находим дату в текущем месяце
            val calendar = Calendar.getInstance()
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
            
            // Ищем день в текущем или следующем месяце
            val targetDay = dayNumber.toIntOrNull() ?: currentDay
            calendar.set(Calendar.DAY_OF_MONTH, targetDay)
            calendar.set(Calendar.MONTH, currentMonth)
            calendar.set(Calendar.YEAR, currentYear)
            
            // Если день уже прошел, берем следующий месяц
            if (targetDay < currentDay) {
                calendar.add(Calendar.MONTH, 1)
            }
            
            // Форматируем дату: "October 15"
            val dateFormat = SimpleDateFormat("MMMM d", Locale.ENGLISH)
            val formattedDate = dateFormat.format(calendar.time)
            
            // Форматируем время: "3:00 PM"
            val timeParts = timeStr.split(":")
            val hour = timeParts[0].toIntOrNull() ?: 0
            val minute = timeParts.getOrElse(1) { "00" }
            
            val amPm = if (hour >= 12) "PM" else "AM"
            val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
            
            "$formattedDate, $displayHour:$minute $amPm"
        } catch (e: Exception) {
            "$dateStr, $timeStr"
        }
    }
    
    private fun clearReservation() {
        // Очистка резервации не требуется, так как она уже сохранена в БД
        // Форма резервации очищается в ReserveSeatViewModel при возврате
        // Здесь можно добавить дополнительную логику очистки, если необходимо
    }
}

