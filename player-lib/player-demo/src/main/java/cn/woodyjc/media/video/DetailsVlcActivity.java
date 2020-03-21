package cn.woodyjc.media.video;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.PlayInfo;
import com.woodyhi.player.internal.DefaultControllerView;
import com.woodyhi.player.vlc.VlcPlayerManager;
import com.woodyhi.player.widget.PlayerView;

public class DetailsVlcActivity extends AppCompatActivity {

    AbsPlayerManager manager1;
    AbsPlayerManager manager2;
    AbsPlayerManager manager3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_vlc);

        PlayerView playerView1 = findViewById(R.id.video_player_view_1);
        manager1 = new VlcPlayerManager(this);
        playerView1.setPlayerManager(manager1);
        DefaultControllerView controllerView1 = new DefaultControllerView(this);
        controllerView1.setPlayerManger(manager1);
        playerView1.setControllerView(controllerView1);
        manager1.play(new PlayInfo(VideoPath.https));

//        PlayerView playerView2 = findViewById(R.id.video_player_view_2);
//        manager2 = new VlcPlayerManager(this);
//        playerView2.setPlayerManager(manager2);
//        DefaultControllerView controllerView2 = new DefaultControllerView(this);
//        controllerView2.setPlayerManger(manager2);
//        playerView2.setControllerView(controllerView2);
//        manager2.playback(new PlayInfo(VideoPath.rtmp));


//        PlayerView playerView3 = findViewById(R.id.video_player_view_3);
//        manager3 = new VlcPlayerManager(this);
//        playerView3.setPlayerManager(manager3);
//        DefaultControllerView controllerView3 = new DefaultControllerView(this);
//        controllerView3.setPlayerManger(manager3);
//        playerView3.setControllerView(controllerView3);
//        manager3.playback(new PlayInfo(rtsp));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (manager1 != null)
            manager1.release();
        if (manager2 != null)
            manager2.release();
        if (manager3 != null)
            manager3.release();
    }
}
