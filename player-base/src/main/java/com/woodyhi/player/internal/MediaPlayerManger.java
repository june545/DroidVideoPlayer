package com.woodyhi.player.internal;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.LogUtil;
import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.base.PlayerCallback;

import java.io.IOException;

/**
 * 播放器管理器
 *
 * @author June
 * @date 2019-06-07
 */
public class MediaPlayerManger extends AbsPlayerManager {
    private static final String TAG = MediaPlayerManger.class.getSimpleName();

    MediaPlayer mMediaPlayer;
    SurfaceHolder surfaceHolder;
    PlaybackInfo playbackInfo;
    private boolean isSeekable = true;
    private boolean isValidMediaPlayer;

    public MediaPlayerManger() {
    }

    private MediaPlayer createMediaPlayer(Surface surface) {
        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setSurface(surface);
        mediaPlayer.setScreenOnWhilePlaying(true);// works when setDisplay invoked

        mediaPlayer.setOnPreparedListener(mp -> {
            LogUtil.d(TAG, "onPrepared: ---------");
//                if (lastPosition > 0) {
//                    mp.seekTo(lastPosition);
//                } else {
            mp.start();
//                }
        });
        mediaPlayer.setOnVideoSizeChangedListener((mp, width, height) -> {
            LogUtil.d(TAG, "onVideoSizeChanged: width=" + width + ", height=" + height);
            if (width * height == 0) return;
            for (PlayerCallback listener : playerCallbacks) {
                listener.onVideoSizeChanged(width, height);
            }
        });
        mediaPlayer.setOnBufferingUpdateListener((mp, percent) -> {
//                Log.d(TAG, tagPrefix + "mediaplayer buffered progress : " + percent + "%");
//                if (onBufferingUpdateListener != null) {
//                    onBufferingUpdateListener.onBufferingUpdate(mp, percent);
//                }
            for (PlayerCallback listener : playerCallbacks) {
                listener.onBufferingUpdate(percent);
            }
        });
        mediaPlayer.setOnSeekCompleteListener(mp -> LogUtil.d(TAG, "onSeekComplete"));
        mediaPlayer.setOnCompletionListener(mp -> {
            LogUtil.d(TAG, "onCompletion: " + mp);
            for (PlayerCallback listener : playerCallbacks) {
                listener.onCompletion();
            }
        });
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            private boolean startFlag;
            private long start;
            private long end;

            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                Log.d(TAG, tagPrefix + "onInfo what = " + what + ", extra = " + extra);
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
//                        Log.d(TAG, tagPrefix + "info MEDIA_INFO_BAD_INTERLEAVING");
                        break;
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
//                        updateLoadingState(false);
                        for (PlayerCallback listener : playerCallbacks) {
                            listener.onRenderingStart();
                        }
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        /* 遇到过这个事件，视频一直不动，mediaplayer状态是playing、网络正常，不知道是不是视频流的问题 */
                        LogUtil.d(TAG, "info MEDIA_INFO_BUFFERING_START");
                        start = System.currentTimeMillis();
                        for (PlayerCallback listener : playerCallbacks) {
                            listener.onBufferingStart();
                        }
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        LogUtil.d(TAG, "info MEDIA_INFO_BUFFERING_END");
                        end = System.currentTimeMillis();
                        for (PlayerCallback listener : playerCallbacks) {
                            listener.onBufferingEnd();
                        }
                        break;
                    case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
//                        Log.d(TAG, tagPrefix + "info MEDIA_INFO_METADATA_UPDATE");
                        break;
                    case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
//                        Log.d(TAG, tagPrefix + "info MEDIA_INFO_NOT_SEEKABLE");
                        isSeekable = false;
                        break;
                    case MediaPlayer.MEDIA_INFO_UNKNOWN:
//                        Log.d(TAG, tagPrefix + "info MEDIA_INFO_UNKNOWN");
                        break;
                    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
//                        Log.d(TAG, tagPrefix + "info MEDIA_INFO_VIDEO_TRACK_LAGGING");
                        break;
                }
                return false;
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
//                Log.e(TAG, tagPrefix + "onError what = " + what + ", extra = " + extra);
                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
//                        Log.e(TAG, tagPrefix + "error MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK");
                        break;
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
//                        Log.e(TAG, tagPrefix + "error MEDIA_ERROR_SERVER_DIED");
                        break;
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
//                        Log.e(TAG, tagPrefix + "error MEDIA_ERROR_UNKNOWN");
//                        Toast.makeText(getContext(), "未知错误", Toast.LENGTH_SHORT).show();
//                        updateLoadingState(false);
                        break;
                }
                return false;
            }
        });

        try {
            if (playbackInfo.path == null) {
                AssetFileDescriptor afd = playbackInfo.assetFileDescriptor;
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            } else {
                mediaPlayer.setDataSource(playbackInfo.path);
            }

            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

    private void loadMedia(Surface surface) {
        if (mMediaPlayer == null) {
            mMediaPlayer = createMediaPlayer(surface);
        } else {
            try {
                mMediaPlayer.setSurface(surface);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.surfaceHolder = holder;
        loadMedia(holder.getSurface());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.surfaceHolder = null;
        if (mMediaPlayer != null) {
            mMediaPlayer.setSurface(null);
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        }
    }


    //-----------------------------------------Controller--------------------------------------
    public void playback(PlaybackInfo info) {
        this.playbackInfo = info;
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            }
            try {
                mMediaPlayer.setDataSource(info.path);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (surfaceHolder != null)
                loadMedia(surfaceHolder.getSurface());
        }
    }

    @Override
    public void playback() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying())
            mMediaPlayer.pause();
    }

    @Override
    public void seekTo(int msec) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(msec);
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
        if (mMediaPlayer != null && mMediaPlayer.isPlaying())
            return true;
        return false;
    }

    @Override
    public boolean isSeekable() {
        return isSeekable;
    }

    @Override
    public int getDuration() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
