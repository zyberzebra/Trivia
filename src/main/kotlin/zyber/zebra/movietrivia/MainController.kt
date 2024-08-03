package zyber.zebra.movietrivia

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {

    @Autowired
    private lateinit var quizService: QuizService

    private lateinit var currentQuestion: Question

    @GetMapping("/")
    fun welcome(): String {
        val welcomeMessage = "Welcome to the Movie Trivia Quiz!"
        currentQuestion = quizService.question()
        val options = currentQuestion.options.joinToString("<br>",prefix = "<br>", postfix = "<br>")
        currentQuestion.correctAnswerIndex
        val questionString = """
            <h2>${currentQuestion.question}</h2>
            ${currentQuestion.options.mapIndexed { index, option ->
            "<button onclick=\"checkAnswer($index)\">$option</button>"
        }.joinToString("<br>")}
        """.trimIndent()
        return """
            <html>
                <body>
                    $welcomeMessage<br><br>
                    $questionString
                    <script>
                        function checkAnswer(selectedIndex) {
                            fetch('/check-answer?index=' + selectedIndex)
                                .then(response => response.text())
                                .then(data => alert(data))
                        }
                    </script>
                </body>
            </html>
        """
    }

    @GetMapping("/check-answer")
    fun checkAnswer(@RequestParam index: Int): String {
        return if (index == currentQuestion.correctAnswerIndex) {
            "Correct! Well done."
        } else {
            "Incorrect. Try again!"
        }
    }

}