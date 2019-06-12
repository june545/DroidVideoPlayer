package cn.woodyjc.media.video;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.internal.DefaultControllerView;
import com.woodyhi.player.vlc.VlcControllerView;
import com.woodyhi.player.vlc.VlcPlayerManager;
import com.woodyhi.player.vlc.VlcPlayerView;
import com.woodyhi.player.widget.PlayerView;

public class DetailsVlcActivity extends AppCompatActivity {
    String path = "http://vfx.mtime.cn/Video/2019/05/21/mp4/190521101629869012.mp4";

    String path2 = "rtmp://live.hkstv.hk.lxdns.com/live/hks1";
    String rtsp = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";

    AbsPlayerManager manager1;

    AbsPlayerManager manager3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_vlc);

        VlcPlayerView playerView1 = findViewById(R.id.video_player_view_1);
        manager1 = playerView1.getPlayerManger();
        manager1.playback(new PlaybackInfo(rtsp));

        VlcPlayerView playerView2 = findViewById(R.id.video_player_view_2);
//        playerView2.getPlayerManger().playback(new PlaybackInfo(path2));

        PlayerView playerView = findViewById(R.id.video_player_view_3);
        manager3 = new VlcPlayerManager();
        playerView.setPlayerManager(manager3);
        VlcControllerView controllerView = new VlcControllerView(this);
        controllerView.setPlayerManger(manager3);
        playerView.setControllerView(controllerView);
        manager3.playback(new PlaybackInfo(rtsp));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager1.release();
        manager3.release();
    }
}
