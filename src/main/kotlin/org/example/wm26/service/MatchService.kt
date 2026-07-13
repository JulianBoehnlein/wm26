package org.example.wm26.service

import org.example.wm26.client.OpenLigaClient
import org.example.wm26.client.WeatherClient
import org.example.wm26.model.MatchDocument
import org.example.wm26.model.MatchWeather
import org.example.wm26.producer.MatchResultProducer
import org.example.wm26.repository.MatchRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class MatchService(
    private val openLigaClient: OpenLigaClient,
    private val matchRepository: MatchRepository,
    private val matchResultProducer: MatchResultProducer,
    private val weatherClient: WeatherClient,
    private val stadiumCoordinateService: StadiumCoordinateService
) {
    private val log = LoggerFactory.getLogger(MatchService::class.java)

    fun syncMatches() {
        val matches = openLigaClient.fetchOpenLigaData()

        matches?.forEach { match ->
            if (matchRepository.existsByMatchId(match.matchID)) {
                log.info("Match ${match.matchID} already exists, skipping")
                return@forEach
            }

            val weather = if (match.matchIsFinished && match.location != null) {
                val coords = stadiumCoordinateService
                    .getCoordinates(match.location.locationCity)

                if (coords != null) {
                    val temp = weatherClient.fetchTemperatureAtMatchTime(
                        coords.lat,
                        coords.lon,
                        match.matchDateTimeUTC
                    )
                    temp?.let { MatchWeather(it, coords.city) }
                } else null
            } else null

            matchRepository.save(
                MatchDocument(
                    matchId = match.matchID,
                    weather = weather
                )
            )
            log.info("Match ${match.matchID} saved to MongoDB")

            matchResultProducer.publish(match.matchID.toString(), match)
            log.info("Match ${match.matchID} published to Kafka")
        }
    }
}

