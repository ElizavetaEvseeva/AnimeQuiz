package com.smg.animequiz

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class QuizFragment : Fragment() {

    private var year: Int? = null

    private var quizCount = 0

    private lateinit var buttonA: Button
    private lateinit var buttonB: Button
    private lateinit var buttonC: Button
    private lateinit var buttonD: Button

    private lateinit var textProgress: TextView

    private lateinit var buttonNext: Button
    private lateinit var buttonAbout: Button

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
        view.findViewById<TextView>(R.id.idTextViewProgress).text = "0/10"

        buttonA = view.findViewById(R.id.idButtonAnswer1)
        buttonB = view.findViewById(R.id.idButtonAnswer2)
        buttonC = view.findViewById(R.id.idButtonAnswer3)
        buttonD = view.findViewById(R.id.idButtonAnswer4)

        buttonNext = view.findViewById(R.id.idButtonNext)
        buttonAbout = view.findViewById(R.id.idButtonAbout)
    }


}