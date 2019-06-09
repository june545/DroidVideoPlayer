package cn.woodyjc.media.video;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.woodyhi.player.base.PlaybackInfo;

import java.io.File;

public class DetailsActivity extends AppCompatActivity {
//    String path = Uri.fromFile(new File("/sdcard/Download/iceage.3gp")).toString();
//    String path = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";
//    String path = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8";
    String path = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    VideoPlayerFragmentNew videoPlayerFragmentNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


//        AspectRatioFrameLayout aspectRatioLayout = findViewById(R.id.player_container);
//        aspectRatioLayout.setResizeType(AspectRatioFrameLayout.RESIZE_TYPE_16_9);

        videoPlayerFragmentNew = VideoPlayerFragmentNew.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.player_container, videoPlayerFragmentNew)
                .commit();


        videoPlayerFragmentNew.playback(new PlaybackInfo(path));
    }
}
