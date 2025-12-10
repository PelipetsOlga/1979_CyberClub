package com.application.ui.feature_home_wrapper

import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState

data class ReserveSeatState(
    val isLoading: Boolean = false
) : UiState

sealed class ReserveSeatEvent : UiEvent {
    object OnReserveClicked : ReserveSeatEvent()
}

sealed class ReserveSeatEffect : UiEffect

class ReserveSeatViewModel : MviViewModel<ReserveSeatEvent, ReserveSeatState, ReserveSeatEffect>() {
    override fun createInitialState(): ReserveSeatState = ReserveSeatState()

    override fun handleEvent(event: ReserveSeatEvent) {
        when (event) {
            is ReserveSeatEvent.OnReserveClicked -> {
                // Handle reservation
            }
        }
    }
}

