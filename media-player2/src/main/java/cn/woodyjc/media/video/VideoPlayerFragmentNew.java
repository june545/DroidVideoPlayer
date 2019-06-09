package cn.woodyjc.media.video;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
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
import com.woodyhi.player.base.VideoPlayer;
import com.woodyhi.player.base.view.DefaultControllerView;

import java.lang.ref.WeakReference;

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
    @BindView(R.id.video_player_view) VideoPlayer videoPlayerView;

    @BindView(R.id.default_controller_view) DefaultControllerView controllerView;

    PlayerManger playerManger;

    @BindView(R.id.open_close_fullscreen) ImageView openCloseFullscreenBtn;

    private int systemUiVisibilityWhenPortrait;

    PlaybackInfo playbackInfo;

    static class MyHandler extends Handler {
        static final byte HANDLER_SHOW_CONTROL_BAR = 1;
        static final byte HANDLER_HIDE_CONTROL_BAR = 2;
        static final byte HANDLER_UPDATE_PLAYBACK_PROGRESS = 3;

        WeakReference<VideoPlayerFragmentNew> weakReference;

        MyHandler(VideoPlayerFragmentNew fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            VideoPlayerFragmentNew fragment = weakReference.get();
            if (fragment == null) return;

            switch (msg.what) {
                case HANDLER_SHOW_CONTROL_BAR:

                    break;
                case HANDLER_HIDE_CONTROL_BAR:
//                    fragment.hideControlView();

                    break;
                case HANDLER_UPDATE_PLAYBACK_PROGRESS:
//                    sendEmptyMessageDelayed(HANDLER_UPDATE_PLAYBACK_PROGRESS, 1000 - fragment.lastPosition % 1000);
                    break;
                default:
                    break;
            }
        }
    }

    ;

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
        controllerView = view.findViewById(R.id.default_controller_view);
        playerManger = videoPlayerView.getPlayerManger();
        if (playbackInfo != null) {
            playerManger.setPlaybackInfo(playbackInfo);
            playerManger.playback();
        }
        controllerView.setPlayerManger(playerManger);
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
        Log.d(TAG, "onPause() ");
/*
        if (duration - lastPosition < 5000) {// almost completed
            lastPosition = 0;
        } else {
            // currentPosition -= 3000;// go back few seconds, to compensate player_loading time
        }
        */
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
            playerManger.setPlaybackInfo(info);
            playerManger.playback();
        } else {
            this.playbackInfo = info;
        }
    }

}
