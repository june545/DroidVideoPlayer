package com.woodyhi.player.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.woodyhi.player.base.AbsPlayerView;

/**
 * @auth June
 * @date 2019/06/12
 */
public class PlayerView extends AbsPlayerView {
    public PlayerView(Context context) {
        this(context, null);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
