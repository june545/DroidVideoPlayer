package cn.woodyjc.media.video;

/**
 * Created by june on 2018/2/2.
 */

public interface PlayerControl {
    public void playback();
    public void pause();
    public void seekTo(int msec);

    public void fastforward(int msec);

    public void rewind(int msec);

    public boolean isPlaying();
    public void release();
}
