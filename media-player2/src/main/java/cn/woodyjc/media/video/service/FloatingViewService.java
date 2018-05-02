package cn.woodyjc.media.video.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Created by June on 2016/8/14.
 */
public class FloatingViewService extends Service {
    private static final String TAG = FloatingViewService.class.getSimpleName();
    public static final String PARAM_KEY = "param";
    public static final int PARAM_VALUE = 1;

    /**
     * a notification to keep the service alive when task manager try to kill it
     */
    private static Notification notification;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int ret = super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            Log.d(TAG, "---onStartCommand-" + intent.getIntExtra("PARAM", -1) + "-flags-" + flags + "---return-" + ret);
            if (intent.getIntExtra(PARAM_KEY, -1) == PARAM_VALUE) {
                showFloatingView(this);
                return START_STICKY;
            }
        }
        return ret;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "---onCreate");
        if (notification == null) {
            // build a notification to show nothing just to keep this service alive
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext());
            notification = builder.build();

            // 常駐起動
//            startForeground(33333, notification);
        }

//		noti(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "---onCreate");
    }

    private void showFloatingView(Context context) {
        boolean canShow = false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            canShow = true;
        } else {
            canShow = Settings.canDrawOverlays(context);
        }

        if (canShow) {
            // 加载悬浮窗并显示
//            PlayerView playerView = new PlayerView(context);
//            playerView.floating();
            showWhat();

        } else {
            // 申请悬浮窗权限
            final Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    // 显示悬浮窗操作
    protected void showWhat(){

    }

}
