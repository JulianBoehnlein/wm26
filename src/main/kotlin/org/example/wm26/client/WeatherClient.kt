package org.example.wm26.client

import org.example.wm26.model.WeatherResponse
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Service
class WeatherClient {
    private val restClient: RestClient = RestClient.create()

    fun fetchTemperatureAtMatchTime(
        lat: Double,
        lon: Double,
        matchDateTimeUTC: String  // z.B. "2026-06-11T19:00:00Z"
    ): Double? {
        val date = matchDateTimeUTC.substring(0, 10) // "2026-06-11"
        val hour = matchDateTimeUTC.substring(11, 13) // "19"
        val targetTime = "${date}T${hour}:00" // "2026-06-11T19:00"

        val response = restClient
            .get()
            .uri {
                it.scheme("https")
                    .host("archive-api.open-meteo.com")
                    .path("/v1/archive")
                    .queryParam("latitude", lat)
                    .queryParam("longitude", lon)
                    .queryParam("start_date", date)
                    .queryParam("end_date", date)
                    .queryParam("hourly", "temperature_2m")
                    .queryParam("timezone", "UTC")
                    .build()
            }
            .retrieve()
            .body<WeatherResponse>() ?: return null

        val index = response.hourly.time.indexOf(targetTime)
        if (index == -1) return null

        return response.hourly.temperature_2m.getOrNull(index)
    }
}
