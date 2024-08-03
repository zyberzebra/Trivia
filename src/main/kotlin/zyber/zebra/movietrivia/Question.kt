package zyber.zebra.movietrivia

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val question: String = "What is the answer to life, the universe, and everything?",
    val options: List<String> = listOf("42", "3", "5", "1337"),
    val correctAnswerIndex: Int = 0,
    var selected: Int = 0
)