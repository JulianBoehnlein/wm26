package org.example.wm26.producer

import lombok.extern.slf4j.Slf4j
import org.example.wm26.model.Match
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Slf4j
@Service
class MatchResultProducer {
    @Autowired
    private lateinit var template: KafkaTemplate<String, Match>

    fun publish(matchId: String, payload: Match) {
        template.send("wm26-match-results", matchId, payload)
    }
}
