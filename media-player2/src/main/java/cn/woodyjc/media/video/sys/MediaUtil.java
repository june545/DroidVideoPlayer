package cn.woodyjc.media.video.sys;

import android.content.Context;
import android.content.Intent;

/**
 * @author June Cheng
 * @date 2015年9月25日 下午11:59:05
 */
public class MediaUtil {

	/**
     * 关闭其他播放器
	 * @param context
	 */
	public static void stop(Context context) {
		Intent intent = new Intent();
		intent.setAction("com.android.music.musicservicecommand.stop");
		intent.putExtra("command", "stop");
		context.sendBroadcast(intent);
	}
}
