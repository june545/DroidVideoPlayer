package com.woodyhi.gl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;
import android.view.Surface;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by June on 2018/5/15.
 */
public class MediaPlayerRenderer implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    private static final float[] VERTEX_COORDINATE = new float[]{
            -1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f
    };

    private static final float[] TEXTURE_COORDINATE = new float[]{
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    };

    private static ByteBuffer vertexByteBuffer;
    private static ByteBuffer textureByteBuffer;

    static {
        vertexByteBuffer = ByteBuffer.allocateDirect(VERTEX_COORDINATE.length * 4);
        vertexByteBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer().put(VERTEX_COORDINATE);
        vertexByteBuffer.rewind();

        textureByteBuffer = ByteBuffer.allocateDirect(TEXTURE_COORDINATE.length * 4);
        textureByteBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer().put(TEXTURE_COORDINATE);
        textureByteBuffer.rewind();
    }

    private final String mVertexShader =
            "uniform mat4 uMVPMatrix;\n" +
                    "uniform mat4 uSTMatrix;\n" +
                    "attribute vec4 aPosition;\n" +
                    "attribute vec4 aTextureCoord;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "void main() {\n" +
                    "  gl_Position = uMVPMatrix * aPosition;\n" +
                    "  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n" +
                    "}\n";

    private final String mFragmentShader =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform samplerExternalOES sTexture;\n" +
                    "void main() {\n" +
                    "  gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n";

    private static final String VERTEX_SHADER = "" +
            "attribute vec4 aPosition;\n" +
            "attribute vec2 aTexCoord;\n" +
            "\n" +
            "varying vec2 vTexCoord;\n" +
            "\n" +
            "void main() {\n" +
            "    vTexCoord = aTexCoord;\n" +
            "    gl_Position = aPosition;\n" +
            "}";

    private static final String FRAGMENT_SHADER_OES = "" +
            "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;\n" +
            "uniform samplerExternalOES sTexture;\n" +
            "uniform sampler2D sTexture2D;\n" +
            "uniform int tex;\n" +
            "\n" +
            "varying vec2 vTexCoord;\n" +
            "\n" +
            "void main() {\n" +
            "    vec4 tc;\n" +
            "    if (tex == 0) {\n" +
            "        tc = texture2D(sTexture, vTexCoord);\n" +
            "    } else {\n" +
            "        tc = texture2D(sTexture2D, vTexCoord);\n" +
            "    }\n" +
            "    gl_FragColor = tc;\n" +
            "}";

    private boolean mSurfaceCreated;
    private int mWidth;
    private int mHeight;

    private Context mContext;
    private MediaPlayer mediaPlayer;
    private Uri mUri;
    private String videoPath = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";


    int textureId;
    int programId;
    private SurfaceTexture surfaceTexture;
    private boolean updateSurface;

    // Renderer Properties
    private float[] mMVPMatrix = new float[16];
    private float[] mSTMatrix = new float[16];


    public MediaPlayerRenderer(Context context) {
        mSurfaceCreated = false;
        mWidth = -1;
        mHeight = -1;

        this.mContext = context;
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i("zzz", "Surface created.");
        mSurfaceCreated = true;
        mWidth = -1;
        mHeight = -1;


        textureId = createVideoTexture();
        programId = createProgram();

        //        aPositionLocation= GLES20.glGetAttribLocation(programId,"aPosition");
        //        uMatrixLocation=GLES20.glGetUniformLocation(programId,"uMatrix");
        //        uSTMMatrixHandle = GLES20.glGetUniformLocation(programId, "uSTMatrix");
        //        uTextureSamplerLocation=GLES20.glGetUniformLocation(programId,"sTexture");
        //        aTextureCoordLocation=GLES20.glGetAttribLocation(programId,"aTexCoord");


        // Create SurfaceTexture that will feed this textureId and pass to MediaPlayer
        surfaceTexture = new SurfaceTexture(textureId);
        surfaceTexture.setOnFrameAvailableListener(this);//监听是否有新的一帧数据到来
        Surface surface = new Surface(surfaceTexture);
        mediaPlayer.setSurface(surface);
        surface.release();

        try {
            mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    Log.d("xxx", " width " + width + ", height " + height);
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e("error-", "zzzz " + what + "vvvvv " + extra);
                    return false;
                }
            });
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        synchronized (this) {
            updateSurface = false;
        }

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (!mSurfaceCreated && width == mWidth && height == mHeight) {
            Log.i("zzz", "Surface changed but already handled.");
            return;
        }


        mWidth = width;
        mHeight = height;

        // Set the OpenGL viewport to file the entire surface
        GLES20.glViewport(0, 0, width, height);
        //        Matrix.setIdentityM(mMVPMatrix, 0);

        //        mSurfaceCreated = false;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        synchronized (this) {
            if (updateSurface) {
                surfaceTexture.updateTexImage();//获取新数据
                surfaceTexture.getTransformMatrix(mSTMatrix);//让新的纹理和纹理坐标系能够正确的对应,mSTMatrix的定义是和projectionMatrix完全一样的。
                updateSurface = false;
            }
        }

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);


        GLES20.glUseProgram(programId);
        int positionIndex = GLES20.glGetAttribLocation(programId, "aPosition");
        int texcoordIndex = GLES20.glGetAttribLocation(programId, "aTexCoord");

        GLES20.glEnableVertexAttribArray(positionIndex);
        GLES20.glEnableVertexAttribArray(texcoordIndex);

        GLES20.glVertexAttribPointer(positionIndex, 3, GLES20.GL_FLOAT, false, 0, vertexByteBuffer);
        GLES20.glVertexAttribPointer(texcoordIndex, 2, GLES20.GL_FLOAT, false, 0, textureByteBuffer);

        //        GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "tex"), tex);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
        int sTexture = GLES20.glGetUniformLocation(programId, "sTexture");
        GLES20.glUniform1i(sTexture, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        //        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture2D);
        int sTexture2D = GLES20.glGetUniformLocation(programId, "sTexture2D");
        GLES20.glUniform1i(sTexture2D, 1);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(positionIndex);
        GLES20.glDisableVertexAttribArray(texcoordIndex);


        //        GLES20.glViewport(0, 0, screenWidth, screenHeight);
    }

    // Uniform locations
    private int uMVPMatrixLocation;
    private int uSTMatrixLocation;


    public void onCreate(int width, int height, boolean contextLost) {
        GLES20.glClearColor(0f, 0f, 0f, 1f);
    }

    public void onDrawFrame(boolean firstDraw) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        updateSurface = true;
        //        synchronized (this){
        //            if (updateSurface){
        //                surfaceTexture.updateTexImage();
        //                surfaceTexture.getTransformMatrix(mSTMatrix);
        //                updateSurface = false;
        //            }
        //        }

        //        synchronized (this){
        //            surfaceTexture.updateTexImage();
        //        }
    }


    private int createVideoTexture() {
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);

        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }

    private int createShader(int shaderType, String shaderSource) {
        int shader = GLES20.glCreateShader(shaderType);
        checkGLESError("create shader " + shaderType);
        GLES20.glShaderSource(shader, shaderSource);
        GLES20.glCompileShader(shader);
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            GLES20.glDeleteShader(shader);
            checkGLESError("compile shader " + GLES20.glGetShaderInfoLog(shader));
        }

        return shader;
    }

    private int createProgram() {
        int program = GLES20.glCreateProgram();
        checkGLESError("create program");

        int vertexShader = createShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER);
        int fragmentShader = createShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_OES);
        GLES20.glAttachShader(program, vertexShader);
        //        checkGLESError("attach shader " + vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        //        checkGLESError("attach shader " + fragmentShader);

        GLES20.glLinkProgram(program);
        int[] linked = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linked, 0);
        if (linked[0] == 0) {
            //            checkGLESError("link program");
        }

        return program;
    }


    private void checkGLESError(String what) {
        int error = GLES20.glGetError();
        if (error != GLES20.GL_NO_ERROR) {
            //            Log.e(TAG, "checkGLESError() called with: what = [" + what + "]");
            throw new RuntimeException(what + ": " + GLU.gluErrorString(error));
        }
    }
}
