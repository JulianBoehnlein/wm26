package org.example.wm26.repository

import org.bson.types.ObjectId
import org.example.wm26.model.MatchDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface MatchRepository: MongoRepository<MatchDocument, ObjectId> {
    fun existsByMatchId(matchId: Int): Boolean
}
