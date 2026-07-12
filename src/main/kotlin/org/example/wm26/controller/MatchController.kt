package org.example.wm26.controller

import lombok.extern.slf4j.Slf4j
import org.example.wm26.service.MatchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
@RequestMapping("/sync/matches")
class MatchController {
    @Autowired
    private lateinit var matchService: MatchService

    @GetMapping
    fun syncMatches() {
        matchService.syncMatches()
    }
}
