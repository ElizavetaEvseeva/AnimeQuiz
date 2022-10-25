package com.smg.animequiz

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.smg.animequiz.quiz.QuestionBank
import com.smg.animequiz.shikimoriapi.ShikimoriService

const val QUESTION_COUNT = 10
const val LOG_TAG="quiz_log"

class MainActivity : AppCompatActivity() {

    init {
        instance = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
        hideSystemBars()
        //setupActionBarWithNavController(findNavController(R.id.fragmentContainerView))
    }

    private fun hideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }



    companion object{

        private var instance: MainActivity? = null
        val getInstance get() = instance

        var dbHelper: DBHelper? = null

        fun getContext(): Context?{
            return instance!!.applicationContext
        }

        private fun initComponents(){
            var c = getContext()
            if (c != null) {
                dbHelper = DBHelper(c, null)
            }
        }
    }
}