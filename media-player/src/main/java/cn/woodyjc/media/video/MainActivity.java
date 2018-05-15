package cn.woodyjc.media.video;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import cn.woodyjc.media.video.player.VideoPlayerFragment;

public class MainActivity extends AppCompatActivity {
//    String path = Environment.getExternalStorageDirectory().getPath() + "/test.3gp";
    String path = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";

    VideoPlayerFragment videoPlayerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoPlayerFragment = (VideoPlayerFragment) getSupportFragmentManager().findFragmentById(R.id.video_payer_fragment);
        videoPlayerFragment.play(path);
    }
}
