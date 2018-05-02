package cn.woodyjc.media.video;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * Created by June on 2016/8/22.
 */
public class VideoPlayerView extends VideoFrameLayout implements SurfaceHolder.Callback, PlayerControl {
    private final String TAG = VideoPlayerView.class.getSimpleName();

    private MediaPlayer.OnPreparedListener         onPreparedListener;
    private MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener;
    private MediaPlayer.OnBufferingUpdateListener  onBufferingUpdateListener;
    private MediaPlayer.OnSeekCompleteListener     onSeekCompleteListener;
    private MediaPlayer.OnCompletionListener       onCompletionListener;
    private MediaPlayer.OnInfoListener             onInfoListener;
    private MediaPlayer.OnErrorListener            onErrorListener;

    private MediaPlayer mMediaPlayer;
    private boolean     isSurfaceValid;

    private String mediaPath;
    private int    lastPosition;

    private VideoSurfaceView surfaceView;

    private int videoWidth; // 视频分辨率-宽
    private int videoHeight; // 视频分辨率-高

    public VideoPlayerView(Context context) {
        super(context, null);
        init();
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public VideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Log.d(TAG, "---init()---");
        surfaceView = new VideoSurfaceView(getContext());
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        this.addView(surfaceView, lp);
//		setBackgroundColor(Color.BLACK);
        surfaceView.getHolder().addCallback(this);  // maybe this is useful
    }

    public SurfaceView getSurfaceView() {
        return surfaceView;
    }

    public void updateSurfaceViewSize(int w, int h) {
        // 根据视频分辨率计算SurfaceView尺寸大小
        surfaceView.setVideoSize(w, h);
        surfaceView.requestLayout();
        surfaceView.invalidate();
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        String _format = "other/unknown";
        if (format == PixelFormat.RGBX_8888)
            _format = "RGBX_8888";
        else if (format == PixelFormat.RGB_565)
            _format = "RGB_565";
        else if (format == ImageFormat.YV12)
            _format = "YV12";

        Log.d(TAG, "surfaceChanged -> PixelFormat is " + _format + ", width = " + width + ", height = " + height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        isSurfaceValid = true;
        loadMedia(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        isSurfaceValid = false;
    }

    public MediaPlayer getMediaPlayer(){
        return mMediaPlayer;
    }

    private void loadMedia(SurfaceHolder holder) {
        Log.e(TAG, "loadingMedia");
        if (mMediaPlayer == null) {
            Log.d(TAG, "the mediaplayer instance is null, and creating");
//            playPauseBtn.setBackgroundResource(R.drawable.media_pause);
            mMediaPlayer = createMediaPlayer(holder);

        } else {
            Log.d(TAG, "mediaplayer instance is existsing");
            try {
                mMediaPlayer.setDisplay(holder);// reset surface
//                if (playingOnSurface) {
//                    mLoadingView.setVisibility(View.VISIBLE);
//                    playPauseBtn.setBackgroundResource(R.drawable.media_pause);
//                    mMediaPlayer.seekTo(lastPosition);
//                }
//                showOverlay(playingOnSurface);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private MediaPlayer createMediaPlayer(SurfaceHolder holder) {
        final String tagPrefix = "player -> ";
        final MediaPlayer mMediaPlayer = new MediaPlayer();
//        final MediaPlayer mMediaPlayer = MediaPlayer.create(getContext(), Uri.parse(mediaPath));

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDisplay(holder);
        mMediaPlayer.setScreenOnWhilePlaying(true);// works when setDisplay invoked

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, tagPrefix + "onPrepared");
                if (lastPosition > 0) {
                    Log.i(TAG, tagPrefix + "seekTo " + lastPosition);
                    mMediaPlayer.seekTo(lastPosition);
                } else {
                    Log.i(TAG, tagPrefix + "mediaplayer start");
                    mMediaPlayer.start();
                }
//                playingOnSurface = true;
//                playPauseBtn.setEnabled(true);
//                mSeekBar.setEnabled(true);
//                showOverlay(true);
            }
        });
        mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {

            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                Log.d(TAG, tagPrefix + "video size changed width=" + width + ", height=" + height);
                if (width * height == 0) {
                    return;
                }
                videoWidth = width;
                videoHeight = height;
                updateSurfaceViewSize(width, height);
            }
        });
        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.d(TAG, tagPrefix + "mediaplayer buffered progress : " + percent + "%");
//                mSeekBar.setSecondaryProgress(mSeekBar.getMax() * percent / 100);
                if(onBufferingUpdateListener != null){
                    onBufferingUpdateListener.onBufferingUpdate(mp, percent);
                }
            }
        });
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {

            @Override
            public void onSeekComplete(MediaPlayer mp) {
                Log.d(TAG, tagPrefix + "onSeekComplete");
//                mLoadingView.setVisibility(View.GONE);
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, tagPrefix + "onCompletion " + mp.toString());
//                completed = true;
                if(onCompletionListener != null){
                    onCompletionListener.onCompletion(mp);
                }
            }
        });
        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {

            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                Log.d(TAG, tagPrefix + "onInfo what = " + what + ", extra = " + extra);
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                        Log.d(TAG, tagPrefix + "info MEDIA_INFO_BAD_INTERLEAVING");
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                        if (mLoadingView.getVisibility() == View.VISIBLE)
//                            mLoadingView.setVisibility(View.GONE);
                        Log.d(TAG, tagPrefix + "info MEDIA_INFO_BUFFERING_END");
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                        if (mLoadingView.getVisibility() == View.GONE)
//                            mLoadingView.setVisibility(View.VISIBLE);
                    /* 偶尔这个事件被回调后，视频一直不动，mediaplayer状态是playing、网络正常，不知道是不是视频流的问题 */
                        Log.d(TAG, tagPrefix + "info MEDIA_INFO_BUFFERING_START");
                        break;
                    case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                        Log.d(TAG, tagPrefix + "info MEDIA_INFO_METADATA_UPDATE");
                        break;
                    case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
//                        mSeekBar.setEnabled(false);
                        Log.d(TAG, tagPrefix + "info MEDIA_INFO_NOT_SEEKABLE");
//                        mSeekBarLayout.setVisibility(View.INVISIBLE);
                        break;
                    case MediaPlayer.MEDIA_INFO_UNKNOWN:
                        Log.d(TAG, tagPrefix + "info MEDIA_INFO_UNKNOWN");
                        break;
                    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                        Log.d(TAG, tagPrefix + "info MEDIA_INFO_VIDEO_TRACK_LAGGING");
                        break;
                }
                return false;
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e(TAG, tagPrefix + "onError what = " + what + ", extra = " + extra);
                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                        Log.e(TAG, tagPrefix + "error MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK");
                        break;
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        Log.e(TAG, tagPrefix + "error MEDIA_ERROR_SERVER_DIED");
                        break;
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        Log.e(TAG, tagPrefix + "error MEDIA_ERROR_UNKNOWN");
//                        mLoadingView.setVisibility(View.GONE);
//                        playPauseBtn.setEnabled(true);
                        Toast.makeText(getContext(), "未知错误", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        //设置显示视频显示在SurfaceView上
        try {
            mMediaPlayer.setDataSource(mediaPath);
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mMediaPlayer;
    }

    public int getDuration(){
        if(mMediaPlayer != null){
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    public int getCurrentPosition(){
        if(mMediaPlayer != null){
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void playback() {
        if(mMediaPlayer != null && !mMediaPlayer.isPlaying()){
            mMediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
        }
    }

    @Override
    public void seekTo(int msec) {
        if(mMediaPlayer != null)
            mMediaPlayer.seekTo(msec);
    }

    @Override
    public void fastforward(int msec) {
        seekTo(msec);
    }

    @Override
    public void rewind(int msec) {
        seekTo(msec);
    }

    @Override
    public boolean isPlaying(){
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            return true;
        }
        return false;
    }

    @Override
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void play(String path, int playedTime) {
        this.mediaPath = path;
        this.lastPosition = playedTime;
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (isSurfaceValid) {
//            mLoadingView.setVisibility(View.VISIBLE);
            loadMedia(getSurfaceView().getHolder());
        }
    }

    public void setOnBufferingUpdateListener(MediaPlayer.OnBufferingUpdateListener listener){
        this.onBufferingUpdateListener = listener;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        this.onCompletionListener = listener;
    }
}
