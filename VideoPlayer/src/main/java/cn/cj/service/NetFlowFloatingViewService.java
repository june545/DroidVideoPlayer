package cn.cj.service;

import android.content.Intent;
import android.os.IBinder;

/**
 * Created by June on 2016/8/14.
 */
public class NetFlowFloatingViewService extends FloatingViewService {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void showWhat() {
        NetFlowView netFlowView = new NetFlowView();
    }
}
