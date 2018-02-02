package cn.woodyjc.media.video;

/**
 * Created by june on 2018/2/2.
 */

public interface Function {
    public void playback();
    public void pause();
    public void seekTo(int msec);
    public void fastforward();
    public void rewind();
    public boolean isPlaying();
    public void release();
}
