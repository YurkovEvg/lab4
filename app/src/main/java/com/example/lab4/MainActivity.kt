package com.example.lab4

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        trueButton = findViewById(R.id.true_button)
        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton = findViewById(R.id.false_button)
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)

        questionTextView = findViewById(R.id.textViewQuestion)

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        cheatButton.setOnClickListener { view ->

            val answerIsTrue =
                quizViewModel.currentQuestionAnswer
            val intent =
                CheatActivity.newIntent(this@MainActivity,
                    answerIsTrue)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                val options = ActivityOptions
                .makeClipRevealAnimation(view, 0, 0, view.width, view.height)
            startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
            } else {
                startActivityForResult(intent,
                    REQUEST_CODE_CHEAT)
            }

        }
        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode,
            resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT)
        {
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        trueButton.visibility = View.VISIBLE
        falseButton.visibility = View.VISIBLE
        if (quizViewModel.isLastQuestion()) {
            nextButton.visibility = View.GONE
        } else {
            nextButton.visibility = View.VISIBLE
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer: Boolean =
            quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater ->
                R.string.judgment_toast
            userAnswer == correctAnswer ->
                R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        trueButton.visibility = View.INVISIBLE
        falseButton.visibility = View.INVISIBLE
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        if (quizViewModel.isLastQuestion()) {
            Toast.makeText(this, getString(R.string.correct_answers, quizViewModel.getCorrectAnswersCount()), Toast.LENGTH_SHORT).show()
        }
    }
}