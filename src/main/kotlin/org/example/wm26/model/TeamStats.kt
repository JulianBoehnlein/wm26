package org.example.wm26.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "team_stats")
data class TeamStats(
    @Id val id: ObjectId = ObjectId(),
    val teamId: Int,
    val teamName: String,
    var totalGoals: Int = 0,
    var totalMatches: Int = 0,
    var comebacks: Int = 0,
    var trailingAtHalfTime: Int = 0,
    var penaltyGoals: Int = 0,
    var ownGoals: Int = 0,
    var halfTimeLeads: Int = 0,
    var halfTimeLeadWins: Int = 0,
    var goalsPerMinuteBlock: MutableMap<String, Int> = mutableMapOf(
        "0-15" to 0,
        "16-30" to 0,
        "31-45" to 0,
        "46-60" to 0,
        "61-75" to 0,
        "76-90" to 0
    )
)
