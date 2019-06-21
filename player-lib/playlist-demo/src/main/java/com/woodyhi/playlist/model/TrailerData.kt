package com.woodyhi.playlist.model

/**
 * @auth June
 * @date 2019/06/13
 */

data class Trailer(
        var id: Int,
        var movieName: String,
        var coverImg: String,
        var moviId: Int,
        var url: String,
        var hightUrl: String,
        var videoTitle: String,
        var videoLength: Int,
        var rating: Float,
        var type: Array<String>,
        var summary: String
)

data class TrailerListData(val trailers: List<Trailer>)
