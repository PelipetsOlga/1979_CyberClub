package com.application.ui.feature_home_wrapper.cart.ui.feature_home_wrapper.cart

import androidx.lifecycle.viewModelScope
import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState
import com.application.data.repository.CartRepository
import com.application.ui.feature_home_wrapper.gaming_time.ui.feature_home_wrapper.gaming_time.GamingTimeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartItem(
    val item: GamingTimeItem,
    val quantity: Int
) {
    val totalPrice: Double
        get() = item.price * quantity
}

data class CartInnerState(
    val items: List<CartItem> = emptyList(),
    val isLoading: Boolean = false
) : UiState {
    val totalPrice: Double
        get() = items.sumOf { it.totalPrice }
    
    val isEmpty: Boolean
        get() = items.isEmpty()
}

sealed class CartInnerEvent : UiEvent {
    data class OnIncreaseQuantity(val itemId: String) : CartInnerEvent()
    data class OnDecreaseQuantity(val itemId: String) : CartInnerEvent()
    data class OnRemoveItem(val itemId: String) : CartInnerEvent()
    object OnConfirmOrder : CartInnerEvent()
}

sealed class CartInnerEffect : UiEffect {
    object NavigateToOrderConfirmation : CartInnerEffect()
}

@HiltViewModel
class CartInnerViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : MviViewModel<CartInnerEvent, CartInnerState, CartInnerEffect>() {
    
    init {
        // Load cart items from database
        viewModelScope.launch {
            cartRepository.getAllCartItems()
                .map { entities ->
                    entities.map { entity ->
                        CartItem(
                            item = cartRepository.toItem(entity) as GamingTimeItem,
                            quantity = entity.quantity
                        )
                    }
                }
                .collect { cartItems ->
                    setState { copy(items = cartItems) }
                }
        }
    }
    
    override fun createInitialState(): CartInnerState = CartInnerState()

    override fun handleEvent(event: CartInnerEvent) {
        when (event) {
            is CartInnerEvent.OnIncreaseQuantity -> {
                viewModelScope.launch {
                    val currentItem = viewState.value.items.find { it.item.id == event.itemId }
                    if (currentItem != null) {
                        cartRepository.updateItemQuantity(event.itemId, currentItem.quantity + 1)
                    }
                }
            }
            is CartInnerEvent.OnDecreaseQuantity -> {
                viewModelScope.launch {
                    val currentItem = viewState.value.items.find { it.item.id == event.itemId }
                    if (currentItem != null) {
                        val newQuantity = (currentItem.quantity - 1).coerceAtLeast(0)
                        cartRepository.updateItemQuantity(event.itemId, newQuantity)
                    }
                }
            }
            is CartInnerEvent.OnRemoveItem -> {
                viewModelScope.launch {
                    cartRepository.removeItemFromCart(event.itemId)
                }
            }
            is CartInnerEvent.OnConfirmOrder -> {
                if (viewState.value.items.isNotEmpty()) {
                    setEffect { CartInnerEffect.NavigateToOrderConfirmation }
                }
            }
        }
    }
}

