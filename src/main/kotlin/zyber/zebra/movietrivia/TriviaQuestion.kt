package zyber.zebra.movietrivia

import kotlinx.serialization.Serializable

@Serializable
data class TriviaQuestion(
    val type: String,
    val difficulty: String,
    val category: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)