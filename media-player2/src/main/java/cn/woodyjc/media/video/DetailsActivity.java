package cn.woodyjc.media.video;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.internal.SimplePlayerView;

public class DetailsActivity extends AppCompatActivity {
    //    String path = Uri.fromFile(new File("/sdcard/Download/iceage.3gp")).toString();
//    String path = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";
//    String path = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8";
//    String path = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    String path = "http://vfx.mtime.cn/Video/2019/05/21/mp4/190521101629869012.mp4";
//    String path = "rtmp://live.hkstv.hk.lxdns.com/live/hks1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // 设置状态栏透明 并在根布局设置android:fitsSystemWindows
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams windowParams = getWindow().getAttributes();
            windowParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            // 代码设置
            getWindow().getDecorView().findViewById(android.R.id.content).setFitsSystemWindows(true);
        }


        VideoPlayerFragmentNew videoPlayerFragmentNew = VideoPlayerFragmentNew.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.player_container, videoPlayerFragmentNew)
                .commit();

        videoPlayerFragmentNew.playback(new PlaybackInfo(path));


        SimplePlayerView simplePlayerView1 = findViewById(R.id.video_player_view_1);
        simplePlayerView1.getPlayerManger().playback(new PlaybackInfo(path));
        SimplePlayerView simplePlayerView2 = findViewById(R.id.video_player_view_2);
        simplePlayerView2.getPlayerManger().playback(new PlaybackInfo(path));
    }
}