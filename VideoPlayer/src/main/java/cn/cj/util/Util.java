/**
 *
 */
package cn.cj.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author June Cheng
 * @date 2015年10月30日 上午12:13:35
 */
public class Util {

	public static int px2dip(Context context, int px) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float logicalDensity = metrics.density;
		int dip = Math.round(px / logicalDensity);
		return dip;
	}

	public static int dp2px(Context context, int dip) {
		float scale = context.getResources().getDisplayMetrics().density;
		int px = Math.round(dip * scale);
		return px;
	}

}
