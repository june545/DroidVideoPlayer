package com.woodyhi.player.base;

import android.view.SurfaceHolder;

import java.util.Vector;

/**
 * @auth June
 * @date 2019/06/10
 */
public abstract class AbsPlayerManager implements BaseController {
    protected Vector<PlayerCallback> playerCallbacks = new Vector<>();

    public void addPlayerCallback(PlayerCallback listener) {
        this.playerCallbacks.add(listener);
    }

    public void removePlayerCallback(PlayerCallback callback) {
        playerCallbacks.remove(callback);
    }

    public abstract void surfaceCreated(SurfaceHolder holder);

    public abstract void surfaceDestroyed(SurfaceHolder holder);
}
