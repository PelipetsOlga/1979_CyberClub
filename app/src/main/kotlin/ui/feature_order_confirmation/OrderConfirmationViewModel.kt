package com.application.ui.feature_order_confirmation

import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState

data class OrderConfirmationState(
    val orderId: String = "",
    val qrCode: String = ""
) : UiState

sealed class OrderConfirmationEvent : UiEvent {
    object OnBackToHomeClicked : OrderConfirmationEvent()
    object OnBackClicked : OrderConfirmationEvent()
}

sealed class OrderConfirmationEffect : UiEffect {
    object NavigateToHome : OrderConfirmationEffect()
    object NavigateBack : OrderConfirmationEffect()
}

class OrderConfirmationViewModel : MviViewModel<OrderConfirmationEvent, OrderConfirmationState, OrderConfirmationEffect>() {
    override fun createInitialState(): OrderConfirmationState = OrderConfirmationState()

    override fun handleEvent(event: OrderConfirmationEvent) {
        when (event) {
            is OrderConfirmationEvent.OnBackToHomeClicked -> {
                setEffect { OrderConfirmationEffect.NavigateToHome }
            }
            is OrderConfirmationEvent.OnBackClicked -> {
                setEffect { OrderConfirmationEffect.NavigateBack }
            }
        }
    }
}

