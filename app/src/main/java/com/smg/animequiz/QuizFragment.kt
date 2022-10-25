package com.smg.animequiz

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.smg.animequiz.quiz.Question
import com.smg.animequiz.quiz.QuestionBank
import com.squareup.picasso.Picasso

class QuizFragment : Fragment() {


    private lateinit var buttonA: Button
    private lateinit var buttonB: Button
    private lateinit var buttonC: Button
    private lateinit var buttonD: Button
    private lateinit var textProgress: TextView
    private lateinit var buttonNext: Button
    private lateinit var buttonAbout: Button
    private lateinit var quizImage: ImageView


    private lateinit var navController: NavController

    private var year: Int = 0

    private var nextQuestionIndex = 0

    private var currentQuestion: Question? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            year = it.getInt("year")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }


    companion object { }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        val periodString = when (year){
            1990 -> "1990-1999"
            2000 -> "2000-2009"
            2010 -> "2010-2018"
            2018 -> "2018+"
            else -> {
                "1990-1999"
            }
        }

        view.findViewById<TextView>(R.id.idTextViewPeriod).text = "Период " + periodString

        textProgress = view.findViewById<TextView>(R.id.idTextViewProgress)

        buttonA = view.findViewById(R.id.idButtonAnswer1)
        buttonB = view.findViewById(R.id.idButtonAnswer2)
        buttonC = view.findViewById(R.id.idButtonAnswer3)
        buttonD = view.findViewById(R.id.idButtonAnswer4)

        buttonA.setOnClickListener { buttonAnswerClick(buttonA) }
        buttonB.setOnClickListener { buttonAnswerClick(buttonB) }
        buttonC.setOnClickListener { buttonAnswerClick(buttonC) }
        buttonD.setOnClickListener { buttonAnswerClick(buttonD) }


        view.findViewById<Button>(R.id.idButtonNext).setOnClickListener { buttonNextClick() }

         view.findViewById<Button>(R.id.idButtonAbout).setOnClickListener { buttonAboutClick()  }

        quizImage = view.findViewById(R.id.idImageMainQuiz)

        MainActivity.startQuizSession(this, year)
    }

    fun run(){
        setNextQuestion()
    }

    private fun buttonNextClick(){
        if (nextQuestionIndex > QUESTION_COUNT){
            val b = Bundle()
            b.putInt("correct_count", MainActivity.gameState.correctCount)
            this.navController.navigate(R.id.action_quizFragment2_to_completeFragment, b)
        } else {
            setNextQuestion()
        }
    }

    private fun buttonAboutClick(){
        val b = Bundle()
        b.putString("anime_link", currentQuestion!!.answer.link)
        this.navController.navigate(R.id.action_quizFragment2_to_aboutAnimeFragment, b)
    }

    private fun buttonAnswerClick(answerButton: Button){

        if (MainActivity.gameState.state != State.WAITING_INPUT) return

        var correct: Boolean = false
        @ColorRes var color: Int
        if (answerButton.text == currentQuestion!!.answer.title){
            color = R.color.teal_700
            correct = true
        } else {
            color = R.color.black
        }
        answerButton.setBackgroundColor( ContextCompat.getColor(MainActivity.context, color))

        if (correct) MainActivity.gameState.correctCount++
        nextQuestionIndex++
    }


    private fun setNextQuestion(){

        val q = MainActivity.questionBank.questions[nextQuestionIndex]

        buttonA.text = q.options[0].title
        buttonB.text = q.options[1].title
        buttonC.text = q.options[2].title
        buttonD.text = q.options[3].title

        MainActivity.shikimoriService.loadPictureIntoView(quizImage, q.answer.screenshotLink!!)

        textProgress.text = "$nextQuestionIndex/$QUESTION_COUNT"

        currentQuestion = q

    }
}