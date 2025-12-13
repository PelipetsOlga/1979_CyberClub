package com.application.ui.feature_home_wrapper.match_schedule

import com.application.MviViewModel
import com.application.UiEffect
import com.application.UiEvent
import com.application.UiState
import com.application.domain.model.Match
import com.application.domain.model.Team
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class MatchScheduleState(
    val isLoading: Boolean = false,
    val matches: List<Match> = getHardcodedMatches(),
    val errorMessage: String? = null
) : UiState {
    val hasError: Boolean
        get() = errorMessage != null
}

private fun getHardcodedMatches(): List<Match> {
    return listOf(
        Match(
            id = "match_1",
            matchTitle = "CS2",
            dateTime = "Oct 12, 18:00",
            teamA = Team(
                id = "navi",
                name = "NAVI",
                logoResId = null // Will use placeholder
            ),
            teamB = Team(
                id = "g2",
                name = "G2",
                logoResId = null // Will use placeholder
            ),
            tournamentName = "BLAST Premier Fall",
            stage = "Group Stage"
        ),
        Match(
            id = "match_2",
            matchTitle = "Valorant",
            dateTime = "Oct 12, 18:00",
            teamA = Team(
                id = "fnatic",
                name = "Fnatic",
                logoResId = null // Will use placeholder
            ),
            teamB = Team(
                id = "liquid",
                name = "Liquid",
                logoResId = null // Will use placeholder
            ),
            tournamentName = "BLAST Premier Fall",
            stage = "Playoffs"
        )
    )
}

sealed class MatchScheduleEvent : UiEvent {
    object OnScreenShown : MatchScheduleEvent()
    object OnRetry : MatchScheduleEvent()
}

sealed class MatchScheduleEffect : UiEffect

@HiltViewModel
class MatchScheduleViewModel @Inject constructor() : MviViewModel<MatchScheduleEvent, MatchScheduleState, MatchScheduleEffect>() {

    override fun createInitialState(): MatchScheduleState = MatchScheduleState()

    override fun handleEvent(event: MatchScheduleEvent) {
        when (event) {
            is MatchScheduleEvent.OnScreenShown -> {
                // Data is already loaded in initial state
            }
            is MatchScheduleEvent.OnRetry -> {
                // Data is hardcoded, no need to reload
            }
        }
    }
}

