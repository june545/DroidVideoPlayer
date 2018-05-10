package cn.woodyjc.media.video.player;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.util.Log;
import android.view.OrientationEventListener;

/**
 * 播放器随设备旋转
 *
 * @author June Cheng
 * @date 2015年9月21日 上午12:28:14
 */
public class MyOrientationEventListener extends OrientationEventListener {
    private final String TAG = MyOrientationEventListener.class.getSimpleName();

    private static final int DELTA_DEGREES = 10;

    private int mLastScreenOrientation = -1;
    private int mCurrentScreenOrientation;

    private Context mContext;

    public MyOrientationEventListener(Context context) {
        super(context);
        this.mContext = context;
    }

    public MyOrientationEventListener(Context context, int rate) {
        super(context, rate);
        this.mContext = context;
    }

    @Override
    public void onOrientationChanged(int orientation) {
        //	Log.v(TAG, "onOrientationChanged to orientation in " + orientation + " degrees");

        // 阻止微小旋转时屏幕跟着旋转
        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
            // ▲当orientation 接近水平位置时，可能传递的值
            return;
        } else if (DELTA_DEGREES < orientation && orientation < 90 - DELTA_DEGREES) {
            return;
        } else if (90 + DELTA_DEGREES < orientation && orientation < 180 - DELTA_DEGREES) {
            return;
        } else if (180 + DELTA_DEGREES < orientation && orientation < 270 - DELTA_DEGREES) {
            return;
        } else if (270 + DELTA_DEGREES < orientation && orientation < 360 - DELTA_DEGREES) {
            return;
        }

        // 允许屏幕旋转的角度
        if (360 - 15 < orientation || orientation < 15) {
            mCurrentScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            Log.v(TAG, "SCREEN_ORIENTATION_PORTRAIT " + mCurrentScreenOrientation);

        } else if (270 - 15 < orientation && orientation < 270 + 15) {
            mCurrentScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            Log.v(TAG, "SCREEN_ORIENTATION_LANDSCAPE " + mCurrentScreenOrientation);

        } else if (180 - 15 < orientation && orientation < 180 + 15) {
            mCurrentScreenOrientation = Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO
                    ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            Log.v(TAG, "SCREEN_ORIENTATION_REVERSE_PORTRAIT " + mCurrentScreenOrientation);

        } else if (90 - 15 < orientation && orientation < 90 + 15) {
            mCurrentScreenOrientation = Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO
                    ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            Log.v(TAG, "SCREEN_ORIENTATION_REVERSE_LANDSCAPE " + mCurrentScreenOrientation);
        }

        if (mLastScreenOrientation != mCurrentScreenOrientation) {
            ((Activity) mContext).setRequestedOrientation(mCurrentScreenOrientation);
            mLastScreenOrientation = mCurrentScreenOrientation;
        }
    }

}
