package cn.woodyjc.media.video;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.PlayInfo;
import com.woodyhi.player.internal.SimplePlayerView;

import cn.woodyjc.media.video.databinding.FragmentVideoPlayerNewBinding;


/**
 * @author June
 */
public class VideoPlayerFragmentNew extends Fragment {
    private final String TAG = VideoPlayerFragmentNew.class.getSimpleName();

    //  延迟时间
    private static final int AUTO_HIDE_CONTROL_DELAY_TIME = 10000;

    private Context mContext;
    private View rootView;
    FragmentVideoPlayerNewBinding viewBinding;

    SimplePlayerView videoPlayerView;
    View controllerView;

    AbsPlayerManager playerManger;
    PlayInfo playInfo;

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
        viewBinding = FragmentVideoPlayerNewBinding.inflate(inflater);
        rootView = viewBinding.getRoot();
        videoPlayerView = viewBinding.videoPlayerView;
        initViews(rootView);
        return rootView;
    }

    private void initViews(View view) {
        controllerView = videoPlayerView.getControllerView();
        playerManger = videoPlayerView.getPlayerManager();
        playerManger.play(playInfo);
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
        if (playerManger != null)
            playerManger.release();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "fragment.onConfigurationChanged:  " + newConfig);
        super.onConfigurationChanged(newConfig);
        WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                // 设置非全屏，显示状态栏
//                params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                ((Activity) mContext).getWindow().setAttributes(params);
//                ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

                ((Activity) mContext).getWindow().getDecorView().setSystemUiVisibility(systemUiVisibilityWhenPortrait);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                // 设置全屏,隐藏状态栏
//                params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//                ((Activity) mContext).getWindow().setAttributes(params);
//                ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

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
    public void playback(PlayInfo info) {
        if (playerManger != null) {
            playerManger.play(info);
        } else {
            this.playInfo = info;
        }
    }

}
