package com.woodyhi.player.base;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.woodyhi.player.base.view.VideoSurfaceView;

/**
 * @author June
 * @date 2019-06-08
 */
public class VideoPlayerViewA extends FrameLayout {
    private static final String TAG = VideoPlayerViewA.class.getSimpleName();

    VideoSurfaceView surfaceView;
    View coverView;
    View controllerView;

    PlayerManger playerManger;
    BaseController baseController;

    public BaseController getBaseController() {
        return baseController;
    }

    public VideoPlayerViewA(Context context) {
        this(context, null);
    }

    public VideoPlayerViewA(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayerViewA(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        setBackgroundColor(Color.parseColor("#FF002000"));
    }

    private void init(Context context) {
        surfaceView = new VideoSurfaceView(getContext());
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        addView(surfaceView, lp);

        playerManger = new PlayerManger();
        playerManger.addPlayerListener(playerListener);

        surfaceView.setPlayerManager(playerManger);
    }

    public void setCoverView(View view) {
        this.coverView = view;
    }

    public void setControllerView(View view) {
        this.controllerView = view;
    }

    /**
     * 播放链接
     */
    public void playback(PlaybackInfo info) {
        playerManger.setPlaybackInfo(info);
        playerManger.playback();
    }

    public PlayerManger getPlayerManger() {
        return playerManger;
    }

    PlayerListener playerListener = new PlayerListener() {
        @Override
        public void onVideoSizeChanged(int width, int height) {
            surfaceView.updateViewSizeByVideoSize(width, height);
        }
    };
}
