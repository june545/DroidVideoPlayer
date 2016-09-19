/**
 * 
 */
package cn.cj.media.video;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * @author June Cheng
 * @date 2015年10月30日 上午12:09:30
 */
public class PlayerGestureListener extends SimpleOnGestureListener {
	private final String	TAG		= "PlayerGestureListener";

	private Context			mContext;
	private AudioManager	audioMgr;
	private int				maxVolume;

	private int				currentVolume;
	private float			volumePercent;

	// horizontal
	private int				percent	= 33;

	public PlayerGestureListener(Context context) {
		this.mContext = context;
		audioMgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Log.d(TAG, "onSingleTapUp");
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		Log.d(TAG, "onScroll");

		// 垂直方向
		if (Math.abs(distanceY) > Math.abs(distanceX)) {
			if (distanceY > 3) {
				if (volumePercent < 100) {
					volumePercent++;
				}

			} else if (distanceY < -3) {
				if (volumePercent > 0) {
					volumePercent--;
				}
			}
			if (volumePercent > 0) {
			} else {
			}
			int volume = (int) (volumePercent / 100 * maxVolume);
			Log.d(TAG, "Volume " + volume);
			audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
		}
		// 水平
		else {
			Log.d(TAG, "");
			if (distanceX > 3) {
				if (percent < 100) {
					percent++;
				}
			} else if (distanceX < -3) {
				if (percent > 0) {
					percent--;
				}
			}
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		Log.d(TAG, "onDown");
		return true;
	}

}
