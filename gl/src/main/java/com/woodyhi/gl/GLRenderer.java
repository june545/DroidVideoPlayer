package com.woodyhi.gl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by June on 2018/5/15.
 */
public class GLRenderer implements GLSurfaceView.Renderer {

    private boolean mFirstDraw;
    private boolean mSurfaceCreated;
    private int mWidth;
    private int mHeight;
    private long mLastTime;
    private int mFPS;

    public GLRenderer() {
        mFirstDraw = true;
        mSurfaceCreated = false;
        mWidth = -1;
        mHeight = -1;
        mLastTime = System.currentTimeMillis();
        mFPS = 0;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i("zzz", "Surface created.");
        mSurfaceCreated = true;
        mWidth = -1;
        mHeight = -1;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (!mSurfaceCreated && width == mWidth && height == mHeight) {
                Log.i("zzz", "Surface changed but already handled.");
            return;
        }

//        if (Util.DEBUG) {
            // Android honeycomb has an option to keep the
            // context.
//            String msg = "Surface changed width:" + width
//                    + " height:" + height;
//            if (mSurfaceCreated) {
//                msg += " context lost.";
//            } else {
//                msg += ".";
//            }
//            Log.i("zzz", msg);
//        }

        mWidth = width;
        mHeight = height;

        onCreate(mWidth, mHeight, mSurfaceCreated);
        mSurfaceCreated = false;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        onDrawFrame(mFirstDraw);

//        if (Util.DEBUG) {
            mFPS++;
            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastTime >= 1000) {
                mFPS = 0;
                mLastTime = currentTime;
            }
//        }

        if (mFirstDraw) {
            mFirstDraw = false;
        }
    }

    public int getFPS() {
        return mFPS;
    }

    public void onCreate(int width, int height, boolean contextLost) {
        GLES20.glClearColor(0f, 0f, 0f, 1f);
    }

    public void onDrawFrame(boolean firstDraw) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

//    public abstract void onCreate(int width, int height, boolean contextLost);

//    public abstract void onDrawFrame(boolean firstDraw);
}
