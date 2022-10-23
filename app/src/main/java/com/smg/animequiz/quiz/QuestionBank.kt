package com.smg.animequiz.quiz

import com.smg.animequiz.models.Anime
import com.smg.animequiz.models.AnimeCharacter
import kotlin.random.Random

class QuestionBank(allAnime: ArrayList<Anime>, chars: ArrayList<AnimeCharacter>, count: Int) {

    public val questions: ArrayList<Question> = ArrayList()
    init {
        val rndList = ArrayList<Int>()

        for (i in 1..count){
            var nextInt = Random.nextInt(0, chars.count())
            while (rndList.contains(nextInt)){
                nextInt = Random.nextInt(0, chars.count())
            }
            rndList.add(nextInt)
        }
        rndList.forEach{
            val rndAnime = getRandomAnimeForChar(chars[it], allAnime)
            questions.add(
                Question(chars[it],
                    rndAnime,
                    getRandomAnimes(rndAnime, 3, allAnime)
                )
            )
        }
    }

    private fun getRandomAnimeForChar(char: AnimeCharacter, sourceList: ArrayList<Anime>): Anime{
        var anime = sourceList[Random.nextInt(0, sourceList.count())]
        while(char.anime == anime){
            anime = sourceList[Random.nextInt(0, sourceList.count())]
        }
        return anime
    }

    private fun getRandomAnimes(anime: Anime, count: Int, sourseList: ArrayList<Anime>): ArrayList<Anime>{
        var animes = ArrayList<Anime>()
        var rndAnime = sourseList[Random.nextInt(0, sourseList.count())]
        for (i in 1..(count + 1)){
            while (rndAnime == anime || animes.contains(rndAnime)){
                rndAnime = sourseList[Random.nextInt(0, sourseList.count())]
            }
        }
        animes[Random.nextInt(0, animes.count())] = anime
        return animes
    }
}