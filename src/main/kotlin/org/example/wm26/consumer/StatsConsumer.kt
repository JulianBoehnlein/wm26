package org.example.wm26.consumer

import org.example.wm26.model.Match
import org.example.wm26.service.StatsService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@KafkaListener(
    topics = ["wm26-match-results"],
    groupId = "wm26-stats-consumer-group",
)
class StatsConsumer(
    private val statsService: StatsService
) {
    private val log = LoggerFactory.getLogger(StatsConsumer::class.java)

    @KafkaHandler
    fun consume(message: Match) {
        log.info("StatsConsumer received message")
        statsService.processMatch(message)
    }
}
