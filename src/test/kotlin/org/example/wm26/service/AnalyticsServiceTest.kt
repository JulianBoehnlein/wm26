package org.example.wm26.service


import io.mockk.every
import io.mockk.mockk
import org.example.wm26.model.MatchStats
import org.example.wm26.model.TeamStats
import org.example.wm26.repository.MatchStatsRepository
import org.example.wm26.repository.TeamStatsRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AnalyticsServiceTest {

    private val teamStatsRepository: TeamStatsRepository = mockk()
    private val matchStatsRepository: MatchStatsRepository = mockk()
    private lateinit var analyticsService: AnalyticsService

    @BeforeEach
    fun setup() {
        analyticsService = AnalyticsService(teamStatsRepository, matchStatsRepository)
    }

    // ── Team Analysen ──────────────────────────────────────────

    @Test
    fun `comeback rate wird korrekt berechnet`() {
        every { teamStatsRepository.findAll() } returns listOf(
            TeamStats(
                teamId = 1,
                teamName = "Deutschland",
                totalMatches = 5,
                totalGoals = 10,
                comebacks = 2,
                trailingAtHalfTime = 4,
                penaltyGoals = 2,
                halfTimeLeads = 3,
                halfTimeLeadWins = 3
            )
        )

        val result = analyticsService.getAllTeamAnalytics()

        assertEquals(50.0, result.first().comebackRate)
    }

    @Test
    fun `comeback rate ist 0 wenn team nie hinten lag`() {
        every { teamStatsRepository.findAll() } returns listOf(
            TeamStats(
                teamId = 1,
                teamName = "Spanien",
                totalMatches = 3,
                totalGoals = 6,
                comebacks = 0,
                trailingAtHalfTime = 0,
                penaltyGoals = 0,
                halfTimeLeads = 3,
                halfTimeLeadWins = 3
            )
        )

        val result = analyticsService.getAllTeamAnalytics()

        assertEquals(0.0, result.first().comebackRate)
    }

    @Test
    fun `elfmeter anteil wird korrekt berechnet`() {
        every { teamStatsRepository.findAll() } returns listOf(
            TeamStats(
                teamId = 1,
                teamName = "England",
                totalMatches = 4,
                totalGoals = 8,
                comebacks = 0,
                trailingAtHalfTime = 0,
                penaltyGoals = 2,
                halfTimeLeads = 0,
                halfTimeLeadWins = 0
            )
        )

        val result = analyticsService.getAllTeamAnalytics()

        assertEquals(25.0, result.first().penaltyGoalPercentage)
    }

    @Test
    fun `elfmeter anteil ist 0 wenn keine tore erzielt`() {
        every { teamStatsRepository.findAll() } returns listOf(
            TeamStats(
                teamId = 1,
                teamName = "Katar",
                totalMatches = 3,
                totalGoals = 0,
                comebacks = 0,
                trailingAtHalfTime = 0,
                penaltyGoals = 0,
                halfTimeLeads = 0,
                halfTimeLeadWins = 0
            )
        )

        val result = analyticsService.getAllTeamAnalytics()

        assertEquals(0.0, result.first().penaltyGoalPercentage)
    }

    @Test
    fun `halbzeit fuehrung siegquote wird korrekt berechnet`() {
        every { teamStatsRepository.findAll() } returns listOf(
            TeamStats(
                teamId = 1,
                teamName = "Frankreich",
                totalMatches = 5,
                totalGoals = 12,
                comebacks = 0,
                trailingAtHalfTime = 0,
                penaltyGoals = 1,
                halfTimeLeads = 4,
                halfTimeLeadWins = 3
            )
        )

        val result = analyticsService.getAllTeamAnalytics()

        assertEquals(75.0, result.first().halfTimeLeadWinRate)
    }

    // ── Temperatur Analysen ────────────────────────────────────

    @Test
    fun `match bei 28 grad landet im 25-30 bucket`() {
        every { matchStatsRepository.findAll() } returns listOf(
            MatchStats(matchId = 1, totalGoals = 4, ownGoals = 0,
                penaltyGoals = 1, temperatureCelsius = 28.0,
                climateZone = "HOT", kickoffHour = 19, locationCity = "Houston",
                locationStadium = "NRG Stadium")
        )

        val result = analyticsService.getTemperatureGoalAnalysis()

        val bucket = result.find { it.temperatureRange == "25-30°C" }
        assertNotNull(bucket)
        assertEquals(4.0, bucket!!.averageGoals)
        assertEquals(1, bucket.matchCount)
    }

    @Test
    fun `leere temperatur buckets haben 0 durchschnitt`() {
        every { matchStatsRepository.findAll() } returns listOf(
            MatchStats(matchId = 1, totalGoals = 3, ownGoals = 0,
                penaltyGoals = 0, temperatureCelsius = 28.0,
                climateZone = "HOT", kickoffHour = 20, locationCity = "Dallas",
                locationStadium = "AT&T Stadium")
        )

        val result = analyticsService.getTemperatureGoalAnalysis()

        val coldBucket = result.find { it.temperatureRange == "< 15°C" }
        assertEquals(0.0, coldBucket!!.averageGoals)
        assertEquals(0, coldBucket.matchCount)
    }

    @Test
    fun `matches ohne temperatur werden gefiltert`() {
        every { matchStatsRepository.findAll() } returns listOf(
            MatchStats(matchId = 1, totalGoals = 3, ownGoals = 0,
                penaltyGoals = 0, temperatureCelsius = null,
                climateZone = "HOT", kickoffHour = 20, locationCity = "Dallas",
                locationStadium = "AT&T Stadium"),
            MatchStats(matchId = 2, totalGoals = 2, ownGoals = 0,
                penaltyGoals = 0, temperatureCelsius = 27.0,
                climateZone = "HOT", kickoffHour = 20, locationCity = "Houston",
                locationStadium = "NRG Stadium")
        )

        val result = analyticsService.getTemperatureGoalAnalysis()

        val totalMatches = result.sumOf { it.matchCount }
        assertEquals(1, totalMatches)
    }

    // ── Uhrzeit Analysen ───────────────────────────────────────

    @Test
    fun `stunde 19 landet in 18-24 uhr bucket`() {
        every { matchStatsRepository.findAll() } returns listOf(
            MatchStats(matchId = 1, totalGoals = 3, ownGoals = 0,
                penaltyGoals = 0, temperatureCelsius = 22.0,
                climateZone = "MODERATE", kickoffHour = 19,
                locationCity = "New York", locationStadium = "MetLife Stadium")
        )

        val result = analyticsService.getKickoffTimeGoalAnalysis()

        val bucket = result.find { it.timeRange == "18-24 Uhr" }
        assertNotNull(bucket)
        assertEquals(3.0, bucket!!.averageGoals)
        assertEquals(1, bucket.matchCount)
    }

    @Test
    fun `durchschnitt wird korrekt uber mehrere matches berechnet`() {
        every { matchStatsRepository.findAll() } returns listOf(
            MatchStats(matchId = 1, totalGoals = 2, ownGoals = 0,
                penaltyGoals = 0, temperatureCelsius = 20.0,
                climateZone = "MODERATE", kickoffHour = 20,
                locationCity = "Seattle", locationStadium = "Lumen Field"),
            MatchStats(matchId = 2, totalGoals = 4, ownGoals = 0,
                penaltyGoals = 0, temperatureCelsius = 18.0,
                climateZone = "COOL", kickoffHour = 22,
                locationCity = "Vancouver", locationStadium = "BC Place")
        )

        val result = analyticsService.getKickoffTimeGoalAnalysis()

        val bucket = result.find { it.timeRange == "18-24 Uhr" }
        assertEquals(3.0, bucket!!.averageGoals)
        assertEquals(2, bucket.matchCount)
    }

    // ── Klimazone Analysen ─────────────────────────────────────

    @Test
    fun `klimazonen werden korrekt gruppiert`() {
        every { matchStatsRepository.findAll() } returns listOf(
            MatchStats(matchId = 1, totalGoals = 4, ownGoals = 1,
                penaltyGoals = 1, temperatureCelsius = 32.0,
                climateZone = "HOT", kickoffHour = 20,
                locationCity = "Houston", locationStadium = "NRG Stadium"),
            MatchStats(matchId = 2, totalGoals = 2, ownGoals = 0,
                penaltyGoals = 0, temperatureCelsius = 33.0,
                climateZone = "HOT", kickoffHour = 19,
                locationCity = "Miami", locationStadium = "Hard Rock Stadium"),
            MatchStats(matchId = 3, totalGoals = 3, ownGoals = 0,
                penaltyGoals = 0, temperatureCelsius = 15.0,
                climateZone = "COOL", kickoffHour = 17,
                locationCity = "Vancouver", locationStadium = "BC Place")
        )

        val result = analyticsService.getClimateZoneAnalysis()

        val hotZone = result.find { it.climateZone == "HOT" }
        val coolZone = result.find { it.climateZone == "COOL" }

        assertNotNull(hotZone)
        assertNotNull(coolZone)
        assertEquals(3.0, hotZone!!.averageGoals)
        assertEquals(2, hotZone.matchCount)
        assertEquals(3.0, coolZone!!.averageGoals)
        assertEquals(1, coolZone.matchCount)
    }
}
