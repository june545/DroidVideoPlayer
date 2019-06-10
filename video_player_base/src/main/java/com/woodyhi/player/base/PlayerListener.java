package com.woodyhi.player.base;

/**
 * @author June
 * @date 2019-06-09
 */
public abstract class PlayerListener {

    public void onPrepared() {
    }

    public void onBufferingStart() {
    }


    public void onBufferingUpdate(int percent) {
    }


    public void onBufferingEnd() {
    }


    public void onCompletion() {
    }

    public void onVideoSizeChanged(int width, int height) {
    }


}
