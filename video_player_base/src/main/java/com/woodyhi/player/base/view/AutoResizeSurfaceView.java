package com.woodyhi.player.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author June
 */
public class AutoResizeSurfaceView extends SurfaceView {
    private final String TAG = AutoResizeSurfaceView.class.getSimpleName();

    private int videoWidth;
    private int videoHeight;

    public AutoResizeSurfaceView(Context context) {
        this(context, null);
    }

    public AutoResizeSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoResizeSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(videoWidth, widthMeasureSpec);
        int height = getDefaultSize(videoHeight, heightMeasureSpec);
//        Log.d(TAG, "----- onMeasure videoW=" + videoWidth + ", videoH=" + videoHeight + " --- DefaultSize w=" + width + ", h=" + height);

        if (videoWidth != 0 && videoHeight != 0) {
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize;
                height = heightSpecSize;

                if (videoWidth * height > width * videoHeight) {
                    height = width * videoHeight / videoWidth;
                } else if (videoWidth * height < width * videoHeight) {
                    width = height * videoWidth / videoHeight;
                }
//                Log.d(TAG, "-----onMeasure both EXACTLY w=" + width + ", h=" + height);

            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                width = widthSpecSize;
                height = width * videoHeight / videoWidth;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    height = heightSpecSize;
                }
//                Log.d(TAG, "-----onMeasure width EXACTLY w=" + width + ", h=" + height);

            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                height = heightSpecSize;
                width = height * videoWidth / videoHeight;
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    width = widthSpecSize;
                }
//                Log.d(TAG, "-----onMeasure height EXACTLY w=" + width + ", h=" + height);

            } else {
                width = videoWidth;
                height = videoHeight;
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    width = widthSpecSize;
                    height = width * videoHeight / videoWidth;
                }
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    height = heightSpecSize;
                    width = height * videoWidth / videoHeight;
                }
//                Log.d(TAG, "-----onMeasure no EXACTLY w=" + width + ", h=" + height);
            }
        }

//        Log.d(TAG, "----videoplayerview w=" + ((View) getParent()).getWidth() + " h=" + ((View) getParent()).getHeight());
//        Log.d(TAG, "--------------------------------\n\n\n");
        setMeasuredDimension(width, height);
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
    }

    public void updateViewSizeByVideoSize(int videoWidth, int videoHeight) {
        setVideoSize(videoWidth, videoHeight);
        requestLayout();
        invalidate();
    }

}
