package com.woodyhi.playlist.api

import com.woodyhi.playlist.model.TrailerListData
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * @auth June
 * @date 2019/06/18
 */
interface ApiService {
    @GET("PageSubArea/TrailerList.api")
    fun getTrailersData(): Observable<TrailerListData>
}