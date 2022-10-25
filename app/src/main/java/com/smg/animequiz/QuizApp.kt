package com.smg.animequiz

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentContainerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.smg.animequiz.models.Anime
import com.smg.animequiz.quiz.QuestionBank
import com.smg.animequiz.shikimoriapi.ShikimoriService


class QuizApp: Application() {

    private lateinit var requestQueue: RequestQueue
    private lateinit var shikimoriService : ShikimoriService
    var gameState: GameState = GameState()
    val questionBank = QuestionBank()

    public val getGameState get() = gameState
    public val getShikimoriService get() = shikimoriService
    public val getQuestionBank get() = questionBank

    override fun onCreate() {
        Log.d(LOG_TAG, "APP CREATED")
        super.onCreate()
        instance = this

        requestQueue = Volley.newRequestQueue(applicationContext)
        shikimoriService = ShikimoriService()
    }

    companion object {
        lateinit var instance: QuizApp
            private set
    }

    fun getRequestQueue(): RequestQueue {
        return requestQueue
    }

    public fun startQuizSession(year: Int){
        Log.d(LOG_TAG, "Starting quiz session from main activity")
        gameState = GameState()
        gameState.state = State.LOADING
        //shikimoriService.getMainJsonString(QUESTION_COUNT, year, getRequestQueue()) {
        shikimoriService.getTestMainJsonString(QUESTION_COUNT, year, getRequestQueue()) {
            Log.d(LOG_TAG, "GOT MAIN STRING, parsing")
            dataParsedCallback(it)
        }
        //shikimoriService.getWeatherData(getRequestQueue()) {
        //    Log.d(LOG_TAG, "GOT WEATHER")
        //    //dataParsedCallback(it)
        //}
        Log.d(LOG_TAG, "COMPLETING START QUIZ SESSION")
    }

    private fun dataParsedCallback(success: Boolean){
        if (!success){
            Log.e("QUIZ_ERROR", "Error in shikimori data parsing")
            return
        }

        //questionBank.generateQuestions(shikimoriService.allAnimeTitles, QUESTION_COUNT)
        questionBank.generateTestQuestions(shikimoriService.allAnimeTitles, QUESTION_COUNT)

        val animesToLoadScreenShots = ArrayList<Anime>()
        questionBank.questions.forEach{
            animesToLoadScreenShots.add(it.answer)
        }
        //shikimoriService.getAnimeScreenshotLinks(animesToLoadScreenShots, getRequestQueue()){
        //    questionBankInitComplete(it)
        //}
        shikimoriService.getAnimeScreenshotLinksTest(animesToLoadScreenShots, getRequestQueue()){
            questionBankInitComplete(it)
        }
    }

    fun questionBankInitComplete(success: Boolean){
        if (!success){
            Log.e("QUIZ_ERROR", "Failed to load questions")
            return
        }
        gameState.state = State.WAITING_INPUT
        Log.d(LOG_TAG, "QUESTING BANK INIT COMPLETE")

        val quizFragment = MainActivity.getInstance!!
            .findViewById<FragmentContainerView>(R.id.fragmentContainerView)
            .getFragment<QuizFragment>()

        quizFragment.run()
    }
}
