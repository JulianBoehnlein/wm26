package org.example.wm26.client

import org.example.wm26.model.Match
import org.example.wm26.producer.MatchResultProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ObjectMapper

@Service
class OpenLigaClient {
    private val restClient: RestClient = RestClient.create()

    @Autowired
    private lateinit var producer: MatchResultProducer

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    fun fetchOpenLigaData(): List<Match>? {
        return restClient
            .get()
            .uri("https://api.openligadb.de/getmatchdata/wm26/2026")
            .retrieve()
            .body<List<Match>>()
    }
}
