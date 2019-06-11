package com.woodyhi.player.base;

import android.view.SurfaceHolder;

/**
 * @auth June
 * @date 2019/06/10
 */
public abstract class AbsPlayerManager implements BaseController {

    public abstract void surfaceCreated(SurfaceHolder holder);
    public abstract void surfaceDestroyed(SurfaceHolder holder);
}
