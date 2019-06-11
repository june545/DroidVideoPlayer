package com.woodyhi.player.vlc;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.PlaybackInfo;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;

/**
 * @author June
 * @date 2019-06-11
 */
public class VlcPlayerManager extends AbsPlayerManager {

    private Context context;
    private SurfaceView surfaceView;

    private MediaPlayer mediaPlayer;
    private IVLCVout ivlcVout;

    public VlcPlayerManager() {
    }


    public void setContext(Context context) {
        this.context = context;
    }

    private void createMediaPlayer() {
        ArrayList<String> options = new ArrayList<>();
        options.add(":file-caching=500"); // 文件缓存
        options.add(":network-caching=500"); // 网络缓存
        options.add(":live-caching=500"); // 直播缓存
        options.add(":sout-mux-caching=500"); // 输出缓存
        options.add(":codec=mediacodec,iomx,all");
        options.add(":rtsp-frame-buffer-size=1000"); // RTSP帧缓冲大小，默认大小为100000
        options.add(":rtsp-tcp"); // RTSP采用TCP传输方式
        options.add(":sout-rtp-proto={dccp,sctp,tcp,udp,udplite}"); // RTSP采用TCP传输方式

        LibVLC libVLC = new LibVLC(context, options);
        mediaPlayer = new MediaPlayer(libVLC);
        ivlcVout = mediaPlayer.getVLCVout();

    }

    public void setSurfaceView(SurfaceView view) {
        this.surfaceView = view;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    //-------------------------------------Controller-----------------------------------------
    @Override
    public void playback(PlaybackInfo info) {
        if(mediaPlayer == null) {
            createMediaPlayer();
        }
        ivlcVout.setVideoView(surfaceView);
        ivlcVout.attachViews();
    }

    @Override
    public void playback() {

    }

    @Override
    public void pause() {

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
