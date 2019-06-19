package com.woodyhi.player.ijk;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.LogUtil;
import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.base.PlayerCallback;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @auth June
 * @date 2019/06/10
 */
public class IjkPlayerManager extends AbsPlayerManager {
    private final String TAG = IjkPlayerManager.class.getSimpleName();

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
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1); //Sound Library for Embedded Systems 为嵌入式系统打开声音库
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "http-detect-range-support", 1);
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48); // 是否开启环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "fastseek");
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 1); // 设置播放前的探测时间 1,达到首屏秒开效果
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 1024 * 10); // 播放前的探测Size，默认是1M, 改小一点会出画面更快
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1L); // 每处理一个packet之后刷新io上下文
        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "reconnect", 3); // 重连次数

        /*
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);


mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"max-buffer-size",maxCacheSize);最大缓冲大小,单位kb
mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"max-fps",30);最大fps
        ijkMediaPlayer.setVolume(1.0f, 1.0f);
        // 开启硬解码
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
*/

        ijkMediaPlayer = ijkPlayer;

        ijkMediaPlayer.setOnPreparedListener(IMediaPlayer::start);
        ijkMediaPlayer.setOnVideoSizeChangedListener((mp, width, height, sar_num, sar_den) -> {
            LogUtil.d(TAG, "onVideoSizeChanged: w " + width + ", h " + height +
                    ", sar_num " + sar_num + ", sar_den " + sar_den);
            for(PlayerCallback callback : playerCallbacks)
                callback.onVideoSizeChanged(width, height);
        });
        ijkMediaPlayer.setOnInfoListener((iMediaPlayer, what, extra) -> false);
        ijkMediaPlayer.setOnSeekCompleteListener(iMediaPlayer -> {

        });
        ijkMediaPlayer.setOnBufferingUpdateListener((iMediaPlayer, percent) -> {
            for (PlayerCallback callback : playerCallbacks)
                callback.onBufferingUpdate(percent);
        });
        ijkMediaPlayer.setOnCompletionListener(iMediaPlayer -> {
            for (PlayerCallback callback : playerCallbacks)
                callback.onCompletion();
        });
        ijkMediaPlayer.setOnErrorListener((iMediaPlayer, what, extra) -> false);
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
    public void surfaceCreated(SurfaceView view, SurfaceHolder holder) {
        this.surfaceHolder = holder;
        loadMedia();
    }

    @Override
    public void surfaceDestroyed(SurfaceView view, SurfaceHolder holder) {
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
        if (ijkMediaPlayer != null && !ijkMediaPlayer.isPlaying())
            ijkMediaPlayer.start();
    }

    @Override
    public void pause() {
        if (ijkMediaPlayer != null && ijkMediaPlayer.isPlaying())
            ijkMediaPlayer.pause();
    }

    @Override
    public void seekTo(int msec) {
        if (ijkMediaPlayer != null)
            ijkMediaPlayer.seekTo(msec);
    }

    @Override
    public void fastForward(int msec) {
        // TODO
    }

    @Override
    public void fastRewind(int msec) {
        // TODO
    }

    @Override
    public boolean isPlaying() {
        return ijkMediaPlayer != null && ijkMediaPlayer.isPlaying();
    }

    @Override
    public boolean isSeekable() {
        return false;
    }

    @Override
    public int getDuration() {
        if (ijkMediaPlayer != null)
            return (int) ijkMediaPlayer.getDuration();
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (ijkMediaPlayer != null)
            return (int) ijkMediaPlayer.getCurrentPosition();
        return 0;
    }

    @Override
    public void release() {
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.stop();
            ijkMediaPlayer.setDisplay(null);
            ijkMediaPlayer.release();
            ijkMediaPlayer = null;
        }
    }
}
