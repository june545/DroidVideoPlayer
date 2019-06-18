package cn.woodyjc.media.video;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.widget.PlayerView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
        // 设置状态半透明
//            WindowManager.LayoutParams windowParams = getWindow().getAttributes();
//            windowParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        // 并在根布局设置android:fitsSystemWindows 或 在代码中设置：
//            getWindow().getDecorView().findViewById(android.R.id.content).setFitsSystemWindows(true);
//        }

        // 设置状态栏全透明
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        VideoPlayerFragmentNew videoPlayerFragmentNew = VideoPlayerFragmentNew.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.player_container, videoPlayerFragmentNew)
                .commit();

        videoPlayerFragmentNew.playback(new PlaybackInfo(VideoPath.https));


        PlayerView playerView2 = findViewById(R.id.video_player_view_2);

        PlayerView playerView3 = findViewById(R.id.video_player_view_3);
    }
}
