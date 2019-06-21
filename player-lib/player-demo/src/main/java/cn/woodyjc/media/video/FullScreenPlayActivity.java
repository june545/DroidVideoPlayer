package cn.woodyjc.media.video;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.PlayInfo;
import com.woodyhi.player.internal.DefaultControllerView;
import com.woodyhi.player.vlc.DefaultVlcPlayerActivity;
import com.woodyhi.player.vlc.VlcPlayerManager;
import com.woodyhi.player.widget.PlayerView;

public class FullScreenPlayActivity extends AppCompatActivity {
    private final String TAG = FullScreenPlayActivity.class.getSimpleName();

    String path = "http://vfx.mtime.cn/Video/2019/05/21/mp4/190521101629869012.mp4";


    AbsPlayerManager mpMgr;
    PlayerView mpPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_play);

        Intent intent = new Intent(this, DefaultVlcPlayerActivity.class);
        intent.setData(Uri.parse("http://vfx.mtime.cn/Video/2019/06/15/mp4/190615103827358781.mp4"));
        startActivity(intent);
        finish();
//        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mpPlayerView = findViewById(R.id.player_view);
        mpMgr = new VlcPlayerManager(this);
        mpPlayerView.setPlayerManager(mpMgr);
        DefaultControllerView mpControllerView = new DefaultControllerView(this);
        mpControllerView.setPlayerManger(mpMgr);
        mpPlayerView.setControllerView(mpControllerView);
        mpMgr.play(new PlayInfo(path));
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "fragment.onConfigurationChanged:  " + newConfig);
        super.onConfigurationChanged(newConfig);
        Activity activity = this;
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                // 设置非全屏，显示状态栏
                params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                activity.getWindow().setAttributes(params);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                // 设置全屏,隐藏状态栏
                params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                activity.getWindow().setAttributes(params);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

                fullscreen();
                break;
        }
    }

    private void fullscreen() {
        FrameLayout parent = (FrameLayout) mpPlayerView.getParent();
        parent.removeView(mpPlayerView);

        mpPlayerView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                rootView.addView(mpPlayerView);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mpMgr != null)
            mpMgr.release();
    }
}
