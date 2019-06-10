package cn.woodyjc.media.video;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.base.PlayerManger;
import com.woodyhi.player.base.SimplePlayerView;
import com.woodyhi.player.base.view.DefaultControllerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author June
 */
public class VideoPlayerFragmentNew extends Fragment {
    private final String TAG = VideoPlayerFragmentNew.class.getSimpleName();

    //  延迟时间
    private static final int AUTO_HIDE_CONTROL_DELAY_TIME = 10000;

    private Context mContext;
    private View rootView;
    @BindView(R.id.video_player_view) SimplePlayerView videoPlayerView;
    DefaultControllerView controllerView;
    @BindView(R.id.open_close_fullscreen) ImageView openCloseFullscreenBtn;

    PlayerManger playerManger;
    PlaybackInfo playbackInfo;

    private int systemUiVisibilityWhenPortrait;


    public static VideoPlayerFragmentNew newInstance() {
        VideoPlayerFragmentNew videoPlayerFragmentNew = new VideoPlayerFragmentNew();
        return videoPlayerFragmentNew;
    }

    public VideoPlayerFragmentNew() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_video_player_new, container, false);
        ButterKnife.bind(this, rootView);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View view) {
        playerManger = videoPlayerView.getPlayerManger();
        playerManger.playback(playbackInfo);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        systemUiVisibilityWhenPortrait = getActivity().getWindow().getDecorView()
                .getSystemUiVisibility();
        // 获取屏幕尺寸
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "fragment.onConfigurationChanged:  " + newConfig);
        super.onConfigurationChanged(newConfig);
        WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                // 设置非全屏，显示状态栏
                params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                ((Activity) mContext).getWindow().setAttributes(params);
                ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                // 设置全屏,隐藏状态栏
                params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                ((Activity) mContext).getWindow().setAttributes(params);
                ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                break;
        }
    }

    /*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged() " + newConfig.orientation);
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                ((Activity) mContext).getWindow().getDecorView().setSystemUiVisibility(systemUiVisibilityWhenPortrait);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                // 全屏设置
                ((Activity) mContext).getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LOW_PROFILE
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                break;
        }
    }
*/

    /*
    void toggleControllerView() {
        if (playerControlBar.getVisibility() == View.VISIBLE) {
            hideControlView();
        } else {
            showControllerView(true);
        }
    }
    */

    /*
    private void showControllerView(boolean autoHide) {
        if (playerControlBar.getVisibility() == View.GONE) {
            playerControlBar.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.control_bar_show_up));
            playerControlBar.setVisibility(View.VISIBLE);
        }
        if (autoHide) {
            myHandler.removeMessages(MyHandler.HANDLER_HIDE_CONTROL_BAR);
            myHandler.sendEmptyMessageDelayed(MyHandler.HANDLER_HIDE_CONTROL_BAR, AUTO_HIDE_CONTROL_DELAY_TIME);
        }
    }

    private void hideControlView() {
        if (playerControlBar.getVisibility() == View.VISIBLE) {
            playerControlBar.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.control_bar_hide_down));
            playerControlBar.setVisibility(View.GONE);
        }
    }
*/

    /**
     * 待播放视频
     */
    public void playback(PlaybackInfo info) {
        if (playerManger != null) {
            playerManger.playback(info);
        } else {
            this.playbackInfo = info;
        }
    }

}
