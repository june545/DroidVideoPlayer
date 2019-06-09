package cn.woodyjc.media.video.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;

import cn.woodyjc.android.floatingview.FloatingParams;
import cn.woodyjc.android.floatingview.FloatingView;
import cn.woodyjc.media.video.R;

/**
 * Created by June on 2016/8/14.
 */
public class FloatingViewPlayer implements View.OnTouchListener {
	private static final String TAG = FloatingViewPlayer.class.getSimpleName();
	String path = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_640x360.m4v";
	Context      context;
	FloatingView floatingView;
	View         contentView;
	ImageView    imageButton;

	public FloatingViewPlayer(Context context) {
		this.context = context;
	}

	public void show(String path) {
		this.path = path;
		contentView = createView();

		floatingView = new FloatingView(context);
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		floatingView.setFloatingParams(FloatingParams.getDefault(dm.widthPixels, dm.heightPixels));
		floatingView.addView(contentView);
		floatingView.show();
	}

	MediaPlayer player;


	public View createView() {
		View view = View.inflate(context, R.layout.floating_player_view, null);
		SurfaceView surfaceView = (SurfaceView) view.findViewById(R.id.surfaceview);
		imageButton = (ImageView) view.findViewById(R.id.close);
		imageButton.setVisibility(View.GONE);
		surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				Log.d(TAG, "---  surfaceCreated");
				play(holder);
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				Log.d(TAG, "---surfaceChanged width=" + width + ",height=" + height);
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				Log.d(TAG, "---  surfaceDestroyed");
			}
		});
		surfaceView.setOnTouchListener(this);
		imageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				player.stop();
				player.release();
				player = null;
				if (floatingView != null) {
					floatingView.remove();
					floatingView = null;
				}
			}
		});
		return view;
	}

	private void play(SurfaceHolder holder) {
		player = new MediaPlayer();
		try {
			player.setDisplay(holder);
			player.setDataSource(path);
			player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
				}
			});
			player.prepareAsync();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	boolean moveing;
	float   lastX;
	float   lastY;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				moveing = false;
				lastX = event.getX();
				lastY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				float x = event.getX();
				float y = event.getY();
				if (x - lastX > 5 || y - lastY > 5) {
					moveing = true;
				}
				break;
			case MotionEvent.ACTION_UP:
				if (!moveing) // 判断点击事件
					changeButtonState();
				break;
		}
		return true;
	}

	private void changeButtonState() {
		if (imageButton.getVisibility() == View.VISIBLE) {
			player.start();
			imageButton.setVisibility(View.GONE);
		} else {
			player.pause();
			imageButton.setVisibility(View.VISIBLE);
		}
	}
}

