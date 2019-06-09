package com.woodyhi.player.base;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

/**
 * @author june
 */
public class ProgressTimer extends Handler {

    private static final int UPDATE_PLAYBACK_PROGRESS = 0;

    private WeakReference<Fragment> weakReference;
    private Callback callback;
    private long time;
    private boolean update;

    public ProgressTimer(Fragment fragment) {
        this.weakReference = new WeakReference<>(fragment);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void start() {
        time = System.currentTimeMillis();
        update = true;
        sendEmptyMessage(UPDATE_PLAYBACK_PROGRESS);
    }

    public void stop() {
        update = false;
    }

    @Override
    public void handleMessage(Message msg) {
        if(weakReference.get() == null) return;

        switch (msg.what) {
            case UPDATE_PLAYBACK_PROGRESS:
                if (update) {
                    sendEmptyMessageDelayed(
                            UPDATE_PLAYBACK_PROGRESS,
                            1000 - (System.currentTimeMillis() - time) % 1000);

                    if (callback != null) {
                        callback.onTick(System.currentTimeMillis() - time);
                    }
                }
                break;
        }
    }

    public interface Callback {
        void onTick(long ms);
    }

}
