/**
 *
 */
package cn.cj.media.video;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import cn.cj.media.video.player.R;

/**
 * @author June Cheng
 * @date 2015年10月30日 上午12:09:30
 */
public class VideoPlayerGestureListener extends SimpleOnGestureListener {
    private final String TAG = VideoPlayerGestureListener.class.getSimpleName();
    /**
     * 手势
     */
    public static final byte GESTURE_NON = 0x0;
    public static final byte GESTURE_HORIZONTAL = 0x1; // 水平方向手势
    public static final byte GESTURE_VERTICAL = 0x2; // 垂直方向手势

    private byte gestureOrientaion = GESTURE_NON;

    private Context context;
    private boolean gestureDown = false;
    private AudioManager audioMgr;
    private int maxVolume; // 音量最大值
    private float volumePerPixel; // 每个像素表示的音量值
    private float lastVolume = -1; // 以此表示变化后的音量值

    private Callback callback;

    public VideoPlayerGestureListener(Context context) {
        this.context = context;
        audioMgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumePerPixel = (float) maxVolume / MediaUtil.getDM(context).widthPixels; // 以屏幕宽度为最大滑动距离
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG, "onSingleTapUp " + e.getAction());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(TAG, "isControlBarShowing=" + " , onSingleTapConfirmed -> " + e.getAction());
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            callback.onSimpleTap();
        }
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, "onDoubleTap " + e.getAction());
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d(TAG, "onDoubleTapEvent " + e.getAction());
        if (e.getAction() == MotionEvent.ACTION_UP) {

        }
        return super.onDoubleTapEvent(e);
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d(TAG, "onShowPress " + e.getAction());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i(TAG, "e1:x=" + e1.getRawX() + ",y=" + e1.getRawY() + ", e2:x=" + e2.getRawX() + ",y=" + e2.getRawY() + ",  distanceX=" + distanceX + ", distanceY=" + distanceY);
        float x = e2.getRawX() - e1.getRawX();
        float y = e2.getRawY() - e1.getRawY();
        Log.i(TAG, "x dis = " + x + ", y dis = " + y);
        if (gestureDown) {// 1、首先，判定此次手势方向
            Log.d(TAG, "onScroll gestureDown");
            if (Math.abs(y) - Math.abs(x) > 1) {
                gestureOrientaion = GESTURE_VERTICAL;
                if(callback != null){
                    callback.onVertical();
                }
            } else if (Math.abs(x) - Math.abs(y) > 1) {
                if(callback != null){
                    callback.onHorizontal();
                }
            }
            gestureDown = false;
        } else { // 2、然后，处理

            // 上下方向手势
            if (gestureOrientaion == GESTURE_VERTICAL) {
                if (lastVolume == -1) {
                    lastVolume = audioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
                }
                float volume = lastVolume + (distanceY * volumePerPixel);
                if (volume < 0) {
                    volume = 0;
                }
                if (volume > maxVolume) {
                    volume = maxVolume;
                }
//                if (volume > 0) {
//                    mVolumePercent.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_lock_silent_mode_off), null,
//                            null, null);
//                } else {
//                    mVolumePercent.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_lock_silent_mode), null, null,
//                            null);
//                }
//                mVolumePercent.setText((int) (volume / maxVolume * 100) + "%");
                audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, (int) volume, 0);
                lastVolume = volume;
            }
            // 左右方向手势
            else if (gestureOrientaion == GESTURE_HORIZONTAL) {
//                int timePerPixel = duration / MediaUtil.getDM(getContext()).widthPixels; // 每个像素代表的播放时长
//                positionToSeek = lastPosition + (int) x * timePerPixel; //根据X计算快进/后退时间
//                if (positionToSeek < 0) {
//                    positionToSeek = 0;
//                }
//                if (positionToSeek > duration) {
//                    positionToSeek = duration;
//                }
//                String b = MediaUtil.formatMillisTime(positionToSeek) + "/" + MediaUtil.formatMillisTime(duration);
//                mFastForwardProgresText.setText(b);
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
        Log.d(TAG, "onDown X=" + e.getX() + ", Y=" + e.getY() + ", rawX=" + e.getRawX() + ", rawY=" + e.getRawY());
//        int delta = playerControlBar.getHeight();
//        Log.d(TAG, "PlayerBottomBar.getHeight() " + delta);
        // 设置手势有效区域 if nessesary
//			if (e.getX() > delta && e.getY() > delta && mPlayerWidth - e.getX() > delta && mPlayerHeight - e.getY() > delta) {
//				Log.d(TAG, " gesture is at available region");
        gestureDown = true;
//			} else {
        // any down action will result in setting gestureDown true on the available rect region
//				gestureDown = false;
//				Log.d(TAG, " gesture is unavailable ");
//			}
        return true;
    }

    interface Callback {
        public void onSimpleTap();
        public void onVertical();
        public void onHorizontal();
    }
}
