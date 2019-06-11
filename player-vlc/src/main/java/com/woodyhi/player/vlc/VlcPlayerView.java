package com.woodyhi.player.vlc;

import android.content.Context;
import android.util.AttributeSet;

import com.woodyhi.player.base.AbsPlayerView;

/**
 * @auth June
 * @date 2019/06/10
 */
public class VlcPlayerView extends AbsPlayerView {
    public VlcPlayerView(Context context) {
        this(context, null);
    }

    public VlcPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VlcPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        VlcPlayerManager manager = new VlcPlayerManager();
        manager.setContext(context);
        this.setPlayerManager(manager);
        manager.setSurfaceView(getSurfaceView());
    }


}
