package cn.woodyjc.media.video;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.ijk.IjkPlayerFragment;
import com.woodyhi.player.ijk.IjkPlayerManager;
import com.woodyhi.player.internal.DefaultControllerView;
import com.woodyhi.player.widget.PlayerView;

public class DetailsIjkActivity extends AppCompatActivity {
    //    String path = "http://vfx.mtime.cn/Video/2019/05/21/mp4/190521101629869012.mp4";
    String path = "http://vfx.mtime.cn/Video/2019/06/11/mp4/190611221730282660.mp4";

    String rtmp = "rtmp://live.hkstv.hk.lxdns.com/live/hks1";

    //    String rtmp = "rtmp://live.hkstv.hk.lxdns.com/live/hks1";
    String https = "https://res.exexm.com/cw_145225549855002";
    //    String path = "https://res.exexm.com/cw_145225549855002";
    String rtsp = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";

    AbsPlayerManager mgr1;
    AbsPlayerManager manager2;
    AbsPlayerManager manager3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_ijk);


//        IjkPlayerFragment fragment = new IjkPlayerFragment();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.player_container, fragment)
//                .commit();
//        fragment.playback(new PlaybackInfo(https));


        PlayerView playerView1 = findViewById(R.id.video_player_view_1);
        mgr1 = new IjkPlayerManager();
        playerView1.setPlayerManager(mgr1);
        DefaultControllerView controllerView1 = new DefaultControllerView(this);
        controllerView1.setPlayerManger(mgr1);
        playerView1.setControllerView(controllerView1);
        mgr1.playback(new PlaybackInfo(https));



/*
        PlayerView playerView2 = findViewById(R.id.video_player_view_2);
        manager2 = new IjkPlayerManager();
        playerView2.setPlayerManager(manager2);
        DefaultControllerView controllerView2 = new DefaultControllerView(this);
        controllerView2.setPlayerManger(manager2);
        playerView2.setControllerView(controllerView2);
        manager2.playback(new PlaybackInfo(rtmp));



        PlayerView playerView3 = findViewById(R.id.video_player_view_3);
        manager3 = new IjkPlayerManager();
        playerView3.setPlayerManager(manager3);
        DefaultControllerView controllerView3 = new DefaultControllerView(this);
        controllerView3.setPlayerManger(manager3);
        playerView3.setControllerView(controllerView3);
        manager3.playback(new PlaybackInfo(rtsp));
*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mgr1 != null)
            mgr1.release();
        if (manager2 != null)
            manager2.release();
        if (manager3 != null)
            manager3.release();
    }
}
