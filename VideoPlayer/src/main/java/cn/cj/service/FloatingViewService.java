package cn.cj.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import cn.cj.media.video.player.R;
import cn.cj.videoplayer.MainActivity;

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

    private void noti(Context context) {
        Notification mNotification = new Notification(R.mipmap.ic_launcher, "aaaaaa", System.currentTimeMillis());
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //该通知能被状态栏的清除按钮给清除掉
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
        // 设置提醒声
        mNotification.defaults |= Notification.DEFAULT_SOUND;
        Intent notificationIntent = new Intent(context, MainActivity.class); /* 建立PendingIntent */
        PendingIntent sender = PendingIntent.getBroadcast(context, 1111, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //要实现自定义布局，首先自己定义一个 布局文件notification.xml,如放一个ImageView 和 TextView，用这个布局文件生成 //RemoteViews的实例

        RemoteViews contentview = new RemoteViews(context.getPackageName(), R.layout.notification_view);
        mNotification.contentView = contentview;
        mNotification.contentIntent = sender;
        mNotificationManager.notify(1111, mNotification);
    }

}
