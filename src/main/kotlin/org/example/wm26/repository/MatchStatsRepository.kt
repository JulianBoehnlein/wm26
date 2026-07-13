package org.example.wm26.repository

import org.bson.types.ObjectId
import org.example.wm26.model.MatchStats
import org.springframework.data.mongodb.repository.MongoRepository

interface MatchStatsRepository : MongoRepository<MatchStats, ObjectId> {
    fun existsByMatchId(matchId: Int): Boolean
}
