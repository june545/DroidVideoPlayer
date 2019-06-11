package com.woodyhi.player.ijk;

import android.view.SurfaceHolder;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.PlaybackInfo;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @auth June
 * @date 2019/06/10
 */
public class IjkPlayerManager extends AbsPlayerManager {

    IjkMediaPlayer ijkMediaPlayer;
    SurfaceHolder surfaceHolder;

    PlaybackInfo playbackInfo;

    private IjkMediaPlayer createMediaPlayer() {
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.stop();
            ijkMediaPlayer.setDisplay(null);
            ijkMediaPlayer.release();
        }
        final IjkMediaPlayer ijkPlayer = new IjkMediaPlayer();
        ijkPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

        // 开启硬解码
        // ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);

        ijkMediaPlayer = ijkPlayer;

        ijkMediaPlayer.setOnPreparedListener(iMediaPlayer -> iMediaPlayer.start());
        ijkMediaPlayer.setOnInfoListener((iMediaPlayer, i, i1) -> false);
        ijkMediaPlayer.setOnSeekCompleteListener(iMediaPlayer -> {

        });
        ijkMediaPlayer.setOnBufferingUpdateListener((iMediaPlayer, i) -> {

        });
        ijkMediaPlayer.setOnErrorListener((iMediaPlayer, i, i1) -> false);
        return ijkMediaPlayer;
    }

    private void loadMedia() {
        ijkMediaPlayer.setSurface(surfaceHolder.getSurface());
        ijkMediaPlayer.prepareAsync();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.surfaceHolder = holder;
        loadMedia();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.surfaceHolder = null;
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.setSurface(null);
        }
        pause();
    }


    //-----------------------------------------Controller--------------------------------------
    public void playback(PlaybackInfo info) {
        this.playbackInfo = info;
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.stop();
            ijkMediaPlayer.setDisplay(null);
            ijkMediaPlayer.release();
        } else {
            if (surfaceHolder != null)
                loadMedia();
        }
    }

    @Override
    public void playback() {

    }

    @Override
    public void pause() {
        if (ijkMediaPlayer != null && ijkMediaPlayer.isPlaying())
            ijkMediaPlayer.pause();
    }

    @Override
    public void seekTo(int msec) {

    }

    @Override
    public void fastForward(int msec) {

    }

    @Override
    public void fastRewind(int msec) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public boolean isSeekable() {
        return false;
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }


}
