package com.woodyhi.player.vlc;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.internal.DefaultControllerView;
import com.woodyhi.player.widget.PlayerView;

public class DefaultVlcPlayerActivity extends AppCompatActivity {

    private AbsPlayerManager playerManager;
    private DefaultControllerView controllerView1;
    private boolean isLandscape = false;
    private int originalSystemUiVisibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_vlc_player);
        originalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();

        String path = getIntent().getDataString();
        if (path == null) return;

        PlayerView playerView1 = findViewById(R.id.playerView);
        playerManager = new VlcPlayerManager(this);
        playerView1.setPlayerManager(playerManager);
        controllerView1 = new DefaultControllerView(this);
        controllerView1.setPlayerManger(playerManager);
        playerView1.setControllerView(controllerView1);
        playerManager.playback(new PlaybackInfo(path));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                getWindow().getDecorView().setVisibility(originalSystemUiVisibility);
                isLandscape = false;
                controllerView1.configurationChanged(newConfig);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LOW_PROFILE
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                );
                isLandscape = true;
                controllerView1.configurationChanged(newConfig);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playerManager != null)
            playerManager.release();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
