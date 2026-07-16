package org.example.wm26.service

import org.example.wm26.model.*
import org.example.wm26.repository.MatchStatsRepository
import org.example.wm26.repository.TeamStatsRepository
import org.springframework.stereotype.Service
import kotlin.math.roundToInt

@Service
class AnalyticsService(
    private val teamStatsRepository: TeamStatsRepository,
    private val matchStatsRepository: MatchStatsRepository
) {

    fun getAllTeamAnalytics(): List<TeamAnalytics> {
        return teamStatsRepository.findAll().map { it.toAnalytics() }
    }

    fun getTeamAnalytics(teamId: Int): TeamAnalytics? {
        return teamStatsRepository.findByTeamId(teamId)?.toAnalytics()
    }

    private fun TeamStats.toAnalytics() = TeamAnalytics(
        teamId = teamId,
        teamName = teamName,
        totalMatches = totalMatches,
        totalGoals = totalGoals,
        comebackRate = if (trailingAtHalfTime > 0)
            (comebacks.toDouble() / trailingAtHalfTime * 100).round()
        else 0.0,
        penaltyGoalPercentage = if (totalGoals > 0)
            (penaltyGoals.toDouble() / totalGoals * 100).round()
        else 0.0,
        halfTimeLeadWinRate = if (halfTimeLeads > 0)
            (halfTimeLeadWins.toDouble() / halfTimeLeads * 100).round()
        else 0.0,
        goalsPerMinuteBlock = goalsPerMinuteBlock,
        ownGoals = ownGoals,
        comebacks = comebacks,
        trailingAtHalftime = trailingAtHalfTime,
        halfTimeLeads = halfTimeLeads,
        halfTimeLeadWins = halfTimeLeadWins
    )

    fun getTemperatureGoalAnalysis(): List<TemperatureGoalAnalysis> {
        val allStats = matchStatsRepository.findAll()
            .filter { it.temperatureCelsius != null }

        val ranges = mapOf(
            "< 15°C" to { temp: Double -> temp < 15 },
            "15-20°C" to { temp: Double -> temp in 15.0..19.99 },
            "20-25°C" to { temp: Double -> temp in 20.0..24.99 },
            "25-30°C" to { temp: Double -> temp in 25.0..29.99 },
            "> 30°C" to { temp: Double -> temp >= 30 }
        )

        return ranges.map { (label, filter) ->
            val matches = allStats.filter { filter(it.temperatureCelsius!!) }
            TemperatureGoalAnalysis(
                temperatureRange = label,
                averageGoals = if (matches.isNotEmpty())
                    (matches.sumOf { it.totalGoals }.toDouble() / matches.size).round()
                else 0.0,
                matchCount = matches.size
            )
        }
    }

    fun getKickoffTimeGoalAnalysis(): List<KickoffTimeGoalAnalysis> {
        val allStats = matchStatsRepository.findAll()

        val ranges = mapOf(
            "0-6 Uhr" to (0..5),
            "6-12 Uhr" to (6..11),
            "12-18 Uhr" to (12..17),
            "18-24 Uhr" to (18..23)
        )

        return ranges.map { (label, hours) ->
            val matches = allStats.filter { it.kickoffHour in hours }
            KickoffTimeGoalAnalysis(
                timeRange = label,
                averageGoals = if (matches.isNotEmpty())
                    (matches.sumOf { it.totalGoals }.toDouble() / matches.size).round()
                else 0.0,
                matchCount = matches.size
            )
        }
    }

    fun getClimateZoneAnalysis(): List<ClimateZoneAnalysis> {
        val allStats = matchStatsRepository.findAll()
            .filter { it.climateZone != null }

        return allStats.groupBy { it.climateZone!! }.map { (zone, matches) ->
            ClimateZoneAnalysis(
                climateZone = zone,
                averageGoals = (matches.sumOf { it.totalGoals }.toDouble() / matches.size).round(),
                averageOwnGoals = (matches.sumOf { it.ownGoals }.toDouble() / matches.size).round(),
                averagePenaltyGoals = (matches.sumOf { it.penaltyGoals }.toDouble() / matches.size).round(),
                matchCount = matches.size
            )
        }.sortedBy { it.climateZone }
    }

    private fun Double.round(decimals: Int = 2): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return (this * multiplier).roundToInt() / multiplier
    }
}
