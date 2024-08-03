package zyber.zebra.movietrivia

import org.springframework.stereotype.Service

@Service
class QuizService {


    fun question() : Question {
        val question = Question()
        return question;
    }


}