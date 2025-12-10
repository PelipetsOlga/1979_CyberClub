package com.application.ui.feature_cart

import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState

data class CartState(
    val items: List<String> = emptyList(),
    val totalPrice: Double = 0.0
) : UiState

sealed class CartEvent : UiEvent {
    object OnCheckoutClicked : CartEvent()
    object OnBackClicked : CartEvent()
}

sealed class CartEffect : UiEffect {
    object NavigateToOrderConfirmation : CartEffect()
    object NavigateBack : CartEffect()
}

class CartViewModel : MviViewModel<CartEvent, CartState, CartEffect>() {
    override fun createInitialState(): CartState = CartState()

    override fun handleEvent(event: CartEvent) {
        when (event) {
            is CartEvent.OnCheckoutClicked -> {
                setEffect { CartEffect.NavigateToOrderConfirmation }
            }
            is CartEvent.OnBackClicked -> {
                setEffect { CartEffect.NavigateBack }
            }
        }
    }
}

