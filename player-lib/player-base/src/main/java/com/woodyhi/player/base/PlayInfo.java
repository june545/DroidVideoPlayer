package com.woodyhi.player.base;

import android.content.res.AssetFileDescriptor;

/**
 * @author June
 * @date 2019-06-09
 */
public class PlayInfo {
    public String path;
    public int playedTime;
    public String type;
    public AssetFileDescriptor assetFileDescriptor;

    public PlayInfo(String path) {
        this.path = path;
    }


    public PlayInfo(AssetFileDescriptor afd) {
        this.assetFileDescriptor = afd;
    }
}
