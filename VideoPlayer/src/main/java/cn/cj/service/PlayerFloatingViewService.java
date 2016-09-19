package cn.cj.service;

import android.content.Intent;
import android.os.IBinder;

import cn.cj.media.FloatingViewPlayer;

/**
 * Created by June on 2016/8/14.
 */
public class PlayerFloatingViewService extends FloatingViewService {

    private String mediePath;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            mediePath = intent.getStringExtra("MEDIAPATH");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void showWhat() {
        if(mediePath == null){
            return;
        }
        FloatingViewPlayer floatingViewPlayer = new FloatingViewPlayer(this);
        floatingViewPlayer.show(mediePath);
    }
}
