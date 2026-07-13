package org.example.wm26.service

import org.example.wm26.client.WeatherClient
import org.example.wm26.model.Match
import org.example.wm26.model.MatchStats
import org.example.wm26.model.Team
import org.example.wm26.model.TeamStats
import org.example.wm26.repository.MatchStatsRepository
import org.example.wm26.repository.TeamStatsRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StatsService(
    private val teamStatsRepository: TeamStatsRepository,
    private val matchStatsRepository: MatchStatsRepository,
    private val weatherClient: WeatherClient,
    private val stadiumCoordinateService: StadiumCoordinateService,
    private val climateZoneService: ClimateZoneService,
) {
    private val log = LoggerFactory.getLogger(StatsService::class.java)

    fun processMatch(match: Match) {

        if (!match.matchIsFinished) {
            log.info("Match ${match.matchID} not finished, skipping")
            return
        }

        if (matchStatsRepository.existsByMatchId(match.matchID)) {
            log.info("Stats for match ${match.matchID} already exist, skipping")
            return
        }

        processMatchStats(match)
        processTeamStats(match, match.team1)
        processTeamStats(match, match.team2)
    }

    private fun processMatchStats(match: Match) {
        val goals = match.goals ?: emptyList()
        val totalGoals = goals.size
        val ownGoals = goals.count { it.isOwnGoal ?: false}
        val penaltyGoals = goals.count { it.isPenalty ?: false }
        val kickoffHour = match.matchDateTimeUTC.substring(11, 13).toInt()

        val coords = match.location?.let {
            stadiumCoordinateService.getCoordinates(it.locationCity)
        }

        val temperature = if (coords != null) {
            weatherClient.fetchTemperatureAtMatchTime(
                coords.lat,
                coords.lon,
                match.matchDateTimeUTC
            )
        } else null

        val climateZone = match.location?.let {
            climateZoneService.getClimateZone(it.locationCity)
        }

        matchStatsRepository.save(
            MatchStats(
                matchId = match.matchID,
                totalGoals = totalGoals,
                ownGoals = ownGoals,
                penaltyGoals = penaltyGoals,
                temperatureCelsius = temperature,
                climateZone = climateZone,
                kickoffHour = kickoffHour,
                locationCity = match.location?.locationCity,
                locationStadium = match.location?.locationStadium
            )
        )
        log.info("MatchStats saved for match ${match.matchID}")
    }

    private fun processTeamStats(match: Match, team: Team?) {
        if (team == null) return

        val teamStats = teamStatsRepository.findByTeamId(team.teamId)
            ?: TeamStats(teamId = team.teamId, teamName = team.teamName)

        val halfTime = match.matchResults?.find { it.resultTypeID == 1 }
        val fullTime = match.matchResults?.find { it.resultTypeID == 2 }
        val isTeam1 = team.teamId == match.team1?.teamId
        val goals = match.goals ?: emptyList()

        // Comeback-Rate + Halbzeit-Führung → Siegquote
        if (halfTime != null && fullTime != null) {
            val htOwn = if (isTeam1) halfTime.pointsTeam1 else halfTime.pointsTeam2
            val htOpp = if (isTeam1) halfTime.pointsTeam2 else halfTime.pointsTeam1
            val ftOwn = if (isTeam1) fullTime.pointsTeam1 else fullTime.pointsTeam2
            val ftOpp = if (isTeam1) fullTime.pointsTeam2 else fullTime.pointsTeam1

            // Comeback
            if (htOwn < htOpp) {
                teamStats.trailingAtHalfTime++
                if (ftOwn > ftOpp) teamStats.comebacks++
            }

            // Halbzeit-Führung → Sieg
            if (htOwn > htOpp) {
                teamStats.halfTimeLeads++
                if (ftOwn > ftOpp) teamStats.halfTimeLeadWins++
            }
        }

        // Tore des Teams
        val teamGoals = goals.filter { it.scoringTeamId == team.teamId }
        teamStats.totalGoals += teamGoals.size
        teamStats.totalMatches++
        teamStats.penaltyGoals += teamGoals.count { it.isPenalty ?: false }
        teamStats.ownGoals += goals.count { it.isOwnGoal ?: false && it.scoringTeamId != team.teamId }

        // Torminuten-Verteilung
        teamGoals.forEach { goal ->
            val minute = goal.matchMinute ?: return@forEach
            val block = when (minute) {
                in 0..15 -> "0-15"
                in 16..30 -> "16-30"
                in 31..45 -> "31-45"
                in 46..60 -> "46-60"
                in 61..75 -> "61-75"
                else -> "76-90"
            }
            teamStats.goalsPerMinuteBlock[block] =
                (teamStats.goalsPerMinuteBlock[block] ?: 0) + 1
        }

        teamStatsRepository.save(teamStats)
        log.info("TeamStats updated for ${team.teamName}")
    }
}
