package com.woodyhi.player.base;

import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import java.util.Vector;

/**
 * @auth June
 * @date 2019/06/10
 */
public abstract class AbsPlayerManager implements BaseController {
    private final String TAG = AbsPlayerManager.class.getSimpleName();

    protected Vector<PlayerCallback> playerCallbacks = new Vector<>();

    protected SurfaceView surfaceView;
    protected TextureView textureView;
    protected Surface mSurface;
    protected volatile boolean isSurfaceValid;
    protected SurfaceTexture surfaceTexture;

    public void addPlayerCallback(PlayerCallback listener) {
        this.playerCallbacks.add(listener);
    }

    public void removePlayerCallback(PlayerCallback callback) {
        playerCallbacks.remove(callback);
    }

    public void setVideoView(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        this.surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                LogUtil.d(TAG, "surfaceCreated: ---------");
                mSurface = holder.getSurface();
                isSurfaceValid = true;
                onSurfaceCreated(holder.getSurface());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                LogUtil.d(TAG, "surfaceDestroyed: ---------");
                mSurface = null;
                isSurfaceValid = false;
                onSurfaceDestroyed();
            }
        });
    }

    public void setVideoView(TextureView textureView) {
        this.textureView = textureView;
        textureView.setBackgroundColor(Color.parseColor("#eeff0000"));
        this.textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                LogUtil.d(TAG, "onSurfaceTextureAvailable: ---------" + width + "   " + height);
                mSurface = new Surface(surface);
                surfaceTexture = surface;
                isSurfaceValid = true;
                onSurfaceCreated(mSurface);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                LogUtil.d(TAG, "onSurfaceTextureSizeChanged: -----------------");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                LogUtil.d(TAG, "onSurfaceTextureDestroyed: -----------------");
                mSurface = null;
                surfaceTexture = null;
                isSurfaceValid = false;
                onSurfaceDestroyed();
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        });
    }

    protected void onSurfaceCreated(Surface surface) {
    }

    protected void onSurfaceDestroyed() {
    }

    public void play(boolean fromUser) {
    }

    public void pause(boolean fromUser) {
    }
}
