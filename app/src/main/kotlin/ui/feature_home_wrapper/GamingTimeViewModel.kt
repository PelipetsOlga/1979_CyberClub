package com.application.ui.feature_home_wrapper

import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState

data class GamingTimeState(
    val isLoading: Boolean = false
) : UiState

sealed class GamingTimeEvent : UiEvent {
    object OnCartIconClicked : GamingTimeEvent()
}

sealed class GamingTimeEffect : UiEffect {
    object NavigateToCart : GamingTimeEffect()
}

class GamingTimeViewModel : MviViewModel<GamingTimeEvent, GamingTimeState, GamingTimeEffect>() {
    override fun createInitialState(): GamingTimeState = GamingTimeState()

    override fun handleEvent(event: GamingTimeEvent) {
        when (event) {
            is GamingTimeEvent.OnCartIconClicked -> {
                setEffect { GamingTimeEffect.NavigateToCart }
            }
        }
    }
}

