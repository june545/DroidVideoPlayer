package com.woodyhi.player.vlc;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.LogUtil;
import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.base.PlayerCallback;

import org.videolan.libvlc.IVLCVout;
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
    private volatile int lastPosition;

    public VlcPlayerManager(Context context) {
        this.context = context;
    }

    private void createMediaPlayer() {
        if (!VLCUtil.hasCompatibleCPU(context)) {
            Log.e(TAG, VLCUtil.getErrorMsg());
            throw new IllegalStateException("LibVLC initialisation failed: " + VLCUtil.getErrorMsg());
        }
        ArrayList<String> options = new ArrayList<>();
//        options.add("-vvv"); // verbosity
        options.add("--http-reconnect");
//        options.add("--network-caching=6000");
        options.add("--aout=opensles");
        options.add("--audio-time-stretch");
        options.add("--rtsp-tcp"); // RTSP采用TCP传输方式
//        options.add("--rtsp-frame-buffer-size=1000"); // RTSP帧缓冲大小，默认大小为100000

        libVLC = new LibVLC(context, options);
        mediaPlayer = new MediaPlayer(libVLC);
        mediaPlayer.setEventListener(new EventListenerImpl(this));
    }

//        options.add(":file-caching=500"); // 文件缓存
//        options.add(":live-caching=500"); // 直播缓存
//        options.add(":sout-mux-caching=500"); // 输出缓存
//        options.add(":codec=mediacodec,iomx,all");
//        options.add(":sout-rtp-proto={dccp,sctp,tcp,udp,udplite}"); // RTSP采用TCP传输方式

    private static class EventListenerImpl implements MediaPlayer.EventListener {
        private WeakReference<VlcPlayerManager> mOwner;

        public EventListenerImpl(VlcPlayerManager owner) {
            mOwner = new WeakReference<>(owner);
        }

        @Override
        public void onEvent(MediaPlayer.Event event) {
            VlcPlayerManager mgr = mOwner.get();
            if(mgr == null) return;
            LogUtil.d(TAG, "Player EVENT " + event.type + ", " + Integer.toHexString(event.type));
            switch (event.type) {
                case MediaPlayer.Event.EndReached:
                    LogUtil.d(TAG, "MediaPlayerEndReached");
                    mgr.onCompletion();
                    mgr.release();
                    break;
                case MediaPlayer.Event.EncounteredError:
                    LogUtil.d(TAG, "Media Player Error, re-try");
                    //releasePlayer();
                    break;
                case MediaPlayer.Event.TimeChanged:
//                    mgr.onTimeChanged(event.getTimeChanged());
                    mgr.lastPosition = (int) event.getTimeChanged();
//                    Media.VideoTrack vtrack = mgr.mediaPlayer.getCurrentVideoTrack();
//                    if (vtrack != null) {
//                        LogUtil.d(TAG, "TimeChanged ------  videoW : " + vtrack.width + ", videoH : " + vtrack.height);
//                    }
                    break;
                case MediaPlayer.Event.Buffering:
                    LogUtil.d(TAG, "MediaPlayer.Event.Buffering ------  " + event.getBuffering());
//                    mgr.onBuffering(event.getBuffering());
                    break;
                case MediaPlayer.Event.Playing:
                case MediaPlayer.Event.Paused:
                case MediaPlayer.Event.Stopped:
                default:
                    break;
            }
        }
    }

    private void onBuffering(float percent) {
        for (PlayerCallback callback : playerCallbacks)
            callback.onBufferingUpdate(percent);
    }

    private void onCompletion() {
        for (PlayerCallback callback : playerCallbacks)
            callback.onCompletion();
    }

    @Deprecated
    private void onTimeChanged(long time) {
        for (PlayerCallback callback : playerCallbacks) {
            callback.onTimeChanged(time);
        }
    }

    private IVLCVout.OnNewVideoLayoutListener onNewVideoLayoutListener = new IVLCVout.OnNewVideoLayoutListener() {
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void onNewVideoLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
            LogUtil.d(TAG, "---------- width: " + width + ", height: " + height
                    + ", visibleWidth: " + visibleWidth + ", visibleHeight: " + visibleHeight);
            new VlcVideoLayout(context)
                    .with((FrameLayout) surfaceView.getParent(), surfaceView, mediaPlayer)
                    .onNewLayout(width, height, visibleWidth, visibleHeight, sarNum, sarDen)
                    .updateVideoSurfaces(1080, 1920);
        }
    };

    // 监听surfaceview容器大小
    private View.OnLayoutChangeListener onLayoutChangeListener = (
            v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom
    ) -> {
        if (right - left != oldRight - oldLeft || bottom - top != oldBottom - oldTop) {
            if (mediaPlayer != null) {
                mediaPlayer.getVLCVout().setWindowSize(right - left, bottom - top);
            }
        }
    };

    private void loadMedia() {
        if (surfaceView == null) {
            throw new NullPointerException("surfaceView field is null");
        }
        FrameLayout parent = (FrameLayout) surfaceView.getParent();
        parent.removeOnLayoutChangeListener(onLayoutChangeListener);
        parent.addOnLayoutChangeListener(onLayoutChangeListener);

        if (mediaPlayer == null) {
            createMediaPlayer();
            mediaPlayer.getVLCVout().detachViews();
            mediaPlayer.getVLCVout().setVideoSurface(surfaceHolder.getSurface(), surfaceHolder);
            mediaPlayer.getVLCVout().setWindowSize(surfaceView.getMeasuredWidth(), surfaceView.getMeasuredHeight());
            mediaPlayer.getVLCVout().attachViews(onNewVideoLayoutListener);
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
        if (mediaPlayer == null)
            loadMedia();

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
            mediaPlayer.setTime(msec);
        }
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
        return lastPosition;
    }

    @Override
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.getVLCVout().detachViews();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (libVLC != null) {
            libVLC.release();
            libVLC = null;
        }
    }
}