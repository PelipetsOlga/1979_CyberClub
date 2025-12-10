package com.application.ui.feature_splash

import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState

data class SplashState(
    val isLoading: Boolean = true
) : UiState

sealed class SplashEvent : UiEvent {
    object OnScreenShown : SplashEvent()
}

sealed class SplashEffect : UiEffect {
    data class NavigateToOnboarding(val onboardingCompleted: Boolean) : SplashEffect()
}

class SplashViewModel : MviViewModel<SplashEvent, SplashState, SplashEffect>() {
    override fun createInitialState(): SplashState = SplashState()

    override fun handleEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.OnScreenShown -> {
                // Simulate checking onboarding status
                // In real app, this would check SharedPreferences or DataStore
                val onboardingCompleted = false // TODO: Get from storage
                setEffect { SplashEffect.NavigateToOnboarding(onboardingCompleted) }
            }
        }
    }
}

