package com.woodyhi.player.base;

import android.media.MediaPlayer;

/**
 * @author June
 * @date 2019-06-09
 */
public abstract class PlayerListener {

    public void onPrepared() {
    }

    public void onBufferingStart(MediaPlayer mp) {
    }


    public void onBufferingUpdate(MediaPlayer mp, int percent) {
    }


    public void onBufferingEnd(MediaPlayer mp) {
    }


    public void onCompletion(MediaPlayer mp) {
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    }


}
