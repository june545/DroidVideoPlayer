package cn.woodyjc.media.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
//    String path = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";
//    String path = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8";
//    String path = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8";
//    String path = "http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void internal(View view) {
        startActivity(new Intent(this, DetailsActivity.class));

    }

    public void ijkplay(View view) {
        startActivity(new Intent(this, DetailsIjkActivity.class));

    }

    public void vlcplay(View view) {
        startActivity(new Intent(this, DetailsVlcActivity.class));

    }

    public void allplay(View view) {
        startActivity(new Intent(this, AllPlayer3Activity.class));
    }

    public void fullscreenplay(View view) {
        startActivity(new Intent(this, FullScreenPlayActivity.class));
    }
}
