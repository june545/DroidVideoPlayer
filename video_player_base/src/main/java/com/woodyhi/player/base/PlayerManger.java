package com.woodyhi.player.base;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.Vector;

/**
 * 播放器管理器
 *
 * @author June
 * @date 2019-06-07
 */
public class PlayerManger {
    private static final String TAG = PlayerManger.class.getSimpleName();

    MediaPlayer mMediaPlayer;
    SurfaceHolder surfaceHolder;
    PlaybackInfo playbackInfo;
    private boolean isSeekable = true;
    private boolean isValidMediaPlayer;
    Vector<PlayerListener> playerListeners = new Vector<>();

    public PlayerManger() {
    }

    public void addPlayerListener(PlayerListener listener) {
        this.playerListeners.add(listener);
    }

    private MediaPlayer createMediaPlayer(Surface surface) {
        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setSurface(surface);
        mediaPlayer.setScreenOnWhilePlaying(true);// works when setDisplay invoked

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                LogUtil.d(TAG, "onPrepared: ---------");
//                if (lastPosition > 0) {
//                    mp.seekTo(lastPosition);
//                } else {
                mp.start();
//                }
            }
        });
        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {

            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                LogUtil.d(TAG, "onVideoSizeChanged: width=" + width + ", height=" + height);
                if (width * height == 0) return;
                for (PlayerListener listener : playerListeners) {
                    listener.onVideoSizeChanged(width, height);
                }
            }
        });
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
//                Log.d(TAG, tagPrefix + "mediaplayer buffered progress : " + percent + "%");
//                if (onBufferingUpdateListener != null) {
//                    onBufferingUpdateListener.onBufferingUpdate(mp, percent);
//                }
                for (PlayerListener listener : playerListeners) {
                    listener.onBufferingUpdate(percent);
                }
            }
        });
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {

            @Override
            public void onSeekComplete(MediaPlayer mp) {
                LogUtil.d(TAG, "onSeekComplete");
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtil.d(TAG, "onCompletion: " + mp);
                for (PlayerListener listener : playerListeners) {
                    listener.onCompletion();
                }
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
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        /* 遇到过这个事件，视频一直不动，mediaplayer状态是playing、网络正常，不知道是不是视频流的问题 */
                        LogUtil.d(TAG, "info MEDIA_INFO_BUFFERING_START");
//                        updateLoadingState(true);
                        start = System.currentTimeMillis();
                        for (PlayerListener listener : playerListeners) {
                            listener.onBufferingStart();
                        }
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        LogUtil.d(TAG, "info MEDIA_INFO_BUFFERING_END");
//                        updateLoadingState(false);
                        end = System.currentTimeMillis();
                        for (PlayerListener listener : playerListeners) {
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

    public void surfaceCreated(SurfaceHolder holder) {
        LogUtil.d(TAG, "--- surfaceCreated");
        this.surfaceHolder = holder;
        loadMedia(holder.getSurface());
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.d(TAG, "--- surfaceDestroyed");
        this.surfaceHolder = null;
        if(mMediaPlayer != null)
            mMediaPlayer.setSurface(null);
    }

    public void setPlaybackInfo(PlaybackInfo info) {
        this.playbackInfo = info;
    }

    public void playback() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        if (surfaceHolder != null)
            loadMedia(surfaceHolder.getSurface());
    }

    public int getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    public int getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public boolean isSeekable() {
        return isSeekable;
    }

    public void seekTo(int seekto) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(seekto);
        }
    }

    public boolean isPlaying() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying())
            return true;
        return false;
    }

    public void pause() {
        if (mMediaPlayer != null)
            mMediaPlayer.pause();
    }
}
