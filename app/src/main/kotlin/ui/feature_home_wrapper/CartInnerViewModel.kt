package com.application.ui.feature_home_wrapper

import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState

data class CartInnerState(
    val items: List<String> = emptyList()
) : UiState

sealed class CartInnerEvent : UiEvent {
    object OnCheckoutClicked : CartInnerEvent()
}

sealed class CartInnerEffect : UiEffect {
    object NavigateToOrderConfirmation : CartInnerEffect()
}

class CartInnerViewModel : MviViewModel<CartInnerEvent, CartInnerState, CartInnerEffect>() {
    override fun createInitialState(): CartInnerState = CartInnerState()

    override fun handleEvent(event: CartInnerEvent) {
        when (event) {
            is CartInnerEvent.OnCheckoutClicked -> {
                setEffect { CartInnerEffect.NavigateToOrderConfirmation }
            }
        }
    }
}

