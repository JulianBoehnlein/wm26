package org.example.wm26.model

data class WeatherResponse(
    val hourly: HourlyData
)

data class HourlyData(
    val time: List<String>,
    val temperature_2m: List<Double?>
)

data class MatchWeather(
    val temperatureCelsius: Double,
    val locationCity: String
)
