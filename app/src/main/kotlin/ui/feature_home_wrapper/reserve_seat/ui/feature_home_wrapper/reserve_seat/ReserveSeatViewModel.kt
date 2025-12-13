package com.application.ui.feature_home_wrapper.reserve_seat.ui.feature_home_wrapper.reserve_seat

import androidx.lifecycle.viewModelScope
import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState
import com.application.data.repository.ReservationRepository
import com.application.domain.model.Reservation
import com.application.domain.model.Zone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class ReserveSeatState(
    val name: String = "",
    val phone: String = "",
    val selectedZone: Zone? = null,
    val selectedSeatNumber: String = "",
    val selectedDate: String = "",
    val selectedTime: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : UiState {
    val isFormValid: Boolean
        get() = name.isNotBlank() && 
                phone.isNotBlank() && 
                selectedZone != null && 
                selectedSeatNumber.isNotBlank() && 
                selectedDate.isNotBlank() && 
                selectedTime.isNotBlank()
    
    val availableSeats: List<String>
        get() = when (selectedZone) {
            Zone.PC -> (1..20).map { "PC-%02d".format(it) }
            Zone.CONSOLE -> (1..10).map { "Console-%02d".format(it) }
            Zone.TABLE -> (1..15).map { "Table-%02d".format(it) }
            null -> emptyList()
        }
    
    val availableDates: List<String> = generateDates()
    val availableTimes: List<String> = listOf(
        "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", 
        "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00"
    )
}

private fun generateDates(): List<String> {
    val dates = mutableListOf<String>()
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("EEE d", Locale.ENGLISH)
    val dayFormat = SimpleDateFormat("EEE", Locale.ENGLISH)
    val dayNumberFormat = SimpleDateFormat("d", Locale.ENGLISH)
    
    for (i in 0..13) {
        val dayName = dayFormat.format(calendar.time)
        val dayNumber = dayNumberFormat.format(calendar.time)
        dates.add("$dayName $dayNumber")
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
    return dates
}

sealed class ReserveSeatEvent : UiEvent {
    data class OnNameChanged(val name: String) : ReserveSeatEvent()
    data class OnPhoneChanged(val phone: String) : ReserveSeatEvent()
    data class OnZoneSelected(val zone: Zone) : ReserveSeatEvent()
    data class OnSeatNumberSelected(val seatNumber: String) : ReserveSeatEvent()
    data class OnDateSelected(val date: String) : ReserveSeatEvent()
    data class OnTimeSelected(val time: String) : ReserveSeatEvent()
    object OnConfirmReservation : ReserveSeatEvent()
    object OnClearForm : ReserveSeatEvent()
}

sealed class ReserveSeatEffect : UiEffect {
    data class NavigateToReservationQR(val reservationId: String) : ReserveSeatEffect()
}

@HiltViewModel
class ReserveSeatViewModel @Inject constructor(
    private val reservationRepository: ReservationRepository
) : MviViewModel<ReserveSeatEvent, ReserveSeatState, ReserveSeatEffect>() {

    override fun createInitialState(): ReserveSeatState = ReserveSeatState()

    override fun handleEvent(event: ReserveSeatEvent) {
        when (event) {
            is ReserveSeatEvent.OnNameChanged -> {
                setState { copy(name = event.name) }
            }
            is ReserveSeatEvent.OnPhoneChanged -> {
                setState { copy(phone = event.phone) }
            }
            is ReserveSeatEvent.OnZoneSelected -> {
                setState { 
                    copy(
                        selectedZone = event.zone,
                        selectedSeatNumber = "" // Reset seat when zone changes
                    ) 
                }
            }
            is ReserveSeatEvent.OnSeatNumberSelected -> {
                setState { copy(selectedSeatNumber = event.seatNumber) }
            }
            is ReserveSeatEvent.OnDateSelected -> {
                setState { copy(selectedDate = event.date) }
            }
            is ReserveSeatEvent.OnTimeSelected -> {
                setState { copy(selectedTime = event.time) }
            }
            is ReserveSeatEvent.OnConfirmReservation -> {
                if (viewState.value.isFormValid) {
                    confirmReservation()
                }
            }
            is ReserveSeatEvent.OnClearForm -> {
                setState { createInitialState() }
            }
        }
    }

    private fun confirmReservation() {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            
            try {
                val state = viewState.value
                val reservationId = generateReservationId()
                val id = UUID.randomUUID().toString()
                
                val reservation = Reservation(
                    id = id,
                    reservationId = reservationId,
                    name = state.name,
                    phone = state.phone,
                    zone = state.selectedZone!!,
                    seatNumber = state.selectedSeatNumber,
                    date = state.selectedDate,
                    time = state.selectedTime,
                    timestamp = System.currentTimeMillis()
                )
                
                reservationRepository.saveReservation(reservation)
                
                // Очищаем форму после успешного сохранения
                setState { 
                    createInitialState().copy(isLoading = false)
                }
                setEffect { ReserveSeatEffect.NavigateToReservationQR(reservationId) }
            } catch (e: Exception) {
                setState { 
                    copy(
                        isLoading = false,
                        errorMessage = "Failed to create reservation. Please try again."
                    ) 
                }
            }
        }
    }

    private fun generateReservationId(): String {
        val randomDigits = (10000..99999).random()
        return "R$randomDigits"
    }
}
