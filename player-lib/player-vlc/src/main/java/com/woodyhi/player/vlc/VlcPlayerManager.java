package com.woodyhi.player.vlc;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.LogUtil;
import com.woodyhi.player.base.PlayInfo;
import com.woodyhi.player.base.PlayerCallback;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author June
 * @date 2019-06-11
 */
public class VlcPlayerManager extends AbsPlayerManager {
    private static final String TAG = VlcPlayerManager.class.getSimpleName();

    private Context context;
    private PlayInfo playInfo;

    private MediaPlayer mediaPlayer;
    private LibVLC libVLC;
    private volatile int lastPosition;
    private volatile boolean seekable = true;
    private volatile boolean endReached;
    private boolean play = true;
    private boolean pausedFromUser;

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
        options.add("--network-caching=6000");
        options.add("--aout=opensles");
        options.add("--audio-time-stretch");
        options.add("--rtsp-tcp"); // RTSP采用TCP传输方式
//        options.add("--rtsp-frame-buffer-size=1000"); // RTSP帧缓冲大小，默认大小为100000

        options.add("--live-caching=10000");
//        options.add("--disc-caching=10000");
        options.add("--file-caching=10000");
//        options.add("--fullscreen");
        options.add("--avcodec-skiploopfilter=4");
        options.add("--codec=mediacodec_ndk,mediacodec_jni,iomx,all");
//        options.add("--no-drop-late-frames");
//        options.add("--no-skip-frames");
//        options.add("--no-mediacodec-dr");
        options.add("--no-omxil-dr");
        options.add("--no-omxil-dr");

//        options.add(":file-caching=500"); // 文件缓存
//        options.add(":live-caching=500"); // 直播缓存
//        options.add(":sout-mux-caching=500"); // 输出缓存
//        options.add(":codec=mediacodec,iomx,all");
//        options.add(":sout-rtp-proto={dccp,sctp,tcp,udp,udplite}"); // RTSP采用TCP传输方式

        libVLC = new LibVLC(context, options);
        mediaPlayer = new MediaPlayer(libVLC);
        mediaPlayer.setEventListener(new EventListenerImpl(this));
    }


    private static class EventListenerImpl implements MediaPlayer.EventListener {
        private WeakReference<VlcPlayerManager> mOwner;

        public EventListenerImpl(VlcPlayerManager owner) {
            mOwner = new WeakReference<>(owner);
        }

        @Override
        public void onEvent(MediaPlayer.Event event) {
            VlcPlayerManager mgr = mOwner.get();
            if (mgr == null) return;
            LogUtil.d(TAG, "Player EVENT " + event.type + ", " + Integer.toHexString(event.type));
            switch (event.type) {
                case MediaPlayer.Event.EndReached:
                    LogUtil.d(TAG, "MediaPlayerEndReached");
                    mgr.onCompletion();
                    mgr.release();
                    mgr.play = false;
                    mgr.endReached = true;
                    break;
                case MediaPlayer.Event.EncounteredError:
                    LogUtil.d(TAG, "Media Player Error, re-try");
                    //releasePlayer();
                    break;
                case MediaPlayer.Event.TimeChanged:
                    mgr.lastPosition = (int) event.getTimeChanged();
                    break;
                case MediaPlayer.Event.Buffering:
                    LogUtil.d(TAG, "MediaPlayer.Event.Buffering ------  " + event.getBuffering());
//                    mgr.onBuffering(event.getBuffering());
                    break;
                case MediaPlayer.Event.SeekableChanged:
                    mgr.seekable = event.getSeekable();
                    break;
                case MediaPlayer.Event.Playing:
                    mgr.onPlaying();
                    break;
                case MediaPlayer.Event.Paused:
                case MediaPlayer.Event.Stopped:
                default:
                    break;
            }
        }
    }

    private void onPlaying() {
        for (PlayerCallback callback : playerCallbacks)
            callback.onPlaying();
        this.pausedFromUser = false;
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
//            FrameLayout parent = (FrameLayout) surfaceView.getParent();
//            new VlcVideoLayout(context)
//                    .with(parent, surfaceView, mediaPlayer)
//                    .onNewLayout(width, height, visibleWidth, visibleHeight, sarNum, sarDen)
//                    .updateVideoSurfaces(parent.getWidth(), parent.getHeight());
        }
    };

    // 监听surfaceview容器大小
    private View.OnLayoutChangeListener onLayoutChangeListener = (
            v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom
    ) -> {
        if (right - left != oldRight - oldLeft || bottom - top != oldBottom - oldTop) {
            if (mediaPlayer != null) {
                LogUtil.d(TAG, "onLayoutChangeListener : " + (right - left) + ", " + (bottom - top));
                mediaPlayer.getVLCVout().setWindowSize(right - left, bottom - top);
            }
        }
    };

    private void loadMedia() {
//        if (playInfo == null) {
//            LogUtil.e(TAG, "playInfo is null");
//            return;
//        }
        if (surfaceView == null && textureView == null) {
            LogUtil.e(TAG, "video view field is null");
            return;
        }


        if (surfaceView != null) {
            if (!isSurfaceValid && !surfaceView.getHolder().getSurface().isValid()) {
                LogUtil.e(TAG, "surfaceView -> surface is invalid");
                return;
            }
        }

        if (textureView != null) {
            if (!isSurfaceValid && !textureView.isAvailable()) {
                LogUtil.e(TAG, "textureView -> surface is invalid");
                return;
            }
        }

        if (mediaPlayer == null) {
            createMediaPlayer();
            mediaPlayer.getVLCVout().detachViews();
            if (surfaceView != null) {
                mediaPlayer.getVLCVout().setVideoView(surfaceView);
                mediaPlayer.getVLCVout().setWindowSize(surfaceView.getWidth(), surfaceView.getHeight());
                FrameLayout parent = (FrameLayout) surfaceView.getParent();
                parent.removeOnLayoutChangeListener(onLayoutChangeListener);
                parent.addOnLayoutChangeListener(onLayoutChangeListener);
            } else {
                mediaPlayer.getVLCVout().setVideoView(textureView);
                mediaPlayer.getVLCVout().setWindowSize(textureView.getWidth(), textureView.getHeight());
                FrameLayout parent = (FrameLayout) textureView.getParent();
                parent.removeOnLayoutChangeListener(onLayoutChangeListener);
                parent.addOnLayoutChangeListener(onLayoutChangeListener);
            }
            mediaPlayer.getVLCVout().attachViews(onNewVideoLayoutListener);
        }

        Media media = new Media(libVLC, Uri.parse(playInfo.path));
        mediaPlayer.setMedia(media);
        media.release();
        mediaPlayer.play();
    }

    @Override
    protected void onSurfaceCreated(Surface surface) {
        if (mediaPlayer != null) {
            mediaPlayer.getVLCVout().detachViews();
            mediaPlayer.getVLCVout().setVideoSurface(surface, null);
            mediaPlayer.getVLCVout().attachViews(onNewVideoLayoutListener);
        }
        if (!endReached && !pausedFromUser) {
            if (mediaPlayer != null && mediaPlayer.getPlayerState() == Media.State.Paused) {
                play();
            } else {
                loadMedia();
            }
        }
    }

    @Override
    protected void onSurfaceDestroyed() {
        pause();
        if (mediaPlayer != null)
            mediaPlayer.getVLCVout().detachViews();
    }

    //-------------------------------------Controller-----------------------------------------
    @Override
    public void play(PlayInfo info) {
        if (info == null) {
            if (mediaPlayer == null) {
                loadMedia();
            } else {
                play();
            }
        } else {
            release();
            this.playInfo = info;
            loadMedia();
        }
        play = true;
        endReached = false;
    }

    @Override
    public void play() {
        if (mediaPlayer != null && mediaPlayer.getPlayerState() == Media.State.Paused)
            mediaPlayer.play();
        play = true;
    }

    @Override
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
//        try {
//            Bitmap bitmap = textureView.getBitmap();
//            saveBitmap(bitmap, context.getExternalCacheDir().getAbsolutePath() + "/shot.jpg");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void saveBitmap(Bitmap bitmap, String path) {
        File filePic;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        } else {
            Log.d("xxx", "saveBitmap: 1return");
            return;
        }
        try {
            filePic = new File(path);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("xxx", "saveBitmap: 2return");
            return;
        }
        Log.d("xxx", "saveBitmap: " + filePic.getAbsolutePath());
    }

    @Override
    public void pause(boolean fromUser) {
        this.pausedFromUser = fromUser;
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//            mediaPlayer.getVLCVout().detachViews();
            mediaPlayer.pause();
        }
    }

    public void setPausedFromUser(boolean fromUser) {
        this.pausedFromUser = fromUser;
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

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.getVLCVout().detachViews();
        }
        play = false;
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
        play = false;
    }
}
