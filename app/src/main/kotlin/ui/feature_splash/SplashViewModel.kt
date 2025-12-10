package com.application.ui.feature_splash

import androidx.lifecycle.viewModelScope
import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState
import com.application.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SplashState(
    val isLoading: Boolean = true
) : UiState

sealed class SplashEvent : UiEvent {
    object CheckFirstLaunch : SplashEvent()
}

sealed class SplashEffect : UiEffect {
    object NavigateToOnboarding : SplashEffect()
    object NavigateToHome : SplashEffect()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appRepository: AppRepository
) : MviViewModel<SplashEvent, SplashState, SplashEffect>() {
    override fun createInitialState(): SplashState = SplashState()

    override fun handleEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.CheckFirstLaunch -> {
                checkFirstLaunch()
            }
        }
    }

    fun checkFirstLaunch() {
        viewModelScope.launch {
            // Wait 1.5 seconds
            delay(1500)
            
            // Check if onboarding was completed via DataStore
            val isFirstLaunch = appRepository.isFirstLaunch()
            
            if (isFirstLaunch) {
                setEffect { SplashEffect.NavigateToOnboarding }
            } else {
                setEffect { SplashEffect.NavigateToHome }
            }
        }
    }
}

