package cn.woodyjc.media.video;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.ijk.IjkPlayerFragment;
import com.woodyhi.player.ijk.IjkPlayerView;
import com.woodyhi.player.vlc.VlcPlayerView;

public class DetailsIjkActivity extends AppCompatActivity {
    String path = "http://vfx.mtime.cn/Video/2019/05/21/mp4/190521101629869012.mp4";

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
        playerView1.getPlayerManger().playback(new PlaybackInfo(path));
        IjkPlayerView playerView2 = findViewById(R.id.video_player_view_2);
        playerView2.getPlayerManger().playback(new PlaybackInfo(path));
    }
}
