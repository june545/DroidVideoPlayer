package com.woodyhi.player.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.AbsPlayerView;

/**
 * @auth June
 * @date 2019/06/24
 */
public class SurfaceViewPlayer extends AbsPlayerView {

    private SurfaceView surfaceView;

    public SurfaceViewPlayer(Context context) {
        this(context, null);
    }

    public SurfaceViewPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceViewPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        surfaceView = new ResizeSurfaceView(context);
        super.setVideoView(surfaceView);
    }

    public void setPlayerManager(AbsPlayerManager playerManger) {
        playerManger.setVideoView(surfaceView);
    }
}
