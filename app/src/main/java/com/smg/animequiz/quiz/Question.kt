package com.smg.animequiz.quiz
import com.smg.animequiz.models.*

data class Question(
    val question: AnimeCharacter,
    val answer: Anime,
    val options: ArrayList<Anime>
)