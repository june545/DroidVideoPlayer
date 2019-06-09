package com.woodyhi.player.base;

import android.util.Log;

/**
 * @author June.C
 * @date 2019-06-07
 */
public class LogUtil {
    public static boolean debug = BuildConfig.DEBUG;

    public static void v(String tag, String msg) {
        if (debug)
            Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (debug)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (debug)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (debug)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (debug)
            Log.e(tag, msg);
    }
}
