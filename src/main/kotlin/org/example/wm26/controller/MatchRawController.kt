package org.example.wm26.controller

import org.example.wm26.model.Match
import org.example.wm26.model.RestResponse
import org.example.wm26.service.MatchRawService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping("/api/matches")
class MatchRawController(
    private val matchRawService: MatchRawService,
) {
    @GetMapping()
    fun getMatches(): ResponseEntity<RestResponse<Match>> {
        val payload = matchRawService.getMatches()

        if (payload.isEmpty()) {

            return ResponseEntity.status(404).body(RestResponse(404, emptyList()))
        }

        return ResponseEntity.ok(RestResponse(200, payload))
    }

    @GetMapping("/{matchId}")
    fun getMatch(@PathVariable matchId: Int): ResponseEntity<RestResponse<Match>> {
        val payload = matchRawService.getMatch(matchId)

        payload ?: return ResponseEntity.status(404).body(RestResponse(404, emptyList()))

        return ResponseEntity.ok(RestResponse(200, listOf(payload)))
    }
}
