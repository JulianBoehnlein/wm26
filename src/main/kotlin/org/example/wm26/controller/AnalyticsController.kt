package org.example.wm26.controller

import org.example.wm26.model.ClimateZoneAnalysis
import org.example.wm26.model.KickoffTimeGoalAnalysis
import org.example.wm26.model.RestResponse
import org.example.wm26.model.TeamAnalytics
import org.example.wm26.model.TemperatureGoalAnalysis
import org.example.wm26.service.AnalyticsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping("/api/analytics")
class AnalyticsController(
    private val analyticsService: AnalyticsService
) {
    @GetMapping("/teams")
    fun getTeamsStats(): ResponseEntity<RestResponse<TeamAnalytics>> {
        val payload = analyticsService.getAllTeamAnalytics()

        if (payload.isEmpty()) return ResponseEntity.status(404).body(RestResponse(404, emptyList()))

        return ResponseEntity.ok().body(RestResponse(200, payload))
    }

    @GetMapping("/teams/{teamId}")
    fun getTeamStats(@PathVariable teamId: Int): ResponseEntity<RestResponse<TeamAnalytics>> {
        val payload = analyticsService.getTeamAnalytics(teamId)

        payload ?: return ResponseEntity.status(404).body(RestResponse(404, emptyList()))

        return ResponseEntity.ok().body(RestResponse(200, listOf(payload)))
    }

    @GetMapping("/temperature")
    fun getTemperaturesStats(): ResponseEntity<RestResponse<TemperatureGoalAnalysis>> {
        val payload = analyticsService.getTemperatureGoalAnalysis()

        if(payload.isEmpty()) return ResponseEntity.status(404).body(RestResponse(404, emptyList()))

        return ResponseEntity.ok().body(RestResponse(200, payload))
    }

    @GetMapping("/kickoff-time")
    fun getKickOffStats(): ResponseEntity<RestResponse<KickoffTimeGoalAnalysis>> {
        val payload = analyticsService.getKickoffTimeGoalAnalysis()

        if(payload.isEmpty()) return ResponseEntity.status(404).body(RestResponse(404, emptyList()))

        return ResponseEntity.ok().body(RestResponse(200, payload))
    }

    @GetMapping("/climate-zone")
    fun getClimateStats(): ResponseEntity<RestResponse<ClimateZoneAnalysis>> {
        val payload = analyticsService.getClimateZoneAnalysis()

        if(payload.isEmpty()) return ResponseEntity.status(404).body(RestResponse(404, emptyList()))

        return ResponseEntity.ok().body(RestResponse(200, payload))
    }
}
