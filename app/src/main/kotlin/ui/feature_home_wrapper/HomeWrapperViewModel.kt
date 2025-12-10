package com.application.ui.feature_home_wrapper

import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class HomeWrapperState(
    val isDrawerOpen: Boolean = false
) : UiState

sealed class HomeWrapperEvent : UiEvent {
    object OnDrawerOpen : HomeWrapperEvent()
    object OnDrawerClose : HomeWrapperEvent()
}

sealed class HomeWrapperEffect : UiEffect

@HiltViewModel
class HomeWrapperViewModel @Inject constructor() : MviViewModel<HomeWrapperEvent, HomeWrapperState, HomeWrapperEffect>() {
    override fun createInitialState(): HomeWrapperState = HomeWrapperState()

    override fun handleEvent(event: HomeWrapperEvent) {
        when (event) {
            is HomeWrapperEvent.OnDrawerOpen -> {
                setState { copy(isDrawerOpen = true) }
            }
            is HomeWrapperEvent.OnDrawerClose -> {
                setState { copy(isDrawerOpen = false) }
            }
        }
    }
}

