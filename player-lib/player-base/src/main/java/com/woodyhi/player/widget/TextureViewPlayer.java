package com.woodyhi.player.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.AbsPlayerView;
import com.woodyhi.player.base.PlayerCallback;

/**
 * @auth June
 * @date 2019/06/24
 */
public class TextureViewPlayer extends AbsPlayerView {

    private ResizeTextureView textureView;

    public TextureViewPlayer(Context context) {
        this(context, null);
    }

    public TextureViewPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextureViewPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        textureView = new ResizeTextureView(context);
        super.setVideoView(textureView);
    }

    public void setPlayerManager(AbsPlayerManager playerManger) {
        playerManger.setVideoView(textureView);
        playerManger.addPlayerCallback(new PlayerCallback() {
            @Override
            public void onVideoSizeChanged(int width, int height) {
                textureView.updateViewSizeByVideoSize(width, height);
            }
        });
    }
}
