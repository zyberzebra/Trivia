package zyber.zebra.movietrivia

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json
import org.apache.commons.text.StringEscapeUtils
import org.apache.commons.text.StringEscapeUtils.unescapeHtml4
import org.springframework.stereotype.Service

@Service
class QuizService {


    suspend fun getTriviaQuestion(amount: Int = 1) : List<Question> {
        val questions = fetchQuestions(amount)
        return questions;
    }

    suspend fun fetchQuestions(amount: Int = 1): List<Question> {
        val json = Json {
            ignoreUnknownKeys = true
        }

        val client = HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }
        }

        val response: String = client.get("https://opentdb.com/api.php?amount=$amount&category=11")
        client.close()

        val apiResponse = json.decodeFromString<ApiResponse>(response)

        return apiResponse.results.map { triviaQuestion ->
            val question = unescapeHtml4(triviaQuestion.question)
            val allOptions = triviaQuestion.incorrect_answers.map(StringEscapeUtils::unescapeHtml4).toMutableList()
            val correctAnswer = unescapeHtml4(triviaQuestion.correct_answer)
            val indexToInsertCorrectAnswer = (0..allOptions.size).random()
            allOptions.add(indexToInsertCorrectAnswer, correctAnswer)
            Question(
                question = question,
                options = allOptions,
                correctAnswerIndex = allOptions.indexOf(correctAnswer)
            )
        }
    }


}