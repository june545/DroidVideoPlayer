package com.woodyhi.player.vlc;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.PlaybackInfo;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCUtil;

import java.util.ArrayList;

/**
 * @author June
 * @date 2019-06-11
 */
public class VlcPlayerManager extends AbsPlayerManager {
    private final String TAG = VlcPlayerManager.class.getSimpleName();

    private Context context;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private PlaybackInfo playbackInfo;

    private MediaPlayer mediaPlayer;
    private LibVLC libVLC;

    public VlcPlayerManager() {
    }


    public void setContext(Context context) {
        this.context = context;
    }

    private void create() {
        if (!VLCUtil.hasCompatibleCPU(context)) {
            Log.e(TAG, VLCUtil.getErrorMsg());
            throw new IllegalStateException("LibVLC initialisation failed: " + VLCUtil.getErrorMsg());
        }
        ArrayList<String> options = new ArrayList<>();
        options.add("-vvv");
        options.add("--network-caching=3000");
        options.add("--live-caching=500"); // 直播缓存
        options.add("--sout-mux-caching=500"); // 输出缓存
        options.add("--rtsp-tcp"); // RTSP采用TCP传输方式

        libVLC = new LibVLC(options);
        mediaPlayer = new MediaPlayer(libVLC);
    }

    private void createMediaPlayer() {
        ArrayList<String> options = new ArrayList<>();
//        args.add("-vvv");
//        options.add(":file-caching=500"); // 文件缓存
//        options.add(":network-caching=500"); // 网络缓存
//        options.add(":live-caching=500"); // 直播缓存
//        options.add(":sout-mux-caching=500"); // 输出缓存
//        options.add(":codec=mediacodec,iomx,all");
//        options.add(":rtsp-frame-buffer-size=1000"); // RTSP帧缓冲大小，默认大小为100000
//        options.add(":rtsp-tcp"); // RTSP采用TCP传输方式
//        options.add(":sout-rtp-proto={dccp,sctp,tcp,udp,udplite}"); // RTSP采用TCP传输方式

//        libVLC = new LibVLC(context, options);
        mediaPlayer = new MediaPlayer(libVLC);
    }

    private void loadMedia() {
        if (mediaPlayer == null) {
            create();
            mediaPlayer.getVLCVout().detachViews();
            mediaPlayer.getVLCVout().setVideoView(surfaceView);
            mediaPlayer.getVLCVout().attachViews();
        }
        Media media = new Media(libVLC, Uri.parse(playbackInfo.path));
        mediaPlayer.setMedia(media);
        media.release();
        mediaPlayer.play();
    }

    public void setSurfaceView(SurfaceView view) {
        this.surfaceView = view;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.surfaceHolder = holder;
        if (playbackInfo != null)
            loadMedia();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.surfaceHolder = null;
    }


    //-------------------------------------Controller-----------------------------------------
    @Override
    public void playback(PlaybackInfo info) {
        this.playbackInfo = info;

        if (surfaceHolder != null) {
            loadMedia();
        }
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

    @Override
    public void release() {
        if (mediaPlayer != null)
            mediaPlayer.release();
        if (libVLC != null)
            libVLC.release();
    }
}
