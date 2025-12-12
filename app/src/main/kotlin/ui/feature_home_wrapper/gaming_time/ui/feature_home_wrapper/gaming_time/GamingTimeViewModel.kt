package com.application.ui.feature_home_wrapper.gaming_time.ui.feature_home_wrapper.gaming_time

import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState

data class GamingTimeItem(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val category: ItemCategory,
    val iconRes: Int
)

enum class ItemCategory {
    PC_TIME,
    CONSOLE_TIME,
    DRINKS,
    SNACKS
}

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
        GamingTimeItem(
            id = "pc_1h",
            title = "1 Hour PC",
            description = "Standard PC station",
            price = 3.0,
            category = ItemCategory.PC_TIME,
            iconRes = com.application.R.mipmap.ic_monitor
        ),
        GamingTimeItem(
            id = "pc_3h",
            title = "3 Hours PC",
            description = "Better price per hour",
            price = 7.0,
            category = ItemCategory.PC_TIME,
            iconRes = com.application.R.mipmap.ic_monitor
        ),
        // Console Time
        GamingTimeItem(
            id = "console_1h",
            title = "1 Hour Console",
            description = "PS5 / Xbox session",
            price = 4.0,
            category = ItemCategory.CONSOLE_TIME,
            iconRes = com.application.R.mipmap.ic_pult
        ),
        GamingTimeItem(
            id = "console_2h",
            title = "2 Hour Console",
            description = "Gaming bundle",
            price = 7.0,
            category = ItemCategory.CONSOLE_TIME,
            iconRes = com.application.R.mipmap.ic_pult
        ),
        // Drinks
        GamingTimeItem(
            id = "cola",
            title = "Cola",
            description = "Cold soft drink",
            price = 1.5,
            category = ItemCategory.DRINKS,
            iconRes = com.application.R.mipmap.ic_cola
        ),
        GamingTimeItem(
            id = "energy_drink",
            title = "Energy Drink",
            description = "Boost your game",
            price = 2.5,
            category = ItemCategory.DRINKS,
            iconRes = com.application.R.mipmap.ic_drink
        ),
        GamingTimeItem(
            id = "coffee",
            title = "Coffee",
            description = "Fresh brew",
            price = 2.0,
            category = ItemCategory.DRINKS,
            iconRes = com.application.R.mipmap.ic_coffe
        ),
        // Snacks
        GamingTimeItem(
            id = "chips",
            title = "Chips",
            description = "Crispy potato snack",
            price = 1.5,
            category = ItemCategory.SNACKS,
            iconRes = com.application.R.mipmap.ic_chips
        ),
        GamingTimeItem(
            id = "chocolate",
            title = "Chocolate Bar",
            description = "Sweet energy",
            price = 1.0,
            category = ItemCategory.SNACKS,
            iconRes = com.application.R.mipmap.ic_chocolate
        ),
        GamingTimeItem(
            id = "hotdog",
            title = "Hot-Dog",
            description = "Warm snack",
            price = 1.0,
            category = ItemCategory.SNACKS,
            iconRes = com.application.R.mipmap.ic_hot_dog
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

class GamingTimeViewModel : MviViewModel<GamingTimeEvent, GamingTimeState, GamingTimeEffect>() {
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
                setState { copy(cartItemCount = cartItemCount + 1) }
            }
        }
    }
}

