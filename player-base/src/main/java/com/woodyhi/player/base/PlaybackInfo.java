package com.woodyhi.player.base;

import android.content.res.AssetFileDescriptor;

/**
 * @author June
 * @date 2019-06-09
 */
public class PlaybackInfo {
    public String path;
    public int playedTime;
    public String type;
    public AssetFileDescriptor assetFileDescriptor;

    public PlaybackInfo(String path) {
        this.path = path;
    }


    public PlaybackInfo(AssetFileDescriptor afd) {
        this.assetFileDescriptor = afd;
    }
}
