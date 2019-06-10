package cn.woodyjc.media.video.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
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
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.woodyjc.media.video.R;
import cn.woodyjc.media.video.service.FloatingViewService;
import cn.woodyjc.media.video.service.PlayerFloatingViewService;
import cn.woodyjc.media.view.VideoPlayerViewNew;

/**
 * @author June
 */
public class VideoPlayerFragment extends Fragment {
    private final String TAG = VideoPlayerFragment.class.getSimpleName();

    //  延迟时间
    private static final int AUTO_HIDE_CONTROL_DELAY_TIME = 10000;

    private Context mContext;
    private View rootView;
    @BindView(R.id.video_player_view) VideoPlayerViewNew videoPlayerView;

    private int systemUiVisibilityWhenPortrait;

    // 音量
    @BindView(R.id.volume_layout) LinearLayout mVolumenLayout;
    @BindView(R.id.volume_percent_tv) TextView mVolumePercent;
    // 快进/快退
    @BindView(R.id.fast_forward_progress_layout) LinearLayout mFastForwardProgressLayout;
    @BindView(R.id.fast_forward_progress_text) TextView mFastForwardProgresText;
    /* player control bar */
    @BindView(R.id.player_control_bar) LinearLayout playerControlBar;
    @BindView(R.id.play_pause_btn) ImageView playPauseBtn;
    @BindView(R.id.seekbar_layout) LinearLayout mSeekBarLayout;
    @BindView(R.id.video_playtime) TextView mCurrentTime;
    @BindView(R.id.seekbar) SeekBar mSeekBar;                                                    // 进度
    @BindView(R.id.video_durationtime) TextView mDurationTime;
    @BindView(R.id.open_close_fullscreen) ImageView openCloseFullscreenBtn;
    @BindView(R.id.show_flotingview) ImageView floatingViewIv;
    private String mMediaPath;
    private boolean playingOnSurface = false;
    private int duration;
    private int lastPosition;
    private boolean mSeeking = false;

    private ProgressTimer progressTimer;
    MyHandler myHandler;

    static class MyHandler extends Handler {
        final byte HANDLER_SHOW_CONTROL_BAR = 1;
        static final byte HANDLER_HIDE_CONTROL_BAR = 2;
        final byte HANDLER_UPDATE_PLAYBACK_PROGRESS = 3;

        WeakReference<VideoPlayerFragment> weakReference;

        MyHandler(VideoPlayerFragment fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            VideoPlayerFragment fragment = weakReference.get();
            if (fragment == null) return;

            switch (msg.what) {
                case HANDLER_SHOW_CONTROL_BAR:

                    break;
                case HANDLER_HIDE_CONTROL_BAR:
                    fragment.hideControlView();

                    break;
                case HANDLER_UPDATE_PLAYBACK_PROGRESS:
                    fragment.updateProgress();
                    sendEmptyMessageDelayed(HANDLER_UPDATE_PLAYBACK_PROGRESS, 1000 - fragment.lastPosition % 1000);
                    break;
                default:
                    break;
            }
        }
    }


    public VideoPlayerFragment() {
    }

    public void setMediaPath(String path) {
        this.mMediaPath = path;
    }

    public void setMediaPath(String path, int playedTime) {
        this.mMediaPath = path;
        this.lastPosition = playedTime;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_video_player, container, false);
        ButterKnife.bind(this, rootView);
        initView(rootView);
        videoPlayerView.play(mMediaPath, 0);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myHandler = new MyHandler(this);

        progressTimer = new ProgressTimer(this);
        progressTimer.setCallback(ms -> updateProgress());
        progressTimer.start();
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

        showControllerView(true);

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        progressTimer.start();
    }

    /**
     * when window lose focus then pause player, or get focus then start player
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d(TAG, "onWindowFocusChanged : " + hasFocus);
        if (hasFocus) {
            if (playingOnSurface && !videoPlayerView.isPlaying()) {
                videoPlayerView.playback();
                playPauseBtn.setBackgroundResource(R.drawable.baseline_pause_circle_outline_white_36);
            }
        } else {
            if (videoPlayerView.isPlaying()) {
                videoPlayerView.pause();
                playPauseBtn.setBackgroundResource(R.drawable.media_play);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() ");
        if (videoPlayerView.isPlaying()) {
            videoPlayerView.pause();
            Log.e(TAG, this.getClass().getName() + " pause, toggle playback button : pause");
            playPauseBtn.setBackgroundResource(R.drawable.media_play);
        }
        if (duration - lastPosition < 5000) {// almost completed
            lastPosition = 0;
        } else {
            // currentPosition -= 3000;// go back few seconds, to compensate player_loading time
        }

        progressTimer.stop();
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
        videoPlayerView.release();
    }

    private void initView(View view) {
        mSeekBar.setOnSeekBarChangeListener(new PlaybackSeekBarChangeListener());
        openCloseFullscreenBtn.setOnClickListener(v -> {
            int orientaion = getResources().getConfiguration().orientation;
            if (orientaion == Configuration.ORIENTATION_PORTRAIT) {
                ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                openCloseFullscreenBtn.setBackgroundResource(R.drawable.fullscreen_exit);
            } else if (orientaion == Configuration.ORIENTATION_LANDSCAPE) {
                ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                openCloseFullscreenBtn.setBackgroundResource(R.drawable.fullscreen_exit);
            }
        });
        floatingViewIv.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PlayerFloatingViewService.class);
            intent.putExtra(FloatingViewService.PARAM_KEY, FloatingViewService.PARAM_VALUE);
            intent.putExtra("MEDIAPATH", mMediaPath);
            getActivity().startService(intent);
            getActivity().finish();
        });
    }

    @OnClick(R.id.play_pause_btn)
    public void playPausePlayer() {
        if (videoPlayerView.isPlaying()) {
            videoPlayerView.pause();
            playingOnSurface = false;
            playPauseBtn.setBackgroundResource(R.drawable.media_play);
        } else {
            videoPlayerView.playback();
            playingOnSurface = true;
            playPauseBtn.setBackgroundResource(R.drawable.baseline_pause_circle_outline_white_36);
        }
        showControllerView(true);
    }

    private int portraitWidth;
    private int portraintHeight;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged() " + newConfig.orientation);
        super.onConfigurationChanged(newConfig);

//        WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();

        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                // 设置非全屏(show taskbar)
//                params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                ((Activity) mContext).getWindow().setAttributes(params);
//                ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

                ((Activity) mContext).getWindow().getDecorView().setSystemUiVisibility(systemUiVisibilityWhenPortrait);

                // change player size
                openCloseFullscreenBtn.setBackgroundResource(R.drawable.fullscreen);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                // 设置全屏(hide taskbar)
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

                // change player size
                openCloseFullscreenBtn.setBackgroundResource(R.drawable.fullscreen_exit);
                break;
        }
    }


    private void bigPlayerLandscape() {
        portraitWidth = videoPlayerView.getWidth();
        portraintHeight = videoPlayerView.getHeight();

        Log.d(TAG, "xxxxx portraintWidth " + portraitWidth + ", portraintHeight " + portraintHeight);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int landscapeWidth = dm.widthPixels;
        int landscapeHeight = dm.heightPixels;

        Log.d(TAG, "xxxxx landscapeWidth " + landscapeWidth + ", landscapeHeight " + landscapeHeight);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) videoPlayerView.getLayoutParams();
        lp.width = landscapeWidth;
        lp.height = landscapeHeight;
        videoPlayerView.setLayoutParams(lp);
        videoPlayerView.requestLayout();
        videoPlayerView.invalidate();
    }


    @OnClick(R.id.video_player_view)
    void toggleControllerView() {
        if (playerControlBar.getVisibility() == View.VISIBLE) {
            hideControlView();
        } else {
            showControllerView(true);
        }
    }

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

    /**
     * update playback progress
     */
    private void updateProgress() {
        if (videoPlayerView.isPlaying()) {
            duration = videoPlayerView.getDuration();
            int currentPosition = videoPlayerView.getCurrentPosition();
            lastPosition = currentPosition;
            Log.v(TAG, "duration=" + duration + ", currentPosition=" + currentPosition);

            if (duration > 0 && currentPosition <= duration) {
                if (!mSeeking) {
                    mSeekBar.setMax(duration);
                    mSeekBar.setProgress(currentPosition);
                }

                mCurrentTime.setText(TimeFormater.formatMillisTime(currentPosition));
                mDurationTime.setText(TimeFormater.formatMillisTime(duration));
            }
            if (duration == 0) {
                mSeekBarLayout.setVisibility(View.INVISIBLE);
            }

            lastPosition = currentPosition;
        }
    }

    private class PlaybackSeekBarChangeListener implements OnSeekBarChangeListener {
        int tmpSeekProgress = 0;

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (tmpSeekProgress >= 0) {
                Log.d(TAG, "seek To " + tmpSeekProgress);
                videoPlayerView.seekTo(tmpSeekProgress);
            }
            mSeeking = false;
            showControllerView(true);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.d(TAG, "onStartTrackingTouch");
            mSeeking = true;
            tmpSeekProgress = 0;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Log.v(TAG, "onProgressChanged  progress=" + progress);
            if (fromUser) {
                tmpSeekProgress = progress;
            }
        }
    }

    public void playback(String path) {
//        videoPlayerView.play(path, 0);
//        mMediaPath = path;
//        lastPosition = 0;
    }
}
