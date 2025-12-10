package com.application.ui.feature_home_wrapper

import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState

data class MatchScheduleState(
    val matches: List<String> = emptyList()
) : UiState

sealed class MatchScheduleEvent : UiEvent {
    object OnScreenShown : MatchScheduleEvent()
}

sealed class MatchScheduleEffect : UiEffect

class MatchScheduleViewModel : MviViewModel<MatchScheduleEvent, MatchScheduleState, MatchScheduleEffect>() {
    override fun createInitialState(): MatchScheduleState = MatchScheduleState()

    override fun handleEvent(event: MatchScheduleEvent) {
        when (event) {
            is MatchScheduleEvent.OnScreenShown -> {
                // Load matches
            }
        }
    }
}

