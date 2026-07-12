package org.example.wm26.service

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.NoArgsConstructor
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@NoArgsConstructor
@Service
class StadiumCoordinateService {
    private val objectMapper = ObjectMapper()

    private val stadiums: Map<String, StadiumCoordinate> by lazy {
        val resource = ClassPathResource("stadiums.json")
        val json = resource.inputStream.bufferedReader().readText()
        objectMapper.readValue(
            json,
            object : TypeReference<Map<String, StadiumCoordinate>>() {}
        )
    }

    fun getCoordinates(locationCity: String): StadiumCoordinate? {
        return stadiums.entries
            .firstOrNull { locationCity.contains(it.key, ignoreCase = true) }
            ?.value
    }
}

data class StadiumCoordinate(
    @JsonProperty("lat") val lat: Double,
    @JsonProperty("lon") val lon: Double,
    @JsonProperty("city") val city: String
)
