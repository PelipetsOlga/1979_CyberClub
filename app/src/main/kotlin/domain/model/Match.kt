package com.application.domain.model

data class Match(
    val id: String,
    val matchTitle: String,
    val dateTime: String, // Format: "Oct 12, 18:00"
    val teamA: Team,
    val teamB: Team,
    val tournamentName: String,
    val stage: String // e.g., "Group Stage", "Playoffs"
)

data class Team(
    val id: String,
    val name: String,
    val logoUrl: String? = null,
    val logoResId: Int? = null // For local resources
)

