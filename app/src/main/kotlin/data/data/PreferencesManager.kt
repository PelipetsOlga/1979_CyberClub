package com.application.ui.feature_splash

import androidx.lifecycle.viewModelScope
import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState
import com.application.com.application.domain.com.application.domain.AppRepository
import com.application.domain.repository.AppRepository
import com.application.ui.base.MviViewModel
import com.application.ui.base.UiEffect
import com.application.ui.base.UiEvent
import com.application.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// Events
sealed class SplashEvent : UiEvent {
    object CheckFirstLaunch : SplashEvent()
}

// State
data class SplashState(
    val isLoading: Boolean = true
) : UiState

// Effects
sealed class SplashEffect : UiEffect {
    object NavigateToOnboarding : SplashEffect()
    object NavigateToHome : SplashEffect()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appRepository: AppRepository
) : MviViewModel<SplashEvent, SplashState, SplashEffect>() {

    override fun createInitialState(): SplashState {
        return SplashState()
    }

    override fun handleEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.CheckFirstLaunch -> {
                // This will be handled in checkFirstLaunch()
            }
        }
    }

    fun checkFirstLaunch() {
        viewModelScope.launch {
            // Wait 2 seconds
            delay(2000)

            val isFirstLaunch = appRepository.isFirstLaunch()

            if (isFirstLaunch) {
                // Mark as not first launch anymore
                appRepository.setFirstLaunchCompleted()
                setEffect { SplashEffect.NavigateToOnboarding }
            } else {
                setEffect { SplashEffect.NavigateToHome }
            }
        }
    }
}

