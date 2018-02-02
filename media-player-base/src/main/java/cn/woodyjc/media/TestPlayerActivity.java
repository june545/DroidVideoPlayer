package cn.woodyjc.media;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import cn.woodyjc.media.video.VideoPlayerView;

public class TestPlayerActivity extends AppCompatActivity {
//    String path = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";
    String path = Environment.getExternalStorageDirectory().getPath() + "/test.3gp";
    private VideoPlayerView videoPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_player);


        videoPlayerView = findViewById(R.id.media_player_view);

        videoPlayerView.play(path, 0);
    }
}
