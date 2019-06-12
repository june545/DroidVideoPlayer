package cn.woodyjc.media.video;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.vlc.VlcPlayerView;

public class DetailsVlcActivity extends AppCompatActivity {
    String path = "http://vfx.mtime.cn/Video/2019/05/21/mp4/190521101629869012.mp4";

    String path2 = "rtmp://live.hkstv.hk.lxdns.com/live/hks1";
    String rtsp = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";

    AbsPlayerManager manager1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_vlc);

        VlcPlayerView playerView1 = findViewById(R.id.video_player_view_1);
        manager1 = playerView1.getPlayerManger();
        manager1.playback(new PlaybackInfo(rtsp));

//        VlcPlayerView playerView2 = findViewById(R.id.video_player_view_2);
//        playerView2.getPlayerManger().playback(new PlaybackInfo(path2));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager1.release();
    }
}
