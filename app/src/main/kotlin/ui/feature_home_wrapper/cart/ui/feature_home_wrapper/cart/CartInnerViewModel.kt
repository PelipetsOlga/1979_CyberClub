package com.application.ui.feature_home_wrapper.cart.ui.feature_home_wrapper.cart

import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState
import com.application.ui.feature_home_wrapper.gaming_time.ui.feature_home_wrapper.gaming_time.GamingTimeItem

data class CartItem(
    val item: GamingTimeItem,
    val quantity: Int
) {
    val totalPrice: Double
        get() = item.price * quantity
}

data class CartInnerState(
    val items: List<CartItem> = getDefaultCartItems()
) : UiState {
    val totalPrice: Double
        get() = items.sumOf { it.totalPrice }
    
    val isEmpty: Boolean
        get() = items.isEmpty()
}

// Temporary default items for testing - in real app this would come from shared state/repository
private fun getDefaultCartItems(): List<CartItem> {
    return listOf(
        CartItem(
            item = GamingTimeItem(
                id = "pc_1h",
                title = "1 Hour PC",
                description = "Standard PC station",
                price = 3.0,
                category = com.application.ui.feature_home_wrapper.gaming_time.ui.feature_home_wrapper.gaming_time.ItemCategory.PC_TIME,
                iconRes = com.application.R.mipmap.ic_monitor
            ),
            quantity = 1
        ),
        CartItem(
            item = GamingTimeItem(
                id = "cola",
                title = "Cola",
                description = "Cold soft drink",
                price = 1.5,
                category = com.application.ui.feature_home_wrapper.gaming_time.ui.feature_home_wrapper.gaming_time.ItemCategory.DRINKS,
                iconRes = com.application.R.mipmap.ic_cola
            ),
            quantity = 1
        )
    )
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

class CartInnerViewModel : MviViewModel<CartInnerEvent, CartInnerState, CartInnerEffect>() {
    override fun createInitialState(): CartInnerState = CartInnerState()

    override fun handleEvent(event: CartInnerEvent) {
        when (event) {
            is CartInnerEvent.OnIncreaseQuantity -> {
                setState {
                    copy(
                        items = items.map { cartItem ->
                            if (cartItem.item.id == event.itemId) {
                                cartItem.copy(quantity = cartItem.quantity + 1)
                            } else {
                                cartItem
                            }
                        }
                    )
                }
            }
            is CartInnerEvent.OnDecreaseQuantity -> {
                setState {
                    val updatedItems = items.map { cartItem ->
                        if (cartItem.item.id == event.itemId) {
                            val newQuantity = (cartItem.quantity - 1).coerceAtLeast(0)
                            if (newQuantity > 0) {
                                cartItem.copy(quantity = newQuantity)
                            } else {
                                null
                            }
                        } else {
                            cartItem
                        }
                    }.filterNotNull()
                    copy(items = updatedItems)
                }
            }
            is CartInnerEvent.OnRemoveItem -> {
                setState {
                    copy(
                        items = items.filter { it.item.id != event.itemId }
                    )
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

