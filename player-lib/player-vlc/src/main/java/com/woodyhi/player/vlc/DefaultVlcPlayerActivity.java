package com.woodyhi.player.vlc;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.PlayInfo;
import com.woodyhi.player.internal.DefaultControllerView;
import com.woodyhi.player.widget.SurfaceViewPlayer;
import com.woodyhi.player.widget.TextureViewPlayer;

public class DefaultVlcPlayerActivity extends AppCompatActivity {

    private AbsPlayerManager playerManager;
    private boolean isLandscape = false;
    private int originalSystemUiVisibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_vlc_player);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//         设置状态半透明
            WindowManager.LayoutParams windowParams = getWindow().getAttributes();
            windowParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//         并在根布局设置android:fitsSystemWindows 或 在代码中设置：
            getWindow().getDecorView().findViewById(android.R.id.content).setFitsSystemWindows(true);
        }

        originalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();

        String path = getIntent().getDataString();
        if (path == null) return;

//        PlayerView playerView1 = findViewById(R.id.playerView);
//        playerManager = new VlcPlayerManager(this);
//        playerView1.setPlayerManager(playerManager);
//        DefaultControllerView controllerView1 = new DefaultControllerView(this);
//        controllerView1.setPlayerManger(playerManager);
//        playerView1.setControllerView(controllerView1);
//        playerManager.play(new PlayInfo(path));


        SurfaceViewPlayer playerView1 = findViewById(R.id.playerView);
        playerManager = new VlcPlayerManager(this);
        playerView1.setPlayerManager(playerManager);
        DefaultControllerView controllerView1 = new DefaultControllerView(this);
        controllerView1.setPlayerManger(playerManager);
        playerView1.setControllerView(controllerView1);
        playerManager.play(new PlayInfo(path));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                getWindow().getDecorView().setVisibility(originalSystemUiVisibility);
                isLandscape = false;
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
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (playerManager != null)
            playerManager.play();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (playerManager != null)
            playerManager.pause();
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
