package com.woodyhi.playlist.model

/**
 *
<pre>
{
  "id": 75071,
  "movieName": "魔幻再起！《冰雪奇缘2》曝正式预告",
  "coverImg": "http://img5.mtime.cn/mg/2019/06/11/202131.80341046_120X90X4.jpg",
  "movieId": 222095,
  "url": "http://vfx.mtime.cn/Video/2019/06/11/mp4/190611221730282660.mp4",
  "hightUrl": "http://vfx.mtime.cn/Video/2019/06/11/mp4/190611221730282660.mp4",
  "videoTitle": "冰雪奇缘2 正式中文预告",
  "videoLength": 79,
  "rating": -1,
  "type": [
    "动画",
    "冒险",
    "喜剧",
    "家庭",
    "奇幻",
    "歌舞"
  ],
  "summary": ""
}
</pre>

 * @auth June
 * @date 2019/06/13
 */
class Trailer {
    var id: Int? = 0
    var movieName: String? = null
    var coverImg: String? = null
    var moviId: Int? = 0
    var url: String? = null
    var hightUrl: String? = null
    var videoTitle: String? = null
    var videoLength: Int? = null
    var rating: Float? = null
    var type: Array<String>? = null
    var summary: String? = null

}

data class TrailerListData(val trailers: List<Trailer>)
