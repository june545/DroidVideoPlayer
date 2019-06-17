package com.woodyhi.playlist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.woodyhi.playlist.model.Trailer
import com.woodyhi.playlist.model.TrailerListData
import kotlinx.android.synthetic.main.activity_play_list.*

class PlayListActivity : AppCompatActivity() {
    val TAG = PlayListActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_list)


        val content: String? = assets.open("trailers.txt").use { it.bufferedReader().use { it.readText() } }
        Log.d(TAG, content)

        val trailerListData: TrailerListData? = Gson().fromJson(content, object : TypeToken<TrailerListData>() {}.type)

        for (trailer: Trailer in trailerListData?.trailers!!) {
            println(trailer.movieName)
        }


        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = MyAdapter(trailerListData.trailers)

    }


}
