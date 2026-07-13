package org.example.wm26.consumer

import org.example.wm26.model.Match
import org.example.wm26.service.MatchRawService
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.logging.Logger

@KafkaListener(
    topics = ["wm26-match-results"],
    groupId = "wm26-consumer-group",
)
@Component
class MatchConsumer(
    val matchRawService: MatchRawService,
) {
    private val log = Logger.getLogger(MatchConsumer::class.java.name)

    @KafkaHandler
    fun consume(message: Match) {
        log.info("Received a match: $message")

        matchRawService.saveConsumedMatches(message)
    }
}
