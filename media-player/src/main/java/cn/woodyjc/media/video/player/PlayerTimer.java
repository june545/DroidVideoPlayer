package cn.woodyjc.media.video.player;

import android.os.Handler;
import android.os.Message;

/**
 * Created by June on 2018/5/15.
 */
public class PlayerTimer extends Handler {

    private static final int HANDLER_UPDATE_PLAYBACK_PROGRESS = 0;

    private Handler handler;
    private Callback callback;

    private long time;

    private boolean update;

    PlayerTimer() {
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void start() {
        time = System.currentTimeMillis();
        update = true;
        sendEmptyMessage(HANDLER_UPDATE_PLAYBACK_PROGRESS);
    }

    public void stop() {
        update = false;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case HANDLER_UPDATE_PLAYBACK_PROGRESS:
                if (update) {
                    sendEmptyMessageDelayed(
                            HANDLER_UPDATE_PLAYBACK_PROGRESS,
                            1000 - (System.currentTimeMillis() - time) % 1000);

                    if (callback != null) {
                        callback.onTick(System.currentTimeMillis() - time);
                    }
                }
                break;
        }
    }

    interface Callback {
        void onTick(long ms);
    }

}
