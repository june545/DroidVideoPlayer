package com.woodyhi.gl;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.woodyhi.gl.screenshot.ScreenShot;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView mSurfaceView;
    private GLSurfaceView mGLView;

    private String videoPath = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        mGLView = new GLSurfaceView(this);
        mGLView.setEGLContextClientVersion(2);
        mGLView.setPreserveEGLContextOnPause(true);
//        mGLView.setRenderer(new Render01());
//        mGLView.setRenderer(new MyRender(this));
        final VideoRender videoRender = new VideoRender(this);
        final MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(videoPath);
            videoRender.setMediaPlayer(mp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mGLView.setRenderer(videoRender);
        setContentView(mGLView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mp.pause();
            }
        }, 30000);

        mGLView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                videoRender.shot();
                ScreenShot.shotNew(320, 480);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
         * The activity must call the GL surface view's
         * onResume() on activity onResume().
         */
        if (mSurfaceView != null) {
            mSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        /*
         * The activity must call the GL surface view's
         * onPause() on activity onPause().
         */
        if (mSurfaceView != null) {
            mSurfaceView.onPause();
        }
    }


    private void setUpMP(){
        MediaPlayer mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(videoPath));
        }catch (IOException e){
            e.printStackTrace();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

}
