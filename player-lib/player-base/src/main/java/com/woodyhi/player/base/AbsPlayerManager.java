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

    public abstract void surfaceCreated(SurfaceView surfaceView, SurfaceHolder holder);

    public abstract void surfaceDestroyed(SurfaceView surfaceView, SurfaceHolder holder);

    public void setVideoView(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        this.surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurface = holder.getSurface();
                isSurfaceValid = true;
                onSurfaceCreated(holder.getSurface());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
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
                LogUtil.d("AbsPlayerManager", "onSurfaceTextureAvailable: ---------" + width + "   " + height);
                mSurface = new Surface(surface);
                surfaceTexture = surface;
                isSurfaceValid = true;
                onSurfaceCreated(mSurface);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                LogUtil.d("AbsPlayerManager", "onSurfaceTextureSizeChanged: -----------------");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                LogUtil.d("AbsPlayerManager", "onSurfaceTextureDestroyed: -----------------");
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

    @Override
    public void pause(boolean fromUser) {

    }
}
