package cn.woodyjc.media.video.player;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import cn.woodyjc.media.video.R;
import cn.woodyjc.media.video.service.FloatingViewService;
import cn.woodyjc.media.video.service.PlayerFloatingViewService;
import cn.woodyjc.media.video.VideoPlayerView;

/**
 * @author June Cheng
 * @date 2015年10月28日 下午10:28:47
 */
public class VideoPlayerFragment extends Fragment {
    private final String TAG = VideoPlayerFragment.class.getSimpleName();
    /**
     * 手势
     */
    public static final byte   GESTURE_NON                   = 0x0;
    public static final byte   GESTURE_HORIZONTAL           = 0x1; // 水平方向手势
    public static final byte   GESTURE_VERTICAL             = 0x2; // 垂直方向手势

    //  延迟时间
    private static final int  HIDE_CONTROL_BAR_DELAY_TIME      = 10000;

    private final       byte   HANDLER_SHOW_CONTROL_BAR          = 1;
    private final       byte   HANDLER_HIDE_CONTROL_BAR          = 2;
    private final       byte   HANDLER_UPDATE_PLAYBACK_PROGRESS = 3;

//    private int         mPlayerWidth;                                                //播放器尺寸
//    private int         mPlayerHeight;
    public        byte gestureOrientaion         = GESTURE_NON;

    private Context         mContext;
    private View            rootView;
    private VideoPlayerView videoPlayerView;

    // 尺寸常量
    private int          originalFrameWidth;
    private int          originalFrameHeight;
    private int          mScreenWidth;
    private int          mScreenHeight;
    /**
     * center popup
     */
    // 加载中
    private LinearLayout mLoadingView;
    private ProgressBar  mProgressBar;
    // 音量
    private LinearLayout mVolumenLayout;
    private TextView     mVolumePercent;
    // 快进/快退
    private LinearLayout mFastForwardProgressLayout;
    private TextView     mFastForwardProgresText;
    /* player control bar */
    private LinearLayout playerControlBar;
    private boolean      isControlBarShowing = true;
    private ImageView    playPauseBtn;
    private LinearLayout mSeekBarLayout;
    private TextView     mCurrentTime;
    private SeekBar      mSeekBar;                                                    // 进度
    private TextView     mDurationTime;
    private ImageView    openCloseFullscreenBtn;
    private boolean mSeeking = false;
    private ImageView floatingViewIv;
    private String    mMediaPath;
    private boolean playingOnSurface = false;
    private int     duration;
    private int     lastPosition;

    /**
     * 屏幕旋转事件监听
     */
    private MyOrientationEventListener myOrientationEventListener;
    private GestureDetector            mGestureDetector;
    private int                        positionToSeek;                                            //手势控制播放位置

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HANDLER_SHOW_CONTROL_BAR:
                    showOverlay();

                    break;
                case HANDLER_HIDE_CONTROL_BAR:
                    hideOverlay();

                    break;
                case HANDLER_UPDATE_PLAYBACK_PROGRESS:
                    updateProgress();
                    sendEmptyMessageDelayed(HANDLER_UPDATE_PLAYBACK_PROGRESS,1000 - lastPosition % 1000);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

        // 获取屏幕尺寸
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

        // 屏幕旋转
        myOrientationEventListener = new MyOrientationEventListener(mContext, SensorManager.SENSOR_DELAY_NORMAL);
        if (myOrientationEventListener.canDetectOrientation()) {
            Log.d(TAG, "can Detect Orientation");
//			myOrientationEventListener.enable();
        } else {
            Log.d(TAG, "can not Detect Orientation");
        }
        mGestureDetector = new GestureDetector(mContext, new MySimpleGestureListener());
        showOverlayHideDelayed(true);
        mHandler.sendEmptyMessage(HANDLER_UPDATE_PLAYBACK_PROGRESS);
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
            // currentPosition -= 3000;// go back few seconds, to compensate loading time
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
        videoPlayerView = (VideoPlayerView) view.findViewById(R.id.video_player_view);
        mLoadingView = (LinearLayout) view.findViewById(R.id.loading_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
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

        videoPlayerView.setOnTouchListener(new MyTouchListener());

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
        showOverlayHideDelayed(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged() " + newConfig.orientation);
        super.onConfigurationChanged(newConfig);

        WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();

        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                // 设置非全屏(show taskbar)
                params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                ((Activity) mContext).getWindow().setAttributes(params);
                ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

                // change player size
                openCloseFullscreenBtn.setBackgroundResource(R.drawable.fullscreen);
//                mPlayerWidth = originalFrameWidth;
//                mPlayerHeight = originalFrameHeight;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                // 设置全屏(hide taskbar)
                params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                ((Activity) mContext).getWindow().setAttributes(params);
                ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

                // change player size
                openCloseFullscreenBtn.setBackgroundResource(R.drawable.fullscreen_exit);
//                mPlayerWidth = mScreenHeight;
//                mPlayerHeight = mScreenWidth;
                break;
        }
    }

    /**
     * 显示控制栏并设置延迟隐藏
     */
    private void showOverlayHideDelayed(boolean dismissWithDelay) {
        if(!isControlBarShowing){
            mHandler.sendEmptyMessage(HANDLER_SHOW_CONTROL_BAR);
            mHandler.sendEmptyMessageDelayed(HANDLER_HIDE_CONTROL_BAR, HIDE_CONTROL_BAR_DELAY_TIME);
        }else {
            mHandler.removeMessages(HANDLER_HIDE_CONTROL_BAR);
            mHandler.sendEmptyMessageDelayed(HANDLER_HIDE_CONTROL_BAR, HIDE_CONTROL_BAR_DELAY_TIME);
        }
        if(!dismissWithDelay){
            mHandler.removeMessages(HANDLER_HIDE_CONTROL_BAR);
        }
    }

    /**
     * 显示控制栏
     */
    private void showOverlay(){
        if(!isControlBarShowing) {
            isControlBarShowing = true;
            playerControlBar.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.control_bar_show_up));
            playerControlBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏控制栏
     */
    private void hideOverlay() {
        if (isControlBarShowing) {
            isControlBarShowing = false;
            playerControlBar.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.control_bar_hide_down));
            playerControlBar.setVisibility(View.GONE);
        }
    }

    /**
     * update playback progress
     */
    private void updateProgress() {
        Log.v(TAG, "update progress");
        if (videoPlayerView.isPlaying()) {
            if (mLoadingView.getVisibility() != View.GONE)
                mLoadingView.setVisibility(View.GONE);

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

    private void updateProgress(int currentPosition){
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
            mLoadingView.setVisibility(View.VISIBLE);
            showOverlayHideDelayed(true);
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

    private class MyTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.i(TAG, " MyTouchListener " + event.getAction());
            if (event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
                // avoid multi point action causing confusion
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                mVolumenLayout.setVisibility(View.GONE);
                mFastForwardProgressLayout.setVisibility(View.GONE);

                if (gestureOrientaion == GESTURE_HORIZONTAL) {
                    showOverlayHideDelayed(true);
                    videoPlayerView.seekTo(positionToSeek);

                } else if (gestureOrientaion == GESTURE_VERTICAL) {
                }
                gestureOrientaion = GESTURE_NON;
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                mVolumenLayout.setVisibility(View.GONE);
                mFastForwardProgressLayout.setVisibility(View.GONE);
            }
            if (mGestureDetector.onTouchEvent(event)) {
                return true;
            }
            return false;
        }
    }

    private class MySimpleGestureListener extends SimpleOnGestureListener {
        private AudioManager audioMgr;
        private int          maxVolume;
        private int          currentVolume;
        private boolean gestureDown = false;
        private float volumePercent;

        public MySimpleGestureListener() {
            audioMgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            currentVolume = audioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
            volumePercent = (float) currentVolume / (float) maxVolume;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d(TAG, "onSingleTapUp " + e.getAction());
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d(TAG, "isControlBarShowing=" + isControlBarShowing + " , onSingleTapConfirmed -> " + e.getAction());
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                if (isControlBarShowing) {
                    hideOverlay();
                } else {
                    showOverlayHideDelayed(true);
                }
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d(TAG, "onDoubleTap " + e.getAction());
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.d(TAG, "onDoubleTapEvent " + e.getAction());
            if (e.getAction() == MotionEvent.ACTION_UP) {

            }
            return super.onDoubleTapEvent(e);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d(TAG, "onShowPress " + e.getAction());
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i(TAG, "e1:x=" + e1.getRawX() + ",y=" + e1.getRawY() + ", e2:x=" + e2.getRawX() + ",y=" + e2.getRawY() + ",  distanceX=" + distanceX + ", distanceY=" + distanceY);
            float X = e2.getRawX() - e1.getRawX();
            float Y = e2.getRawY() - e1.getRawY();
            Log.i(TAG, "X = " + X + ", Y = " + Y);
            if (gestureDown) {// 1、首先，判定此次手势方向
                Log.d(TAG, "onScroll gestureDown");
                if (Math.abs(Y) - Math.abs(X) > 1) {
                    gestureOrientaion = GESTURE_VERTICAL;
                    mVolumenLayout.setVisibility(View.VISIBLE);
                } else if (Math.abs(X) - Math.abs(Y) > 1) {
                    if (mSeekBarLayout.getVisibility() == View.VISIBLE) {
                        gestureOrientaion = GESTURE_HORIZONTAL;
                        mFastForwardProgressLayout.setVisibility(View.VISIBLE);
                    }
                }
                gestureDown = false;
            } else { // 2、然后，处理

                // 垂直方向
                if (gestureOrientaion == GESTURE_VERTICAL) {
                    int y = DisplayUtil.px2dip(mContext, (int) Y); // 优化滑动的流畅性
                    Log.d(TAG, "yyyyyyyy " + y);
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
                // 水平
                else if (gestureOrientaion == GESTURE_HORIZONTAL) {
                    if (positionToSeek >= 0 || positionToSeek <= duration) {
                        int x = DisplayUtil.px2dip(mContext, (int) X); // 优化滑动的流畅性
                        Log.d(TAG, "xxxxxxx " + x);
                        positionToSeek = lastPosition + x * 1000; //根据X计算快进/后退时间
                        String b = TimeFormater.formatMillisTime(positionToSeek) + "/" + TimeFormater.formatMillisTime(duration);
                        mFastForwardProgresText.setText(b);
                    }
                }
            }

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d(TAG, "onDown X=" + e.getX() + ", Y=" + e.getY() + ", rawX=" + e.getRawX() + ", rawY=" + e.getRawY());
            int delta = playerControlBar.getHeight();
            Log.d(TAG, "PlayerBottomBar.getHeight() " + delta);
            // 设置手势有效区域 if nessesary
//			if (e.getX() > delta && e.getY() > delta && mPlayerWidth - e.getX() > delta && mPlayerHeight - e.getY() > delta) {
//				Log.d(TAG, " gesture is at available region");
            gestureDown = true;
//			} else {
            // any down action will result in setting gestureDown true on the available rect region
//				gestureDown = false;
//				Log.d(TAG, " gesture is unavailable ");
//			}
            return true;
        }
    }


    public void play(String path) {
        videoPlayerView.play(path, 0);
        mMediaPath = path;
        lastPosition = 0;
        mLoadingView.setVisibility(View.VISIBLE);
    }

}
