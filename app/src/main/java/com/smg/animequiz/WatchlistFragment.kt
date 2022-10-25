package com.smg.animequiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smg.animequiz.models.TitleRVModel


class WatchlistFragment : Fragment() {

    lateinit var titlesRV: RecyclerView
    lateinit var adapter: TitleRVAdapter
    val titlesList = ArrayList<TitleRVModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titlesRV = view.findViewById(R.id.idRVAnimeTitles)
        adapter = TitleRVAdapter(titlesList)
        readTitlesFromDB()
        adapter.notifyDataSetChanged()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watchlist, container, false)
    }


    private fun readTitlesFromDB(){

        val cursor = MainActivity.dbHelper?.getTitle()

        cursor!!.moveToFirst()

        var index = cursor.getColumnIndex(DBHelper.NAME_COL)
        var name = ""
        if(index >= 0){
            name = cursor.getString(index)
        }
        var link = ""
        index = cursor.getColumnIndex(DBHelper.LINK_COL)
        if(index >= 0){
            link = cursor.getString(index)
        }
        var posterLink = ""
        index = cursor.getColumnIndex(DBHelper.POSTER_LINK_COL)
        if(index >= 0){
            posterLink = cursor.getString(index)
        }
        titlesList.add(TitleRVModel(name, link, posterLink))

        while(cursor.moveToFirst()){
            var index = cursor.getColumnIndex(DBHelper.NAME_COL)
            var name = ""
            if(index >= 0){
                name = cursor.getString(index)
            }
            var link = ""
            index = cursor.getColumnIndex(DBHelper.LINK_COL)
            if(index >= 0){
                link = cursor.getString(index)
            }
            var posterLink = ""
            index = cursor.getColumnIndex(DBHelper.POSTER_LINK_COL)
            if(index >= 0){
                posterLink = cursor.getString(index)
            }
        }
        cursor.close()
    }
}