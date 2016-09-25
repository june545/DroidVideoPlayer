package cn.cj.media.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;


/**
 * Created by June on 2016/8/22.
 */
public class VideoPlayerView extends VideoFrameLayout implements SurfaceHolder.Callback {
    String TAG = VideoPlayerView.class.getSimpleName();

    VideoSurfaceView surfaceView;
    SurfaceHolder    surfaceHolder;

    int videoWidth; // 视频分辨率-宽
    int videoHeight; // 视频分辨率-高

    public VideoPlayerView(Context context) {
        this(context, null);
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Log.d(TAG, toString() + "---init()");
        surfaceView = new VideoSurfaceView(getContext());
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        this.addView(surfaceView, lp);
//		setBackgroundColor(Color.BLACK);
        //        surfaceView.getHolder().addCallback(this);  // maybe this is useful
    }

    public boolean isSsurfaceValid() {
        return surfaceHolder != null;
    }

    public SurfaceView getSurfaceView() {
        return surfaceView;
    }

    public void setVideoSize(int w, int h) {
        this.videoWidth = w;
        this.videoHeight = h;
        surfaceView.setVideoSize(w, h);

        // resize
        surfaceView.requestLayout();
        surfaceView.invalidate();
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //        Log.d(TAG, "---surfaceCreated");
        surfaceHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //        Log.d(TAG, "---surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //        Log.d(TAG, "---surfaceDestroyed");
        surfaceHolder = null;
    }


}
