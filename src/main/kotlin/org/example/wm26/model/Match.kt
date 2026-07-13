package org.example.wm26.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Match(
    val matchID: Int,
    val matchDateTime: String,
    val matchDateTimeUTC: String,
    val matchIsFinished: Boolean,
    val team1: Team?,
    val team2: Team?,
    val matchResults: List<MatchResult>?,
    val goals: List<Goal>?,
    val location: Location?,
    val group: Group?
)

data class Team(
    val teamId: Int,
    val teamName: String,
    val shortName: String,
    val teamIconUrl: String?
)

data class Group(
    val groupName: String,
    val groupOrderID: Int,
    val groupID: Int
)

data class MatchResult(
    val resultID: Int,
    val resultName: String,
    val pointsTeam1: Int,
    val pointsTeam2: Int,
    val resultTypeID: Int   // 1 = Halbzeit, 2 = Endergebnis
)

data class Goal(
    val goalID: Int,
    val scoreTeam1: Int,
    val scoreTeam2: Int,
    val matchMinute: Int?,
    val goalGetterID: Int?,
    val goalGetterName: String?,
    val scoringTeamId: Int?,
    @JsonProperty("isPenalty") val isPenalty: Boolean?,
    @JsonProperty("isOwnGoal") val isOwnGoal: Boolean?,
    @JsonProperty("isOvertime") val isOvertime: Boolean?
)

data class Location(
    val locationID: Int,
    val locationCity: String,
    val locationStadium: String
)
