package com.application.ui.feature_home

import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class HomeState(
    val isLoading: Boolean = false
) : UiState

sealed class HomeEvent : UiEvent {
    object OnGamingTimeClicked : HomeEvent()
    object OnCartClicked : HomeEvent()
    object OnMatchScheduleClicked : HomeEvent()
    object OnReserveSeatClicked : HomeEvent()
    object OnClubInfoClicked : HomeEvent()
    object OnSupportClicked : HomeEvent()
}

sealed class HomeEffect : UiEffect {
    data class NavigateToHomeWrapper(val screen: String) : HomeEffect()
    object NavigateToCart : HomeEffect()
}

@HiltViewModel
class HomeViewModel @Inject constructor() : MviViewModel<HomeEvent, HomeState, HomeEffect>() {
    override fun createInitialState(): HomeState = HomeState()

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnGamingTimeClicked -> {
                setEffect { HomeEffect.NavigateToHomeWrapper("gaming_time") }
            }
            is HomeEvent.OnCartClicked -> {
                setEffect { HomeEffect.NavigateToCart }
            }
            is HomeEvent.OnMatchScheduleClicked -> {
                setEffect { HomeEffect.NavigateToHomeWrapper("match_schedule") }
            }
            is HomeEvent.OnReserveSeatClicked -> {
                setEffect { HomeEffect.NavigateToHomeWrapper("reserve_seat") }
            }
            is HomeEvent.OnClubInfoClicked -> {
                setEffect { HomeEffect.NavigateToHomeWrapper("club_info") }
            }
            is HomeEvent.OnSupportClicked -> {
                setEffect { HomeEffect.NavigateToHomeWrapper("support") }
            }
        }
    }
}

