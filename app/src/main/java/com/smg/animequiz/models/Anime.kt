package com.smg.animequiz.models

data class Anime(
    val title: String,
    val year: Int,
    val link: String,
    val posterLink: String,
    val characters: ArrayList<AnimeCharacter>
)