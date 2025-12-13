package com.application.ui.feature_reservation_qr

import androidx.lifecycle.viewModelScope
import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState
import com.application.data.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReservationDetails(
    val name: String,
    val phone: String,
    val zone: String,
    val seatNumber: String,
    val date: String,
    val time: String
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
                    val timestamp = System.currentTimeMillis()
                    val qrCodeData = "$reservationId|$timestamp"
                    
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
                                time = reservation.time
                            ),
                            isLoading = false
                        )
                    }
                } else {
                    setState { 
                        copy(
                            isLoading = false,
                            reservationId = reservationId,
                            qrCodeData = "$reservationId|${System.currentTimeMillis()}"
                        ) 
                    }
                }
            } catch (e: Exception) {
                setState { 
                    copy(
                        isLoading = false,
                        reservationId = reservationId,
                        qrCodeData = "$reservationId|${System.currentTimeMillis()}"
                    ) 
                }
            }
        }
    }
}

