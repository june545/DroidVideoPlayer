package com.woodyhi.player.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.LogUtil;
import com.woodyhi.player.base.PlayerCallback;

/**
 * 设置surface的创建、销毁回调函数，监听视频尺寸来计算SurfaceView
 * @author June
 * @date 2019-06-09
 */
public class VideoSurfaceView extends ResizeSurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = VideoSurfaceView.class.getSimpleName();

    AbsPlayerManager playerManger;

    public VideoSurfaceView(Context context) {
        this(context, null);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
//        setBackgroundColor(Color.parseColor("#55AA053C"));
    }

    private void init(Context context) {
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (playerManger != null)
            playerManger.surfaceCreated(holder);
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

        LogUtil.d(TAG, "surfaceChanged -> PixelFormat is " + _format + ", width = " + width + ", height = " + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (playerManger != null)
            playerManger.surfaceDestroyed(holder);
    }

    public void setPlayerManager(AbsPlayerManager playerManager) {
        this.playerManger = playerManager;
        this.playerManger.addPlayerCallback(new PlayerCallback() {
            @Override
            public void onVideoSizeChanged(int width, int height) {
                updateViewSizeByVideoSize(width, height);
            }
        });
    }


}
