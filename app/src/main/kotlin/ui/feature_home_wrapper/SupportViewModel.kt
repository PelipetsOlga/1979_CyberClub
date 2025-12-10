package com.application.ui.feature_home_wrapper

import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState

data class SupportState(
    val isLoading: Boolean = false
) : UiState

sealed class SupportEvent : UiEvent {
    object OnSendMessageClicked : SupportEvent()
}

sealed class SupportEffect : UiEffect

class SupportViewModel : MviViewModel<SupportEvent, SupportState, SupportEffect>() {
    override fun createInitialState(): SupportState = SupportState()

    override fun handleEvent(event: SupportEvent) {
        when (event) {
            is SupportEvent.OnSendMessageClicked -> {
                // Handle support message
            }
        }
    }
}

