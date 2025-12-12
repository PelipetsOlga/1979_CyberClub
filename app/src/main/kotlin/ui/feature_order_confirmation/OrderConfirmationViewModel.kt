package com.application.ui.feature_order_confirmation

import androidx.lifecycle.viewModelScope
import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState
import com.application.data.repository.CartRepository
import com.application.domain.model.Item
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderItem(
    val item: Item,
    val quantity: Int
)

data class OrderConfirmationState(
    val orderId: String = "",
    val qrCodeData: String = "",
    val items: List<OrderItem> = emptyList(),
    val isLoading: Boolean = false
) : UiState

sealed class OrderConfirmationEvent : UiEvent {
    object OnBackToHomeClicked : OrderConfirmationEvent()
    object OnBackClicked : OrderConfirmationEvent()
    object OnScreenShown : OrderConfirmationEvent()
}

sealed class OrderConfirmationEffect : UiEffect {
    object NavigateToHome : OrderConfirmationEffect()
    object NavigateBack : OrderConfirmationEffect()
}

@HiltViewModel
class OrderConfirmationViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : MviViewModel<OrderConfirmationEvent, OrderConfirmationState, OrderConfirmationEffect>() {
    
    init {
        // Generate order when ViewModel is created
        viewModelScope.launch {
            generateOrder()
        }
    }
    
    private suspend fun generateOrder() {
        setState { copy(isLoading = true) }
        
        // Get cart items
        val cartItems = cartRepository.getAllCartItems().first()
        val orderItems = cartItems.map { entity ->
            OrderItem(
                item = cartRepository.toItem(entity),
                quantity = entity.quantity
            )
        }
        
        // Generate OrderID (format: A + 5 random digits)
        val randomDigits = (10000..99999).random()
        val orderId = "A$randomDigits"
        
        // Generate QR code data: OrderID + Timestamp
        val timestamp = System.currentTimeMillis()
        val qrCodeData = "$orderId|$timestamp"
        
        setState {
            copy(
                orderId = orderId,
                qrCodeData = qrCodeData,
                items = orderItems,
                isLoading = false
            )
        }
        
        // Clear cart after showing QR
        cartRepository.clearCart()
    }
    
    override fun createInitialState(): OrderConfirmationState = OrderConfirmationState()

    override fun handleEvent(event: OrderConfirmationEvent) {
        when (event) {
            is OrderConfirmationEvent.OnBackToHomeClicked -> {
                setEffect { OrderConfirmationEffect.NavigateToHome }
            }
            is OrderConfirmationEvent.OnBackClicked -> {
                setEffect { OrderConfirmationEffect.NavigateBack }
            }
            is OrderConfirmationEvent.OnScreenShown -> {
                // Order is already generated in init
            }
        }
    }
}

