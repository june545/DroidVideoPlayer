package com.woodyhi.player.base;

import android.content.Context;
import android.graphics.Color;
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
public class APlayerView extends FrameLayout {
    private static final String TAG = APlayerView.class.getSimpleName();

    VideoSurfaceView surfaceView;
    View coverView;
    View controllerView;

    PlayerManger playerManger;

    public APlayerView(Context context) {
        this(context, null);
    }

    public APlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public APlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        setBackgroundColor(Color.parseColor("#FF002000"));
    }

    private void init(Context context) {
        surfaceView = new VideoSurfaceView(getContext());
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        surfaceView.setTag("SurfaceView");
        addView(surfaceView, lp);
    }

    public void setCoverView(View view) {
        View cv = findViewWithTag("Cover");
        if (cv != null) {
            removeView(cv);
            coverView = null;
        }
        this.coverView = view;
        this.coverView.setTag("Cover");
        addView(coverView);
    }

    public void setControllerView(View view) {
        View cv = findViewWithTag("Controller");
        if (cv != null) {
            removeView(cv);
            controllerView = null;
        }
        this.controllerView = view;
        this.controllerView.setTag("Controller");
        addView(controllerView);
    }

    /**
     * 播放链接
     */
    public void playback(PlaybackInfo info) {
        playerManger.playback(info);
    }

    public PlayerManger getPlayerManger() {
        return playerManger;
    }

    public void setPlayerManager(PlayerManger playerManger) {
        this.playerManger = playerManger;
        this.playerManger.addPlayerListener(playerCallback);
        surfaceView.setPlayerManager(this.playerManger);
    }

    PlayerCallback playerCallback = new PlayerCallback() {
        @Override
        public void onVideoSizeChanged(int width, int height) {
            surfaceView.updateViewSizeByVideoSize(width, height);
        }
    };

}
