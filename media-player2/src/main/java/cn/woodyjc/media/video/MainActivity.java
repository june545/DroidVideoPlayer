package cn.woodyjc.media.video;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.woodyjc.media.video.player.VideoPlayerFragment;
import cn.woodyjc.media.view.AspectRatioLayout;

public class MainActivity extends AppCompatActivity {
//    String path = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";
//    String path = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8";
//    String path = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8";
    String path = "http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8";

    VideoPlayerFragment videoPlayerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AspectRatioLayout aspectRatioLayout = findViewById(R.id.aspect_ratio_layout);
        aspectRatioLayout.setResizeType(AspectRatioLayout.RESIZE_TYPE_16_9);

        videoPlayerFragment = (VideoPlayerFragment) getSupportFragmentManager().findFragmentById(R.id.video_payer_fragment);
        videoPlayerFragment.play(path);

    }
}
