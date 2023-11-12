package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import kotlin.reflect.typeOf

class QuizQuestionsActivity : AppCompatActivity() {
    private var userName: String? = null
    private val questionsList: ArrayList<Question> = Constants.getQuestions()
    private var currentQuestionIndex = 0
    private var selectedAlternativeIndex = -1 // Chỉ số lựa chọn (-1 chưa chọn)
    private var isAnswerChecked = false // user kiem tra cau hoi
    private var totalScore = 0

    private var tvQuestion: TextView? = null
    private var ivImage: ImageView? = null
    private var progressBar: ProgressBar? = null
    private var tvProgress: TextView? = null
    private var btnSubmit: Button? = null
    private var tvAlternatives: ArrayList<TextView>? = null // Ds chua cac cau tra loi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        initializeViews()

        updateQuestion()

        btnSubmit?.setOnClickListener {
            if (!isAnswerChecked) {
                handleUnansweredQuestion()
            } else {
                handleAnsweredQuestion()
            }
        }

        tvAlternatives?.let {
            setAlternativeClickListeners(it)
        }
    }

    //  khởi tạo và kết nối các thành phần giao diện xml
    private fun initializeViews() {
        userName = intent.getStringExtra("USER_NAME")
        tvQuestion = findViewById(R.id.tvQuestion)
        ivImage = findViewById(R.id.ivImage)
        progressBar = findViewById(R.id.progressBar)
        tvProgress = findViewById(R.id.tvProgress)
        btnSubmit = findViewById(R.id.btnSubmit)
        tvAlternatives = arrayListOf(
            findViewById(R.id.optionOne),
            findViewById(R.id.optionTwo),
            findViewById(R.id.optionThree),
            findViewById(R.id.optionFour),
        )
    }

    private fun handleUnansweredQuestion() {
        val anyAnswerIsChecked = selectedAlternativeIndex != -1
        if (!anyAnswerIsChecked) {
            Toast.makeText(this, "Please, select an alternative", Toast.LENGTH_SHORT).show()
        } else {
            val currentQuestion = questionsList[currentQuestionIndex]
            if (selectedAlternativeIndex == currentQuestion.correctAnswerIndex) {
                handleCorrectAnswer()
            } else {
                handleWrongAnswer(currentQuestion)
            }

            isAnswerChecked = true
            btnSubmit?.text = if (currentQuestionIndex == questionsList.size - 1) "FINISH" else "GO TO NEXT QUESTION"
            selectedAlternativeIndex = -1
        }
    }

    private fun handleCorrectAnswer() {
        answerView(tvAlternatives!![selectedAlternativeIndex], R.drawable.correct_option_border_bg)
        totalScore++
    }

    private fun handleWrongAnswer(currentQuestion: Question) {
        answerView(tvAlternatives!![selectedAlternativeIndex], R.drawable.wrong_option_border_bg)
        answerView(tvAlternatives!![currentQuestion.correctAnswerIndex], R.drawable.correct_option_border_bg)
    }

    private fun handleAnsweredQuestion() {
        if (currentQuestionIndex < questionsList.size - 1) {
            currentQuestionIndex++
            updateQuestion()
        } else {
            navigateToResultActivity()
        }

        isAnswerChecked = false
    }

    private fun setAlternativeClickListeners(alternatives: List<TextView>) {
        for (optionIndex in alternatives.indices) {
            alternatives[optionIndex].setOnClickListener {
                if (!isAnswerChecked) {
                    selectedAlternativeView(it as TextView, optionIndex)
                }
            }
        }
    }

    private fun navigateToResultActivity() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("USER_NAME", userName)
        intent.putExtra("total_questions", questionsList.size)
        intent.putExtra("score", totalScore)
        startActivity(intent)
        finish()
    }

    private fun updateQuestion() {
        defaultAlternativesView()

        // Render Question Text
        tvQuestion?.text = questionsList[currentQuestionIndex].questionText
        // Render Question Image
        ivImage?.setImageResource(questionsList[currentQuestionIndex].image)
        // progressBar
        progressBar?.progress = currentQuestionIndex + 1
        // Text of progress bar
        tvProgress?.text = "${currentQuestionIndex + 1}/${questionsList.size}"

        for (alternativeIndex in questionsList[currentQuestionIndex].alternatives.indices) {
            tvAlternatives!![alternativeIndex].text = questionsList[currentQuestionIndex].alternatives[alternativeIndex]
        }

        btnSubmit?.text = if (currentQuestionIndex == questionsList.size - 1) "FINISH" else "SUBMIT"
    }

    private fun defaultAlternativesView() {
        for (alternativeTv in tvAlternatives!!) {
            alternativeTv.typeface = Typeface.DEFAULT
            alternativeTv.setTextColor(Color.parseColor("#7A8089"))
            alternativeTv.background = ContextCompat.getDrawable(
                this@QuizQuestionsActivity,
                R.drawable.default_option_border_bg
            )
        }
    }

    private fun selectedAlternativeView(option: TextView, index: Int) {
        defaultAlternativesView()
        selectedAlternativeIndex = index

        option.setTextColor(
            Color.parseColor("#363A43")
        )
        option.setTypeface(option.typeface, Typeface.BOLD)
        option.background = ContextCompat.getDrawable(
            this@QuizQuestionsActivity,
            R.drawable.selected_option_border_bg
        )
    }

    private fun answerView(view: TextView, drawableId: Int) {
        view.background = ContextCompat.getDrawable(
            this@QuizQuestionsActivity,
            drawableId
        )
        tvAlternatives!![selectedAlternativeIndex].setTextColor(
            Color.parseColor("#FFFFFF")
        )
    }
}