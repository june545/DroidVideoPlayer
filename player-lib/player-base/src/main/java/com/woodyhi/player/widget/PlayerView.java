package com.woodyhi.player.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.TextureView;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.AbsPlayerView;
import com.woodyhi.player.base.PlayerCallback;

/**
 * @auth June
 * @date 2019/06/12
 */
public class PlayerView extends AbsPlayerView {

    private ResizeSurfaceView surfaceView;
    private TextureView textureView;

    public PlayerView(Context context) {
        this(context, null);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        surfaceView = new ResizeSurfaceView(context);
        super.setVideoView(surfaceView);
    }

    public void setPlayerManager(AbsPlayerManager playerManger) {
        playerManger.setVideoView(surfaceView);
        playerManger.addPlayerCallback(new PlayerCallback() {
            @Override
            public void onVideoSizeChanged(int width, int height) {
                surfaceView.updateViewSizeByVideoSize(width, height);
            }
        });
    }
}
