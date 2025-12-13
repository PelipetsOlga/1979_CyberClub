package com.application.ui.feature_home_wrapper.history

import androidx.lifecycle.viewModelScope
import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState
import com.application.data.repository.ReservationRepository
import com.application.domain.model.ReservationHistoryItem
import com.application.domain.model.ReservationStatus
import com.application.domain.model.OrderHistoryItem
import com.application.domain.model.OrderStatus
import com.application.domain.model.OrderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class HistoryState(
    val selectedTab: HistoryTab = HistoryTab.RESERVATIONS,
    val reservations: List<ReservationHistoryItem> = emptyList(),
    val orders: List<OrderHistoryItem> = emptyList(),
    val isLoading: Boolean = false
) : UiState

enum class HistoryTab {
    RESERVATIONS,
    ORDERS
}

sealed class HistoryEvent : UiEvent {
    object OnScreenShown : HistoryEvent()
    data class OnTabSelected(val tab: HistoryTab) : HistoryEvent()
    object OnBackClicked : HistoryEvent()
}

sealed class HistoryEffect : UiEffect {
    object NavigateBack : HistoryEffect()
}

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val reservationRepository: ReservationRepository
) : MviViewModel<HistoryEvent, HistoryState, HistoryEffect>() {

    override fun createInitialState(): HistoryState = HistoryState()

    override fun handleEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.OnScreenShown -> {
                loadHistory()
            }
            is HistoryEvent.OnTabSelected -> {
                setState { copy(selectedTab = event.tab) }
            }
            is HistoryEvent.OnBackClicked -> {
                setEffect { HistoryEffect.NavigateBack }
            }
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            try {
                // Load reservations in a separate coroutine
                launch {
                    reservationRepository.getAllReservations()
                        .map { reservations ->
                            reservations.map { reservation ->
                                ReservationHistoryItem(
                                    reservationId = reservation.reservationId,
                                    name = reservation.name,
                                    date = formatDate(reservation.date),
                                    time = reservation.time,
                                    zone = reservation.zone.name,
                                    seatNumber = reservation.seatNumber,
                                    status = ReservationStatus.COMPLETED, // Default to completed
                                    timestamp = reservation.timestamp
                                )
                            }
                        }
                        .collect { reservationHistory ->
                            setState { currentState ->
                                currentState.copy(
                                    reservations = reservationHistory
                                )
                            }
                        }
                }

                // Load orders (hardcoded for now, can be extended later)
                val mockOrders = getMockOrders()
                setState { currentState ->
                    currentState.copy(
                        orders = mockOrders,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                setState { copy(isLoading = false) }
            }
        }
    }

    private fun formatDate(dateString: String): String {
        // Convert "Fri 17" to "Oct 17" format
        return try {
            val inputFormat = SimpleDateFormat("EEE d", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("MMM d", Locale.ENGLISH)
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val parsedDate = inputFormat.parse("$dateString $currentYear")
            calendar.time = parsedDate
            outputFormat.format(calendar.time)
        } catch (e: Exception) {
            dateString
        }
    }

    private fun getMockOrders(): List<OrderHistoryItem> {
        // Mock data for orders - can be replaced with actual DB queries later
        return listOf(
            OrderHistoryItem(
                orderId = "A12839",
                items = listOf(
                    OrderItem("3 Hours PC", 1),
                    OrderItem("Cola", 2)
                ),
                total = 9.0,
                date = "Oct 12",
                time = "17:40",
                status = OrderStatus.COMPLETED,
                timestamp = System.currentTimeMillis() - 86400000 // 1 day ago
            )
        )
    }
}

