package com.woodyhi.playlist

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.woodyhi.playlist.api.ApiManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_play_list.*

class TrailersListActivity : AppCompatActivity() {
    val TAG = TrailersListActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_list)

//        本地数据
//        val content: String? = assets.open("trailers.json").use { it.bufferedReader().use { it.readText() } }
//        Log.d(TAG, content)
//        val trailerListData: TrailerListData? = Gson().fromJson(content, object : TypeToken<TrailerListData>() {}.type)
//        for (trailer: Trailer in trailerListData?.trailers!!) {
//            println(trailer.movieName)
//        }

        recycler_view.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this@TrailersListActivity, 2)
        recycler_view.addItemDecoration(object : ItemDecoration() {
            var space = 8
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;
                outRect.top = space;
            }
        })
//        recycler_view.adapter = MyAdapter(trailerListData.trailers)

        val disposable: Disposable? =
                ApiManager.instance
                        .apiService
                        ?.getTrailersData()
                        ?.subscribeOn(Schedulers.io())
                        ?.map { it.trailers }
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe {
                            recycler_view.adapter = MyAdapter(it)
                        }


    }


}
