package com.woodyhi.playlist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.woodyhi.playlist.api.ApiManager
import com.woodyhi.playlist.model.Trailer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_play_list.*

class PlayListActivity : AppCompatActivity() {
    val TAG = PlayListActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_list)


//        val content: String? = assets.open("trailers.txt").use { it.bufferedReader().use { it.readText() } }
//        Log.d(TAG, content)
//        val trailerListData: TrailerListData? = Gson().fromJson(content, object : TypeToken<TrailerListData>() {}.type)
//        for (trailer: Trailer in trailerListData?.trailers!!) {
//            println(trailer.movieName)
//        }


//        recycler_view.layoutManager = LinearLayoutManager(this)
//        recycler_view.adapter = MyAdapter(trailerListData.trailers)

        val disposable: Disposable = ApiManager.getInstance(this)
                .apiService
                .trailersData
                .subscribeOn(Schedulers.io())
                .map { t -> t.trailers }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        object : Consumer<List<Trailer>> {
                            override fun accept(t: List<Trailer>?) {
                                recycler_view.layoutManager = LinearLayoutManager(this@PlayListActivity)
                                recycler_view.adapter = t?.let { MyAdapter(it) }
                            }
                        },
                        object : Consumer<Throwable> {
                            override fun accept(t: Throwable?) {
                                t?.printStackTrace()
                            }
                        });

    }


}
