package cn.woodyjc.media.video.player;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import cn.woodyjc.media.video.R;
import cn.woodyjc.media.video.a.GestureSupport;
import cn.woodyjc.media.video.service.FloatingViewService;
import cn.woodyjc.media.video.service.PlayerFloatingViewService;
import cn.woodyjc.media.view.VideoPlayerViewNew;

/**
 * @author June Cheng
 * @date 2015年10月28日 下午10:28:47
 */
public class VideoPlayerFragment extends Fragment {
    private final String TAG = VideoPlayerFragment.class.getSimpleName();

    //  延迟时间
    private static final int HIDE_CONTROL_BAR_DELAY_TIME = 10000;

    private final byte HANDLER_SHOW_CONTROL_BAR = 1;
    private final byte HANDLER_HIDE_CONTROL_BAR = 2;
    private final byte HANDLER_UPDATE_PLAYBACK_PROGRESS = 3;

    //    private int         mPlayerWidth;                                                //播放器尺寸
    //    private int         mPlayerHeight;

    private Context mContext;
    private View rootView;
    private VideoPlayerViewNew videoPlayerView;

    private int systemUiVisibilityWhenPortrait;

    // 尺寸常量
    private int originalFrameWidth;
    private int originalFrameHeight;
    private int mScreenWidth;
    private int mScreenHeight;
    // 音量
    private LinearLayout mVolumenLayout;
    private TextView mVolumePercent;
    // 快进/快退
    private LinearLayout mFastForwardProgressLayout;
    private TextView mFastForwardProgresText;
    /* player control bar */
    private LinearLayout playerControlBar;
    private ImageView playPauseBtn;
    private LinearLayout mSeekBarLayout;
    private TextView mCurrentTime;
    private SeekBar mSeekBar;                                                    // 进度
    private TextView mDurationTime;
    private ImageView openCloseFullscreenBtn;
    private boolean mSeeking = false;
    private ImageView floatingViewIv;
    private String mMediaPath;
    private boolean playingOnSurface = false;
    private int duration;
    private int lastPosition;

    private int positionToSeek;                                            //手势控制播放位置

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HANDLER_SHOW_CONTROL_BAR:

                    break;
                case HANDLER_HIDE_CONTROL_BAR:
                    hideControlView();

                    break;
                case HANDLER_UPDATE_PLAYBACK_PROGRESS:
                    updateProgress();
                    sendEmptyMessageDelayed(HANDLER_UPDATE_PLAYBACK_PROGRESS, 1000 - lastPosition % 1000);
                    break;
                default:
                    break;
            }
        }
    };

    public VideoPlayerFragment() {
    }

    public void setMediaPath(String path) {
        this.mMediaPath = path;
    }

    public void setMediaPath(String path, int playedTime) {
        this.mMediaPath = path;
        this.lastPosition = playedTime;
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_media_player, container, false);
        initView(rootView);
        videoPlayerView.play(mMediaPath, 0);

        return rootView;
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
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

        gesture();

        showControllerView(true);

        mHandler.sendEmptyMessage(HANDLER_UPDATE_PLAYBACK_PROGRESS);

//        ControllerFragment controllerFragment = new ControllerFragment();
//        controllerFragment.showNow(getChildFragmentManager(), ControllerFragment.class.getSimpleName());
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    /**
     * when window lose focus then pause player, or get focus then start player
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d(TAG, "onWindowFocusChanged : " + hasFocus);
        if (hasFocus) {
            if (playingOnSurface && !videoPlayerView.isPlaying()) {
                videoPlayerView.playback();
                playPauseBtn.setBackgroundResource(R.drawable.media_pause);
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
        videoPlayerView = (VideoPlayerViewNew) view.findViewById(R.id.video_player_view);
        mVolumenLayout = (LinearLayout) view.findViewById(R.id.volume_layout);
        mVolumePercent = (TextView) view.findViewById(R.id.volume_percent_tv);
        mFastForwardProgressLayout = (LinearLayout) view.findViewById(R.id.fast_forward_progress_layout);
        mFastForwardProgresText = (TextView) view.findViewById(R.id.fast_forward_progress_text);
        playerControlBar = (LinearLayout) view.findViewById(R.id.player_control_bar);
        playPauseBtn = (ImageView) view.findViewById(R.id.play_pause_btn);
        mSeekBarLayout = (LinearLayout) view.findViewById(R.id.seekbar_layout);
        mCurrentTime = (TextView) view.findViewById(R.id.video_playtime);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekbar);
        mDurationTime = (TextView) view.findViewById(R.id.video_durationtime);
        openCloseFullscreenBtn = (ImageView) view.findViewById(R.id.open_close_fullscreen);
        floatingViewIv = (ImageView) view.findViewById(R.id.show_flotingview);

        playPauseBtn.setEnabled(false);
        playPauseBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                playPausePlayer();
            }
        });
        mSeekBar.setEnabled(false);
        mSeekBar.setOnSeekBarChangeListener(new PlaybackSeekBarChangeListener());
        openCloseFullscreenBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int orientaion = getResources().getConfiguration().orientation;
                if (orientaion == Configuration.ORIENTATION_PORTRAIT) {
                    ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    openCloseFullscreenBtn.setBackgroundResource(R.drawable.fullscreen_exit);
                } else if (orientaion == Configuration.ORIENTATION_LANDSCAPE) {
                    ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    openCloseFullscreenBtn.setBackgroundResource(R.drawable.fullscreen_exit);
                }
            }
        });
        floatingViewIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayerFloatingViewService.class);
                intent.putExtra(FloatingViewService.PARAM_KEY, FloatingViewService.PARAM_VALUE);
                intent.putExtra("MEDIAPATH", mMediaPath);
                getActivity().startService(intent);
                getActivity().finish();
            }
        });
    }

    public void playPausePlayer() {
        if (videoPlayerView.isPlaying()) {
            videoPlayerView.pause();
            playingOnSurface = false;
            playPauseBtn.setBackgroundResource(R.drawable.media_play);
        } else {
            videoPlayerView.playback();
            playingOnSurface = true;
            playPauseBtn.setBackgroundResource(R.drawable.media_pause);
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


    private void bigPlayerLandscape(){
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


    private void showControllerView(boolean autoHide) {
        if (playerControlBar.getVisibility() == View.GONE) {
            playerControlBar.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.control_bar_show_up));
            playerControlBar.setVisibility(View.VISIBLE);
        }
        if (autoHide) {
            mHandler.removeMessages(HANDLER_HIDE_CONTROL_BAR);
            mHandler.sendEmptyMessageDelayed(HANDLER_HIDE_CONTROL_BAR, HIDE_CONTROL_BAR_DELAY_TIME);
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

    private void updateProgress(int currentPosition) {
        mCurrentTime.setText(TimeFormater.formatMillisTime(currentPosition));
        mSeekBar.setProgress(currentPosition);
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

    public void play(String path) {
        videoPlayerView.play(path, 0);
        mMediaPath = path;
        lastPosition = 0;
    }


    private AudioManager audioMgr;
    private int maxVolume;
    private int currentVolume;
    private float volumePercent;

    private void gesture() {
        audioMgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumePercent = (float) currentVolume / (float) maxVolume;

        GestureSupport gestureSupport = new GestureSupport(getActivity(), videoPlayerView);
        gestureSupport.setGestureClickListener(new GestureSupport.GestureClickListener() {
            @Override
            public void onClick() {
                if (playerControlBar.getVisibility() == View.VISIBLE) {
                    hideControlView();
                } else {
                    showControllerView(true);
                }
            }
        });

        gestureSupport.setGestureFromLeftToRightListener(new GestureSupport.GestureFromLeftToRightListener() {
            @Override
            public void onStart() {
                Log.d(TAG, "LeftToRight ------- start");
                mFastForwardProgressLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScroll(MotionEvent e1, MotionEvent e2) {
                Log.d(TAG, "LeftToRight ------- moving   " + (e2.getRawX() - e1.getRawX()));
                float X = e2.getRawX() - e1.getRawX();
                if (positionToSeek >= 0 || positionToSeek <= duration) {
                    int x = DisplayUtil.px2dip(mContext, (int) X); // 优化滑动的流畅性
                    Log.d(TAG, "xxxxxxx " + x);
                    positionToSeek = lastPosition + x * 1000; //根据X计算快进/后退时间
                    String b = TimeFormater.formatMillisTime(positionToSeek) + "/" + TimeFormater.formatMillisTime(duration);
                    mFastForwardProgresText.setText(b);
                }
            }

            @Override
            public void onEnd() {
                Log.d(TAG, "LeftToRight ------- end");
                mFastForwardProgressLayout.setVisibility(View.GONE);
//                showOverlayHideDelayed(true);
                videoPlayerView.seekTo(positionToSeek);
            }
        });

        gestureSupport.setGestureFromRightToLeftListener(new GestureSupport.GestureFromRightToLeftListener() {
            @Override
            public void onStart() {
                Log.d(TAG, "RightToLeft ------- start");
                mFastForwardProgressLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScroll(MotionEvent e1, MotionEvent e2) {
                Log.d(TAG, "RightToLeft ------- moving   " + (e2.getRawX() - e1.getRawX()));
                float X = e2.getRawX() - e1.getRawX();
                if (positionToSeek >= 0 || positionToSeek <= duration) {
                    int x = DisplayUtil.px2dip(mContext, (int) X); // 优化滑动的流畅性
                    Log.d(TAG, "xxxxxxx " + x);
                    positionToSeek = lastPosition + x * 1000; //根据X计算快进/后退时间
                    String b = TimeFormater.formatMillisTime(positionToSeek) + "/" + TimeFormater.formatMillisTime(duration);
                    mFastForwardProgresText.setText(b);
                }
            }

            @Override
            public void onEnd() {
                Log.d(TAG, "RightToLeft ------- end");
                mFastForwardProgressLayout.setVisibility(View.GONE);
//                showOverlayHideDelayed(true);
                videoPlayerView.seekTo(positionToSeek);
            }
        });

        gestureSupport.setGestureFromBottomToTopListener(new GestureSupport.GestureFromBottomToTopListener() {
            @Override
            public void onStart() {
                Log.d(TAG, "BottomToTop ------- start");
                mVolumenLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScroll(MotionEvent e1, MotionEvent e2) {
                Log.d(TAG, "BottomToTop ------- moving   " + (e2.getRawY() - e1.getRawY()));
                float Y = e2.getRawY() - e1.getRawY();
                int y = DisplayUtil.px2dip(mContext, (int) Y); // 优化滑动的流畅性
                if (0 <= volumePercent && volumePercent <= 100) {
                    volumePercent -= y / 50;
                    if (volumePercent < 0) {
                        volumePercent = 0;
                    }
                    if (volumePercent > 100) {
                        volumePercent = 100;
                    }
                }
                if (volumePercent > 0) {
                    mVolumePercent.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_lock_silent_mode_off), null,
                            null, null);
                } else {
                    mVolumePercent.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_lock_silent_mode), null, null,
                            null);
                }
                mVolumePercent.setText((int) volumePercent + "%");
                int volume = (int) (volumePercent / 100 * maxVolume);
                audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
            }

            @Override
            public void onEnd() {
                Log.d(TAG, "BottomToTop ------- end");
                mVolumenLayout.setVisibility(View.GONE);
            }
        });

        gestureSupport.setGestureFromTopToBottomListener(new GestureSupport.GestureFromTopToBottomListener() {
            @Override
            public void onStart() {
                Log.d(TAG, "TopToBottom ------- start");
                mVolumenLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScroll(MotionEvent e1, MotionEvent e2) {
                Log.d(TAG, "TopToBottom ------- moving   " + (e2.getRawY() - e1.getRawY()));
                float Y = e2.getRawY() - e1.getRawY();
                int y = DisplayUtil.px2dip(mContext, (int) Y); // 优化滑动的流畅性
                if (0 <= volumePercent && volumePercent <= 100) {
                    volumePercent -= y / 50;
                    if (volumePercent < 0) {
                        volumePercent = 0;
                    }
                    if (volumePercent > 100) {
                        volumePercent = 100;
                    }
                }
                if (volumePercent > 0) {
                    mVolumePercent.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_lock_silent_mode_off), null,
                            null, null);
                } else {
                    mVolumePercent.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_lock_silent_mode), null, null,
                            null);
                }
                mVolumePercent.setText((int) volumePercent + "%");
                int volume = (int) (volumePercent / 100 * maxVolume);
                audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
            }

            @Override
            public void onEnd() {
                Log.d(TAG, "TopToBottom ------- end");
                mVolumenLayout.setVisibility(View.GONE);
            }
        });

    }
}
