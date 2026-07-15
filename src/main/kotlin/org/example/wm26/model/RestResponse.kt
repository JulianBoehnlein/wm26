package org.example.wm26.model

data class RestResponse<T>(
    val status: Int,
    val payload: List<T>,
    val errors: List<String>? = null
)
