package cn.woodyjc.media.video.player;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 *
 */
public class DisplayUtil {

    public static int px2dip(Context context, int px) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float density = dm.density;
        int dip = Math.round(px / density);
        return dip;
    }

    public static int dip2px(Context context, int dip) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float density = dm.density;
        int px = Math.round(dip * density);
        return px;
    }

}
