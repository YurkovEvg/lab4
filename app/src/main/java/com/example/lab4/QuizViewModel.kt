package com.example.lab4

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.lab4.Question

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    var currentIndex = 0
    var isCheater = false
    private var correctAnswers = 0

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun isLastQuestion(): Boolean {
        return currentIndex == questionBank.size - 1
    }

    fun checkAnswer(userAnswer: Boolean): Boolean {
        val correctAnswer = questionBank[currentIndex].answer
        if (userAnswer == correctAnswer) {
            correctAnswers++
        }
        return userAnswer == correctAnswer
    }

    fun getCorrectAnswersCount(): Int {
        return correctAnswers
    }
}