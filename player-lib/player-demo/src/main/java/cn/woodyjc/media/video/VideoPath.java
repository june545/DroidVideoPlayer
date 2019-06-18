package cn.woodyjc.media.video;

import android.net.Uri;

import java.io.File;

/**
 * @author June
 * @date 2019-06-19
 */
public class VideoPath {
    public static String local = Uri.fromFile(new File("/sdcard/Download/iceage.3gp")).toString();

    // String path = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";
    // String path = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8";
    public static String path = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";


    public static String http = "http://vfx.mtime.cn/Video/2019/06/15/mp4/190615103827358781.mp4";

    public static String https = "https://res.exexm.com/cw_145225549855002";

    public static String rtmp = "rtmp://live.hkstv.hk.lxdns.com/live/hks1";

    public static String rtsp = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";

}
