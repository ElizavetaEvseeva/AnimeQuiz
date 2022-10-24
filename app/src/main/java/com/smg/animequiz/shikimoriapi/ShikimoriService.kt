package com.smg.animequiz.shikimoriapi
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.smg.animequiz.MainActivity
import com.smg.animequiz.models.Anime
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

class ShikimoriService {

    private var jsonContents: String? = null

    val jsonContent get() = jsonContents

    val allAnimeTitles: ArrayList<Anime> = ArrayList()

    public fun getMainJsonString(count: Int,year: Int, context: Context){

        val yearParam = when(year){
            1990 -> "1990_1999"
            2000 -> "2000_2009"
            2010 -> "2010_2017"
            2018 -> "2018_2022"
            else -> { "2018_2022" }
        }

        val queue = Volley.newRequestQueue(context)

        val link = "https://shikimori.one/api/animes?kind=tv,movie&score=7&order=random&limit=$count&season=$yearParam"
        val stringRequest = object: StringRequest(
            Request.Method.GET, link,
            Response.Listener<String> { response ->
                jsonContents = response
                this.parseJsonData()
            },
            Response.ErrorListener {  })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "node-shikimori"
                return headers
            }
        }
        queue.add(stringRequest)
    }

    public fun getAnimeScreenshot(link: String): Bitmap?{

        return null

    }

    public fun parseJsonData(): Boolean{
        if (jsonContent == null){
            Log.e("QUIZ_ERROR", "ERROR: jsonContent is empty")
            return false
        }

        val content = JSONArray(jsonContent)
        if (content.length() == 0){
            Log.e("QUIZ_ERROR", "ERROR: jsonContent is not valid")
            return false
        }

        for (i in 0..content.length()){
            val anime = content.getJSONObject(i)
            val title = anime.getString("russian")
            val link = anime.getString("url")
            allAnimeTitles.add(Anime(title, link))
        }
        return true
    }

    private class ScreenShotLoader(val totalCount: Int){
        var count: Int = 0
        fun load(): Boolean {
            count++
            return count >= totalCount
        }
    }
    private var screenShotLoader: ScreenShotLoader? = null


    public fun getAnimeScreenshotLinks(animes: ArrayList<Anime>, context: Context) {

        val queue = Volley.newRequestQueue(context)

        screenShotLoader = ScreenShotLoader(animes.count())

        for (anime in animes){
            val link = "https://shikimori.one/api${anime.link}"
            val stringRequest = object: StringRequest(
                Request.Method.GET, link,
                Response.Listener<String> { response ->
                    val linkArray = JSONObject(response).getJSONArray("screenshots")
                    if (linkArray.length() == 0){
                        Log.e("QUIZ_ERROR", "Error while trying to get screenshot link.")
                    } else {
                        anime.screenshotLink = linkArray.getString(Random.nextInt(0, linkArray.length()))
                        if (screenShotLoader!!.load()){
                            screenShotLoader = null
                            MainActivity.questionBankInitComplete(true)
                        }
                    }
                },
                Response.ErrorListener {  })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["User-Agent"] = "node-shikimori"
                    return headers
                }
            }
            queue.add(stringRequest)
        }
    }
}