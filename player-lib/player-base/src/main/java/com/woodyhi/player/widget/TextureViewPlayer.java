package com.woodyhi.player.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.AbsPlayerView;

/**
 * @auth June
 * @date 2019/06/24
 */
public class TextureViewPlayer extends AbsPlayerView {

    private TextureView textureView;

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
        textureView = new TextureView(context);
        super.setVideoView(textureView);
    }

    public void setPlayerManager(AbsPlayerManager playerManger) {
        playerManger.setVideoView(textureView);
    }
}
