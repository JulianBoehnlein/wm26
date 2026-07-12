package org.example.wm26.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "match_raw")
data class MatchRawDocument(
    @Id val id: ObjectId = ObjectId(),
    val matchId: Int,
    val match: Match
)
