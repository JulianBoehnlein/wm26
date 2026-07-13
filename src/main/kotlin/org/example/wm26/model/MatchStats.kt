package org.example.wm26.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "match_stats")
data class MatchStats(
    @Id val id: ObjectId = ObjectId(),
    val matchId: Int,
    val totalGoals: Int,
    val ownGoals: Int,
    val penaltyGoals: Int,
    val temperatureCelsius: Double?,
    val climateZone: String?,
    val kickoffHour: Int,
    val locationCity: String?,
    val locationStadium: String?
)
