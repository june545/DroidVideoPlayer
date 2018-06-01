package cn.woodyjc.media.view;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import cn.woodyjc.media.R;
import cn.woodyjc.media.video.BorderedFrameLayout;
import cn.woodyjc.media.video.PlayerControl;


/**
 * Created by June on 2016/8/22.
 */
public class VideoPlayerViewNew extends BorderedFrameLayout implements SurfaceHolder.Callback, PlayerControl {
    private final String TAG = VideoPlayerViewNew.class.getSimpleName();

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

    private AspectRatioLayout aspectRatioLayout;
    private SurfaceView surfaceView;

    private int videoWidth; // 视频宽
    private int videoHeight; // 视频高

    public VideoPlayerViewNew(Context context) {
        this(context, null);
    }

    public VideoPlayerViewNew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayerViewNew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Log.d(TAG, "---init()---");
        LayoutInflater.from(context).inflate(R.layout.simple_player_view, this);
        aspectRatioLayout = findViewById(R.id.aspect_ratio_layout);
        aspectRatioLayout.setResizeType(AspectRatioLayout.RESIZE_TYPE_FIT_CENTER);

        surfaceView = new SurfaceView(context);
        aspectRatioLayout.addView(surfaceView, 0);
//		setBackgroundColor(Color.BLACK);
        surfaceView.getHolder().addCallback(this);  // maybe this is useful
    }

    public SurfaceView getSurfaceView() {
        return surfaceView;
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
        loadMedia(holder.getSurface());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        isSurfaceValid = false;
    }

    public MediaPlayer getMediaPlayer(){
        return mMediaPlayer;
    }

    private void loadMedia(Surface surface) {
        Log.e(TAG, "loadingMedia");
        if (mMediaPlayer == null) {
            Log.d(TAG, "the mediaplayer instance is null, and creating");
            mMediaPlayer = createMediaPlayer(surface);

        } else {
            Log.d(TAG, "mediaplayer instance is existsing");
            try {
                mMediaPlayer.setSurface(surface);// reset surface
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private MediaPlayer createMediaPlayer(Surface surface) {
        final String tagPrefix = "player -> ";
        final MediaPlayer mMediaPlayer = new MediaPlayer();

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setSurface(surface);
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
                aspectRatioLayout.setVideoSize(width, height);
            }
        });
        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.d(TAG, tagPrefix + "mediaplayer buffered progress : " + percent + "%");
                if(onBufferingUpdateListener != null){
                    onBufferingUpdateListener.onBufferingUpdate(mp, percent);
                }
            }
        });
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {

            @Override
            public void onSeekComplete(MediaPlayer mp) {
                Log.d(TAG, tagPrefix + "onSeekComplete");
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, tagPrefix + "onCompletion " + mp.toString());
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
                        Log.d(TAG, tagPrefix + "info MEDIA_INFO_BUFFERING_END");
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    /* 偶尔这个事件被回调后，视频一直不动，mediaplayer状态是playing、网络正常，不知道是不是视频流的问题 */
                        Log.d(TAG, tagPrefix + "info MEDIA_INFO_BUFFERING_START");
                        break;
                    case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                        Log.d(TAG, tagPrefix + "info MEDIA_INFO_METADATA_UPDATE");
                        break;
                    case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                        Log.d(TAG, tagPrefix + "info MEDIA_INFO_NOT_SEEKABLE");
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
            loadMedia(surfaceView.getHolder().getSurface());
        }
    }

    public void setOnBufferingUpdateListener(MediaPlayer.OnBufferingUpdateListener listener){
        this.onBufferingUpdateListener = listener;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        this.onCompletionListener = listener;
    }
}
