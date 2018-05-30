package cn.woodyjc.media.video.player;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.OrientationEventListener;

/**
 * 设备旋转时，强制旋转屏幕
 * <p>
 * </p>
 * Created by June on 2018/5/30.
 */
public class ScreenOrientationListener extends OrientationEventListener {
    private final String TAG = ScreenOrientationListener.class.getSimpleName();

    private static final int TOLERANCE_DEGREES = 15;

    private Activity activity;

    public ScreenOrientationListener(Activity activity) {
        super(activity, SensorManager.SENSOR_DELAY_NORMAL);
        this.activity = activity;
    }

    // 屏幕角度值范围按顺时针0-360
    @Override
    public void onOrientationChanged(int orientation) {

        int screenOrientation = activity.getRequestedOrientation();

        // 允许屏幕旋转的角度
        if (360 - TOLERANCE_DEGREES < orientation || orientation < TOLERANCE_DEGREES) { // 0±15

            screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            Log.v(TAG, "SCREEN_ORIENTATION_PORTRAIT " + screenOrientation);

        } else if (90 - TOLERANCE_DEGREES < orientation && orientation < 90 + TOLERANCE_DEGREES) { // 90±15

            screenOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            Log.v(TAG, "SCREEN_ORIENTATION_REVERSE_LANDSCAPE " + screenOrientation);

        } else if (180 - TOLERANCE_DEGREES < orientation && orientation < 180 + TOLERANCE_DEGREES) { // 180±15

            screenOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            Log.v(TAG, "SCREEN_ORIENTATION_REVERSE_PORTRAIT " + screenOrientation);

        } else if (270 - TOLERANCE_DEGREES < orientation && orientation < 270 + TOLERANCE_DEGREES) { // 270±15

            screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            Log.v(TAG, "SCREEN_ORIENTATION_LANDSCAPE " + screenOrientation);

        }

        rotateScreen(screenOrientation);
    }

    /**
     * 强制旋转屏幕
     *
     * @param screenOrientation
     */
    private void rotateScreen(int screenOrientation) {
        if (screenOrientation != activity.getRequestedOrientation()) {
            activity.setRequestedOrientation(screenOrientation);
        }
    }

}
