package com.woodyhi.player.internal;

import android.content.Context;
import android.util.AttributeSet;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.AbsPlayerView;
import com.woodyhi.player.base.PlayerCallback;
import com.woodyhi.player.widget.ResizeSurfaceView;

/**
 * @author June
 * @date 2019-06-09
 */
public class SimplePlayerView extends AbsPlayerView {

    private ResizeSurfaceView surfaceView;
    private MediaPlayerManger playerManger;

    public SimplePlayerView(Context context) {
        this(context, null);
    }

    public SimplePlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimplePlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        playerManger = new MediaPlayerManger();
        playerManger.addPlayerCallback(new PlayerCallback() {
            @Override
            public void onVideoSizeChanged(int width, int height) {
                surfaceView.updateViewSizeByVideoSize(width, height);
            }
        });

        surfaceView = new ResizeSurfaceView(context);
        super.setVideoView(surfaceView);
        playerManger.setVideoView(surfaceView);

        DefaultControllerView controllerView = new DefaultControllerView(context);
        controllerView.setPlayerManger(playerManger);
        setControllerView(controllerView);
    }

    public AbsPlayerManager getPlayerManager() {
        return playerManger;
    }
}
