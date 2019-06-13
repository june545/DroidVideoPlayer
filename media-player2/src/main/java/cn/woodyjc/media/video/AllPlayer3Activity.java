package cn.woodyjc.media.video;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.internal.DefaultControllerView;
import com.woodyhi.player.vlc.VlcPlayerManager;
import com.woodyhi.player.widget.PlayerView;

public class AllPlayer3Activity extends AppCompatActivity {

    String path = "http://vfx.mtime.cn/Video/2019/05/21/mp4/190521101629869012.mp4";

    AbsPlayerManager mpMgr;
    AbsPlayerManager ijkMgr;
    AbsPlayerManager vlcMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_player3);

        initMediaPlayer();
        initIjk();
        initVlc();
    }

    private void initMediaPlayer() {
        PlayerView mpPlayerView = findViewById(R.id.mediaplayer_view);
        mpMgr = new VlcPlayerManager(this);
        mpPlayerView.setPlayerManager(mpMgr);
        DefaultControllerView mpControllerView = new DefaultControllerView(this);
        mpControllerView.setPlayerManger(mpMgr);
        mpPlayerView.setControllerView(mpControllerView);
        mpMgr.playback(new PlaybackInfo(path));
    }

    private void initIjk() {
        PlayerView ijkPlayerView = findViewById(R.id.ijkplayer_view);
        ijkMgr = new VlcPlayerManager(this);
        ijkPlayerView.setPlayerManager(ijkMgr);
        DefaultControllerView ijkControllerView = new DefaultControllerView(this);
        ijkControllerView.setPlayerManger(ijkMgr);
        ijkPlayerView.setControllerView(ijkControllerView);
        ijkMgr.playback(new PlaybackInfo(path));
    }

    private void initVlc() {
        PlayerView vlcPlayerView = findViewById(R.id.vlcplayer_view);
        vlcMgr = new VlcPlayerManager(this);
        vlcPlayerView.setPlayerManager(vlcMgr);
        DefaultControllerView vlcControllerView = new DefaultControllerView(this);
        vlcControllerView.setPlayerManger(vlcMgr);
        vlcPlayerView.setControllerView(vlcControllerView);
        vlcMgr.playback(new PlaybackInfo(path));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mpMgr != null) {
            mpMgr.release();
        }
        if (ijkMgr != null) {
            ijkMgr.release();
        }
        if (vlcMgr != null) {
            vlcMgr.release();
        }
    }
}
