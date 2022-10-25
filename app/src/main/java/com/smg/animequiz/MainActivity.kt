package com.smg.animequiz

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.smg.animequiz.quiz.QuestionBank
import com.smg.animequiz.shikimoriapi.ShikimoriService

const val QUESTION_COUNT = 10

class MainActivity : AppCompatActivity() {

    init {
        instance = this
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBarWithNavController(findNavController(R.id.fragmentContainerView))
    }

    private var quizFragment: QuizFragment? = null

    companion object{

        private var instance: MainActivity? = null

        public val shikimoriService = ShikimoriService()
        public val questionBank = QuestionBank()

        public val gameState = GameState()

        val dbHelper: DBHelper = DBHelper(context, null)

        val context get() = instance!!.applicationContext


        public val getInstance get() = instance


        public fun startQuizSession(fragment: QuizFragment, year: Int){

            gameState.state = State.LOADING
            shikimoriService.getMainJsonString(QUESTION_COUNT, year, instance!!.applicationContext )
        }


        public fun dataParsedCallback(success: Boolean){
            if (!success){
                Log.e("QUIZ_ERROR", "Error in shikimori data parsing")
                return
            }
            initQuestionBank()
        }

        private fun initQuestionBank(){
            questionBank.generateQuestions(shikimoriService.allAnimeTitles, QUESTION_COUNT)
        }
        fun questionBankInitComplete(success: Boolean){
            if (!success){
                Log.e("QUIZ_ERROR", "Failed to load questions")
                return
            }
            gameState.state = State.WAITING_INPUT


            val quizFragment = instance!!
                .findViewById<FragmentContainerView>(R.id.fragmentContainerView)
                .getFragment<QuizFragment>()
            quizFragment.run()
        }
    }
}