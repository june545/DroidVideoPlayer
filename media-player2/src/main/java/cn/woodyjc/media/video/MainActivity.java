package cn.woodyjc.media.video;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.woodyjc.media.view.AspectRatioLayout;
import cn.woodyjc.media.video.player.VideoPlayerFragment;

public class MainActivity extends AppCompatActivity {
    String path = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";

    VideoPlayerFragment videoPlayerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoPlayerFragment = (VideoPlayerFragment) getSupportFragmentManager().findFragmentById(R.id.video_payer_fragment);
        videoPlayerFragment.play(path);

//        AspectRatioLayout aspectRatioLayout = findViewById(R.id.aspect_ratio_layout);
//        aspectRatioLayout.setResizeType(AspectRatioLayout.RESIZE_TYPE_16_9);
    }
}
