package cn.woodyjc.media.video;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.ijk.IjkPlayerFragment;

public class DetailsVlcActivity extends AppCompatActivity {
    String path = "http://vfx.mtime.cn/Video/2019/05/21/mp4/190521101629869012.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_vlc);

        IjkPlayerFragment fragment = new IjkPlayerFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.player_container, fragment)
                .commit();

        fragment.playback(new PlaybackInfo(path));
    }
}
