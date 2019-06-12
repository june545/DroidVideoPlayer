package com.woodyhi.player.vlc;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.LogUtil;
import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.base.PlayerCallback;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author June
 * @date 2019-06-11
 */
public class VlcPlayerManager extends AbsPlayerManager {
    private static final String TAG = VlcPlayerManager.class.getSimpleName();

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
//        options.add("-vvv"); // 日志
        options.add("--http-reconnect");
        options.add("--network-caching=3000");
        options.add("--live-caching=500"); // 直播缓存
        options.add("--sout-mux-caching=500"); // 输出缓存
        options.add("--rtsp-tcp"); // RTSP采用TCP传输方式
        options.add("--aout=opensles");
        options.add("--audio-time-stretch"); // time stretching

        libVLC = new LibVLC(options);
        mediaPlayer = new MediaPlayer(libVLC);

        mediaPlayer.setEventListener(new EventListenerImpl(this));
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

    private static class EventListenerImpl implements MediaPlayer.EventListener {
        private WeakReference<VlcPlayerManager> mOwner;

        public EventListenerImpl(VlcPlayerManager owner) {
            mOwner = new WeakReference<>(owner);
        }

        @Override
        public void onEvent(MediaPlayer.Event event) {
            VlcPlayerManager mgr = mOwner.get();
            Log.d(TAG, "Player EVENT");
            switch (event.type) {
                case MediaPlayer.Event.EndReached:
                    LogUtil.d(TAG, "MediaPlayerEndReached");
//                    player.releasePlayer();
                    break;
                case MediaPlayer.Event.EncounteredError:
                    LogUtil.d(TAG, "Media Player Error, re-try");
                    //player.releasePlayer();
                    break;
                case MediaPlayer.Event.PositionChanged:
                    break;
                case MediaPlayer.Event.TimeChanged:
                    mgr.onTimeChanged(event.getTimeChanged());
                    break;
                case MediaPlayer.Event.Playing:
                case MediaPlayer.Event.Paused:
                case MediaPlayer.Event.Stopped:
                default:
                    break;
            }
        }
    }

    private void onTimeChanged(long time) {
        for (PlayerCallback callback : playerCallbacks) {
            callback.onTimeChanged(time);
        }
    }

    private void loadMedia() {
        if (mediaPlayer == null) {
            create();
            mediaPlayer.getVLCVout().detachViews();
            mediaPlayer.getVLCVout().setVideoSurface(surfaceHolder.getSurface(), surfaceHolder);
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
        if (mediaPlayer != null && !mediaPlayer.isPlaying())
            mediaPlayer.play();
    }

    @Override
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    @Override
    public void seekTo(int msec) {
        if (mediaPlayer != null && mediaPlayer.isSeekable()) {
            mediaPlayer.pause();
            mediaPlayer.setPosition(msec);
            mediaPlayer.play();
        }
    }

    @Override
    public void fastForward(int msec) {

    }

    @Override
    public void fastRewind(int msec) {

    }

    @Override
    public boolean isPlaying() {
        return (mediaPlayer != null && mediaPlayer.isPlaying());
    }

    @Override
    public boolean isSeekable() {
        if (mediaPlayer != null)
            return mediaPlayer.isSeekable();
        return false;
    }

    @Override
    public int getDuration() {
        if (mediaPlayer != null)
            return (int) mediaPlayer.getLength();
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (mediaPlayer != null)
            mediaPlayer.getPosition();
        return 0;
    }

    @Override
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (libVLC != null) {
            libVLC.release();
            libVLC = null;
        }
    }
}
