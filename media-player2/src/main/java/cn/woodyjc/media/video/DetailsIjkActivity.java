package cn.woodyjc.media.video;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.ijk.IjkPlayerFragment;
import com.woodyhi.player.ijk.IjkPlayerView;
import com.woodyhi.player.internal.DefaultControllerView;

public class DetailsIjkActivity extends AppCompatActivity {
    String path = "http://vfx.mtime.cn/Video/2019/05/21/mp4/190521101629869012.mp4";
    String rtmp = "rtmp://live.hkstv.hk.lxdns.com/live/hks1";
    String rtsp = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";

    AbsPlayerManager manager2;
    AbsPlayerManager manager3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_ijk);


        IjkPlayerFragment fragment = new IjkPlayerFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.player_container, fragment)
                .commit();

        fragment.playback(new PlaybackInfo(path));


        IjkPlayerView playerView1 = findViewById(R.id.video_player_view_1);
        manager2 = playerView1.getPlayerManger();
        DefaultControllerView controllerView2 = new DefaultControllerView(this);
        controllerView2.setPlayerManger(manager2);
        manager2.playback(new PlaybackInfo(rtmp));

        IjkPlayerView playerView2 = findViewById(R.id.video_player_view_2);
        manager3 = playerView2.getPlayerManger();
        DefaultControllerView controllerView3 = new DefaultControllerView(this);
        controllerView3.setPlayerManger(manager3);
        manager3.playback(new PlaybackInfo(rtsp));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (manager2 != null)
            manager2.release();
        if (manager3 != null)
            manager3.release();
    }
}
