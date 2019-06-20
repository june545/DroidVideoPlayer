package com.woodyhi.player.internal;

import android.content.Context;
import android.util.AttributeSet;

import com.woodyhi.player.base.AbsPlayerView;

/**
 * @author June
 * @date 2019-06-09
 */
public class SimplePlayerView extends AbsPlayerView {
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
        MediaPlayerManger playerManger = new MediaPlayerManger();
        this.setPlayerManager(playerManger);


        DefaultControllerView controllerView = new DefaultControllerView(context);
        controllerView.setPlayerManger(getPlayerManger());
        setControllerView(controllerView);
    }

}
