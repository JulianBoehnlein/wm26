package org.example.wm26.service

import org.springframework.stereotype.Service

@Service
class ClimateZoneService {

    fun getClimateZone(city: String): String {
        return when {
            city.contains("Houston", ignoreCase = true) ||
                    city.contains("Miami", ignoreCase = true) ||
                    city.contains("Mexico City", ignoreCase = true) ||
                    city.contains("Monterrey", ignoreCase = true) ||
                    city.contains("Guadalajara", ignoreCase = true) -> "HOT"

            city.contains("Dallas", ignoreCase = true) ||
                    city.contains("Atlanta", ignoreCase = true) ||
                    city.contains("Philadelphia", ignoreCase = true) ||
                    city.contains("New York", ignoreCase = true) ||
                    city.contains("Kansas City", ignoreCase = true) -> "MODERATE"

            city.contains("Vancouver", ignoreCase = true) ||
                    city.contains("Seattle", ignoreCase = true) ||
                    city.contains("Toronto", ignoreCase = true) ||
                    city.contains("San Francisco", ignoreCase = true) ||
                    city.contains("Boston", ignoreCase = true) -> "COOL"

            else -> "UNKNOWN"
        }
    }
}
