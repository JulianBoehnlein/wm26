package org.example.wm26.repository

import org.bson.types.ObjectId
import org.example.wm26.model.TeamStats
import org.springframework.data.mongodb.repository.MongoRepository

interface TeamStatsRepository : MongoRepository<TeamStats, ObjectId> {
    fun findByTeamId(teamId: Int): TeamStats?
}
