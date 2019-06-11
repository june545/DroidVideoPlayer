package com.woodyhi.player.base;

/**
 * @author June
 * @date 2019/6/8
 */

public interface BaseController {
    /** 播放 */
    public void playback();
    /** 暂停 */
    public void pause();
    /** 拖动播放 */
    public void seekTo(int msec);
    /** 快进 */
    public void fastForward(int msec);
    /** 快退 */
    public void fastRewind(int msec);

    public boolean isPlaying();

    public boolean isSeekable();

    public int getDuration();

    public int getCurrentPosition();
}
