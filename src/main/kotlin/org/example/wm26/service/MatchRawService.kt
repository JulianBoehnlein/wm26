package org.example.wm26.service

import org.example.wm26.model.Match
import org.example.wm26.model.MatchRawDocument
import org.example.wm26.repository.MatchRawRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MatchRawService(
    private val matchRawRepository: MatchRawRepository,
) {
    private val log = LoggerFactory.getLogger(MatchRawService::class.java)

    fun saveConsumedMatches(match: Match) {

        if (matchRawRepository.existsByMatchId(match.matchID)) {
            log.info("MatchRaw ${match.matchID} already exists, skipping")
            return
        }

        matchRawRepository.save(MatchRawDocument(matchId = match.matchID, match = match))
        log.info("MatchRaw ${match.matchID} saved in MongoDB")
    }
}
