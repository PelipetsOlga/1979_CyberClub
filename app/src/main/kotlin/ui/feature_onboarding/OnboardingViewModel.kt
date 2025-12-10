package com.application.ui.feature_onboarding

import androidx.lifecycle.viewModelScope
import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState
import com.application.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

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

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appRepository: AppRepository
) : MviViewModel<OnboardingEvent, OnboardingState, OnboardingEffect>() {
    override fun createInitialState(): OnboardingState = OnboardingState()

    override fun handleEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.OnGetStartedClicked -> {
                viewModelScope.launch {
                    // Save onboarding completed to DataStore via repository
                    appRepository.setFirstLaunchCompleted()
                    setEffect { OnboardingEffect.NavigateToHome }
                }
            }
            is OnboardingEvent.OnPageChanged -> {
                setState { copy(currentPage = event.page) }
            }
        }
    }
}

