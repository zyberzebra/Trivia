package zyber.zebra.movietrivia

import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {

    @Autowired
    private lateinit var quizService: QuizService

    private lateinit var questions: List<Question>
    private lateinit var currentQuestion: Question
    private var questionIndex: Int = 0

    @GetMapping("/")
    fun welcome(): String {
        val welcomeMessage = "Welcome to the Movie Trivia Quiz!"
        val form = """
            <html>
                <body>
                    $welcomeMessage<br><br>
                    <form action="/start-quiz" method="get">
                        <label for="amount">How many questions would you like to answer?</label><br>
                        <input type="number" id="amount" name="amount" min="1" max="50"><br><br>
                        <input type="submit" value="Start Quiz">
                    </form>
                </body>
            </html>
        """
        return form
    }

    @GetMapping("/start-quiz")
    fun startQuiz(@RequestParam amount: Int): String {
        questions = runBlocking { quizService.getTriviaQuestion(amount) }
        questionIndex = 0
        currentQuestion = questions[questionIndex]

        return getQuestionHtml()
    }

    @GetMapping("/check-answer")
    fun checkAnswer(@RequestParam index: Int): String {
        return if (index == currentQuestion.correctAnswerIndex) {
            currentQuestion.hasBeenAnsweredCorrect = true
            val nextQuestion = questions.find { !it.hasBeenAnsweredCorrect }
            if (nextQuestion != null) {
                currentQuestion = nextQuestion
               getQuestionHtml()
            } else {
                "Correct! This is the end of the quiz"
            }
        } else {
            getQuestionHtml("Incorrect. Try again!")
        }
    }

    private fun getQuestionHtml(feedback: String = ""): String {
        val questionString = """
            <h2>${currentQuestion.question}</h2>
            ${currentQuestion.options.mapIndexed { index, option ->
            "<button onclick=\"checkAnswer($index)\">$option</button>"
        }.joinToString("<br>")}
            <br><br>$feedback
        """.trimIndent()

        return """
            <html>
                <body id="question-container">
                    $questionString
                    <script>
                        function checkAnswer(selectedIndex) {
                            fetch('/check-answer?index=' + selectedIndex)
                                .then(response => response.text())
                                .then(data => document.getElementById('question-container').innerHTML = data)
                                .catch(error => console.error('Error:', error));
                        }
                    </script>
                </body>
            </html>
        """
    }
}
