package cn.cj.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

import cn.cj.android.extend.FloatingVideo;
import cn.cj.android.floatingview.FloatingView;
import cn.cj.android.floatingview.FloatingViewManager;
import cn.cj.media.video.player.R;

/**
 * Created by June on 2016/8/14.
 */
public class FloatingViewPlayer implements View.OnTouchListener {
	private static final String TAG = FloatingViewPlayer.class.getSimpleName();
	String path = "/storage/emulated/legacy/疯狂原始人_DVDscr中字_51rrkan.com.rmvb";
	Context             context;
	FloatingViewManager floatingViewManager;
	View                view;
	ImageView           imageButton;

	public FloatingViewPlayer(Context context) {
		this.context = context;
	}

	public void show(String path) {
		this.path = path;
		view = createView();
		floatingViewManager = new FloatingViewManager(context);
		FloatingView.Options options = FloatingVideo.create(context);
		floatingViewManager.addScaleVideoViewToWindow(view, options);
	}

	MediaPlayer player;


	public View createView() {
		View view = View.inflate(context, R.layout.video_player_floating_view, null);
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
				if (floatingViewManager != null) {
					floatingViewManager.removeView();
					floatingViewManager = null;
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

