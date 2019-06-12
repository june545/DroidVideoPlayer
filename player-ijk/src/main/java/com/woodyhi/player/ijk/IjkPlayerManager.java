package com.woodyhi.player.ijk;

import android.view.SurfaceHolder;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.PlaybackInfo;

import java.io.IOException;

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
            ijkMediaPlayer = null;
        }
        final IjkMediaPlayer ijkPlayer = new IjkMediaPlayer();
        ijkPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

        /*
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "http-detect-range-support", 1);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "min-frames", 100);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);

        ijkMediaPlayer.setVolume(1.0f, 1.0f);
*/
        // 开启硬解码
        // ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        //  ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
        //  ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);

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
        if (ijkMediaPlayer == null) {
            createMediaPlayer();
        }
        ijkMediaPlayer.setSurface(surfaceHolder.getSurface());
        try {
            ijkMediaPlayer.setDataSource(playbackInfo.path);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (surfaceHolder != null)
            loadMedia();
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

    @Override
    public void release() {

    }
}
