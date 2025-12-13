package com.application.data.remote.dto

import com.application.domain.model.Match
import com.application.domain.model.Team
import com.google.gson.annotations.SerializedName

data class MatchDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("match_title")
    val matchTitle: String,
    @SerializedName("date_time")
    val dateTime: String,
    @SerializedName("team_a")
    val teamA: TeamDto,
    @SerializedName("team_b")
    val teamB: TeamDto,
    @SerializedName("tournament_name")
    val tournamentName: String,
    @SerializedName("stage")
    val stage: String
) {
    fun toDomain(): Match {
        return Match(
            id = id,
            matchTitle = matchTitle,
            dateTime = dateTime,
            teamA = teamA.toDomain(),
            teamB = teamB.toDomain(),
            tournamentName = tournamentName,
            stage = stage
        )
    }
}

data class TeamDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("logo_url")
    val logoUrl: String? = null,
    @SerializedName("logo_res_id")
    val logoResId: Int? = null
) {
    fun toDomain(): Team {
        return Team(
            id = id,
            name = name,
            logoUrl = logoUrl,
            logoResId = logoResId
        )
    }
}

