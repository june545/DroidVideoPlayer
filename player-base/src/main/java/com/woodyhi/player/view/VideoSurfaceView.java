package com.woodyhi.player.view;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.woodyhi.player.base.LogUtil;
import com.woodyhi.player.internal.PlayerManger;

/**
 * @author June
 * @date 2019-06-09
 */
public class VideoSurfaceView extends AutoResizeSurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = VideoSurfaceView.class.getSimpleName();

    PlayerManger playerManger;

    public VideoSurfaceView(Context context) {
        this(context, null);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
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

    public void setPlayerManager(PlayerManger playerManager) {
        this.playerManger = playerManager;
    }

}
