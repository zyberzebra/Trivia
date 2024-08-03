package zyber.zebra.movietrivia

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val response_code: Int,
    val results: List<TriviaQuestion>
)