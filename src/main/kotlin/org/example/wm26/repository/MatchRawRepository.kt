package org.example.wm26.repository

import org.bson.types.ObjectId
import org.example.wm26.model.MatchRawDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface MatchRawRepository : MongoRepository<MatchRawDocument, ObjectId> {
    fun existsByMatchId(matchId: Int): Boolean
}
