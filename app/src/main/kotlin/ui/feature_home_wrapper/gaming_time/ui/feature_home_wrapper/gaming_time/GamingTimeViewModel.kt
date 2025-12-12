package com.application.ui.feature_home_wrapper.gaming_time.ui.feature_home_wrapper.gaming_time

import androidx.lifecycle.viewModelScope
import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState
import com.application.data.repository.CartRepository
import com.application.domain.model.Item
import com.application.domain.model.ItemCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// Alias for UI layer compatibility
typealias GamingTimeItem = Item

data class GamingTimeState(
    val isLoading: Boolean = false,
    val selectedCategory: ItemCategory? = null,
    val cartItemCount: Int = 0,
    val items: List<GamingTimeItem> = getDefaultItems()
) : UiState {
    val filteredItems: List<GamingTimeItem>
        get() = if (selectedCategory == null) {
            items
        } else {
            items.filter { it.category == selectedCategory }
        }
}

private fun getDefaultItems(): List<GamingTimeItem> {
    return listOf(
        // PC Time
        Item(
            id = "pc_1h",
            title = "1 Hour PC",
            description = "Standard PC station",
            price = 3.0,
            category = ItemCategory.PC_TIME,
            iconResId = com.application.R.mipmap.ic_monitor
        ),
        Item(
            id = "pc_3h",
            title = "3 Hours PC",
            description = "Better price per hour",
            price = 7.0,
            category = ItemCategory.PC_TIME,
            iconResId = com.application.R.mipmap.ic_monitor
        ),
        // Console Time
        Item(
            id = "console_1h",
            title = "1 Hour Console",
            description = "PS5 / Xbox session",
            price = 4.0,
            category = ItemCategory.CONSOLE_TIME,
            iconResId = com.application.R.mipmap.ic_pult
        ),
        Item(
            id = "console_2h",
            title = "2 Hour Console",
            description = "Gaming bundle",
            price = 7.0,
            category = ItemCategory.CONSOLE_TIME,
            iconResId = com.application.R.mipmap.ic_pult
        ),
        // Drinks
        Item(
            id = "cola",
            title = "Cola",
            description = "Cold soft drink",
            price = 1.5,
            category = ItemCategory.DRINKS,
            iconResId = com.application.R.mipmap.ic_cola
        ),
        Item(
            id = "energy_drink",
            title = "Energy Drink",
            description = "Boost your game",
            price = 2.5,
            category = ItemCategory.DRINKS,
            iconResId = com.application.R.mipmap.ic_drink
        ),
        Item(
            id = "coffee",
            title = "Coffee",
            description = "Fresh brew",
            price = 2.0,
            category = ItemCategory.DRINKS,
            iconResId = com.application.R.mipmap.ic_coffe
        ),
        // Snacks
        Item(
            id = "chips",
            title = "Chips",
            description = "Crispy potato snack",
            price = 1.5,
            category = ItemCategory.SNACKS,
            iconResId = com.application.R.mipmap.ic_chips
        ),
        Item(
            id = "chocolate",
            title = "Chocolate Bar",
            description = "Sweet energy",
            price = 1.0,
            category = ItemCategory.SNACKS,
            iconResId = com.application.R.mipmap.ic_chocolate
        ),
        Item(
            id = "hotdog",
            title = "Hot-Dog",
            description = "Warm snack",
            price = 1.0,
            category = ItemCategory.SNACKS,
            iconResId = com.application.R.mipmap.ic_hot_dog
        )
    )
}

sealed class GamingTimeEvent : UiEvent {
    object OnCartIconClicked : GamingTimeEvent()
    data class OnCategorySelected(val category: ItemCategory?) : GamingTimeEvent()
    data class OnAddToCart(val item: GamingTimeItem) : GamingTimeEvent()
}

sealed class GamingTimeEffect : UiEffect {
    object NavigateToCart : GamingTimeEffect()
}

@HiltViewModel
class GamingTimeViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : MviViewModel<GamingTimeEvent, GamingTimeState, GamingTimeEffect>() {
    
    init {
        // Observe cart item count from database
        viewModelScope.launch {
            cartRepository.getCartItemCount().collect { count ->
                setState { copy(cartItemCount = count) }
            }
        }
    }
    
    override fun createInitialState(): GamingTimeState = GamingTimeState()

    override fun handleEvent(event: GamingTimeEvent) {
        when (event) {
            is GamingTimeEvent.OnCartIconClicked -> {
                setEffect { GamingTimeEffect.NavigateToCart }
            }
            is GamingTimeEvent.OnCategorySelected -> {
                setState { copy(selectedCategory = event.category) }
            }
            is GamingTimeEvent.OnAddToCart -> {
                viewModelScope.launch {
                    cartRepository.addItemToCart(event.item)
                    // Count will be updated automatically via Flow
                }
            }
        }
    }
}

