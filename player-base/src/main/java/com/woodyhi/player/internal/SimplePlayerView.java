package com.woodyhi.player.internal;

import android.content.Context;
import android.util.AttributeSet;

import com.woodyhi.player.base.APlayerView;
import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.view.DefaultControllerView;

/**
 * @author June
 * @date 2019-06-09
 */
public class SimplePlayerView extends APlayerView {
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
        PlayerManger playerManger = new PlayerManger();
        setPlayerManager(playerManger);


        DefaultControllerView controllerView = new DefaultControllerView(context);
        controllerView.setPlayerManger(getPlayerManger());
        setControllerView(controllerView);
    }

    public void playback(PlaybackInfo info) {
        getPlayerManger().playback(info);
    }
}
