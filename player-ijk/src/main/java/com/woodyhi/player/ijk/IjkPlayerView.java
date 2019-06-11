package com.woodyhi.player.ijk;

import android.content.Context;
import android.util.AttributeSet;

import com.woodyhi.player.base.AbsPlayerView;
import com.woodyhi.player.internal.MediaPlayerManger;

/**
 * @auth June
 * @date 2019/06/10
 */
public class IjkPlayerView extends AbsPlayerView {
    public IjkPlayerView(Context context) {
        this(context, null);
    }

    public IjkPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IjkPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        IjkPlayerManager playerManger = new IjkPlayerManager();
        setPlayerManager(playerManger);
    }
}
