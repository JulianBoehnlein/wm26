package org.example.wm26.model

data class TeamAnalytics(
    val teamId: Int,
    val teamName: String,
    val totalMatches: Int,
    val totalGoals: Int,
    val comebackRate: Double,
    val penaltyGoalPercentage: Double,
    val halfTimeLeadWinRate: Double,
    val goalsPerMinuteBlock: Map<String, Int>,
    val ownGoals: Int
)

data class TemperatureGoalAnalysis(
    val temperatureRange: String,
    val averageGoals: Double,
    val matchCount: Int
)

data class KickoffTimeGoalAnalysis(
    val timeRange: String,
    val averageGoals: Double,
    val matchCount: Int
)

data class ClimateZoneAnalysis(
    val climateZone: String,
    val averageGoals: Double,
    val averageOwnGoals: Double,
    val averagePenaltyGoals: Double,
    val matchCount: Int
)
