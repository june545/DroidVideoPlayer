package com.woodyhi.playlist;

import androidx.lifecycle.LiveData;

import com.woodyhi.playlist.model.TrailerListData;

import retrofit2.http.GET;

/**
 * @auth June
 * @date 2019/06/18
 */
public interface BackendService {

    @GET("PageSubArea/TrailerList.api")
    LiveData<TrailerListData> getTrailersData();
}
