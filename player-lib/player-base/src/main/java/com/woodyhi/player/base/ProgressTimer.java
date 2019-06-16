package com.woodyhi.player.base;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * @author june
 */
public class ProgressTimer extends Handler {

    private static final int UPDATE_PLAYBACK_PROGRESS = 0;

    private WeakReference<Object> weakReference;
    private int interval;
    private Callback callback;
    private long time;
    private boolean update;

    /**
     * @param obj      临时持有对象观察生命周期
     * @param interval 时间间隔 单位ms
     */
    public ProgressTimer(Object obj, int interval) {
        this.weakReference = new WeakReference<>(obj);
        this.interval = interval;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void start() {
        if (update) return;
        time = System.currentTimeMillis();
        update = true;
        removeMessages(UPDATE_PLAYBACK_PROGRESS);
        sendEmptyMessage(UPDATE_PLAYBACK_PROGRESS);
    }

    public void stop() {
        update = false;
        removeMessages(UPDATE_PLAYBACK_PROGRESS);
    }

    @Override
    public void handleMessage(Message msg) {
        if (weakReference.get() == null) return;

        switch (msg.what) {
            case UPDATE_PLAYBACK_PROGRESS:
                if (callback != null) {
                    callback.onTick(System.currentTimeMillis() - time);
                }

                sendEmptyMessageDelayed(UPDATE_PLAYBACK_PROGRESS, interval);
                break;
        }
    }

    public interface Callback {
        /**
         * elapsed time
         */
        void onTick(long ms);
    }

}
