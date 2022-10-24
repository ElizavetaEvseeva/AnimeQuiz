package com.smg.animequiz.quiz

import com.smg.animequiz.MainActivity
import com.smg.animequiz.models.Anime
import com.smg.animequiz.models.AnimeCharacter
import kotlin.random.Random

class QuestionBank() {

    val questions: ArrayList<Question> = ArrayList()

    fun generateQuestions(allAnime: ArrayList<Anime>, count: Int){

        questions.clear()

        val rndList = ArrayList<Int>()

        for (i in 1..count){
            var nextInt = Random.nextInt(0, allAnime.count())
            while (rndList.contains(nextInt)){
                nextInt = Random.nextInt(0, allAnime.count())
            }
            rndList.add(nextInt)
        }
        val animesToLoadScreenshots = ArrayList<Anime>()
        rndList.forEach{
            animesToLoadScreenshots.add(allAnime[it])
            questions.add(
                Question(
                    allAnime[it],
                    getRandomAnimes(allAnime[it], 3, allAnime)
                )
            )
        }
        MainActivity.shikimoriService.getAnimeScreenshotLinks(animesToLoadScreenshots, MainActivity.context)
    }

    private fun getRandomAnimes(anime: Anime, count: Int, sourseList: ArrayList<Anime>): ArrayList<Anime>{
        val animes = ArrayList<Anime>()
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
//private fun getRandomAnimeForChar(char: AnimeCharacter, sourceList: ArrayList<Anime>): Anime{
//    var anime = sourceList[Random.nextInt(0, sourceList.count())]
//    while(char.anime == anime){
//        anime = sourceList[Random.nextInt(0, sourceList.count())]
//    }
//    return anime
//}
