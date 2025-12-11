package com.application.ui.feature_home_wrapper.club_info.ui.feature_home_wrapper.club_info

import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState

data class ClubInfoState(
    val info: String = ""
) : UiState

sealed class ClubInfoEvent : UiEvent {
    object OnScreenShown : ClubInfoEvent()
}

sealed class ClubInfoEffect : UiEffect

class ClubInfoViewModel : MviViewModel<ClubInfoEvent, ClubInfoState, ClubInfoEffect>() {
    override fun createInitialState(): ClubInfoState = ClubInfoState()

    override fun handleEvent(event: ClubInfoEvent) {
        when (event) {
            is ClubInfoEvent.OnScreenShown -> {
                // Load club info
            }
        }
    }
}

