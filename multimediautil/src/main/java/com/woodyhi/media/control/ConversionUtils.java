package com.woodyhi.media.control;

import android.content.res.Resources;

/**
 * Created by June on 2018/5/23.
 */
public class ConversionUtils {

    public static float getPixelsFromDp(int dp) {
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }

    public static float getDpFromPixels(int px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }

    public static int dp2Px(int dp) {
        return (int) (getPixelsFromDp(dp) + 0.5f);
    }

    public static int px2Dp(int px) {
        return (int) (getDpFromPixels(px) + 0.5f);
    }
}
