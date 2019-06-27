package com.woodyhi.player.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    protected boolean pausedFromUser;

    public void addPlayerCallback(PlayerCallback listener) {
        this.playerCallbacks.add(listener);
    }

    public void removePlayerCallback(PlayerCallback callback) {
        playerCallbacks.remove(callback);
    }

    protected Surface getSurface() {
        Surface _surface = mSurface;
        if (_surface != null) return _surface;

        if (surfaceView != null) {
            _surface = surfaceView.getHolder().getSurface();
            if (_surface.isValid())
                return _surface;
        }

        if (textureView != null && textureView.isAvailable()) {
            _surface = new Surface(textureView.getSurfaceTexture());
            return _surface;
        }
        return null;
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
        if (fromUser || !pausedFromUser) {
            pausedFromUser = false;
            play();
        }
    }

    public void pause(boolean fromUser) {
        if (fromUser)
            this.pausedFromUser = fromUser;
        pause();
    }

    public void takeSnapshot(Context context) {
        long time = System.currentTimeMillis();
        try {
            Bitmap bitmap = textureView.getBitmap();
            saveBitmap(bitmap, context.getExternalCacheDir().getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.d(TAG, "takeSnapshot: --------- time " + (System.currentTimeMillis() - time));
    }

    public void saveBitmap(Bitmap bitmap, String path) {
        File filePic;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        } else {
            Log.d("xxx", "saveBitmap: 1return");
            return;
        }
        try {
            filePic = new File(path);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("xxx", "saveBitmap: 2return");
            return;
        }
        Log.d("xxx", "saveBitmap: " + filePic.getAbsolutePath());
    }
}
