package com.woodyhi.player.base;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author June
 * @date 2019-06-09
 */
public class VideoPlayer extends VideoPlayerViewA {
    public VideoPlayer(Context context) {
        this(context, null);
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
