package com.application.ui.feature_onboarding

import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState

data class OnboardingState(
    val currentPage: Int = 0,
    val totalPages: Int = 4
) : UiState

sealed class OnboardingEvent : UiEvent {
    object OnGetStartedClicked : OnboardingEvent()
    data class OnPageChanged(val page: Int) : OnboardingEvent()
}

sealed class OnboardingEffect : UiEffect {
    object NavigateToHome : OnboardingEffect()
}

class OnboardingViewModel : MviViewModel<OnboardingEvent, OnboardingState, OnboardingEffect>() {
    override fun createInitialState(): OnboardingState = OnboardingState()

    override fun handleEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.OnGetStartedClicked -> {
                // TODO: Save onboardingCompleted = true to storage
                setEffect { OnboardingEffect.NavigateToHome }
            }
            is OnboardingEvent.OnPageChanged -> {
                setState { copy(currentPage = event.page) }
            }
        }
    }
}

