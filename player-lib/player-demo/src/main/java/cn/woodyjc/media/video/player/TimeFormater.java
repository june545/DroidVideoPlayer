package cn.woodyjc.media.video.player;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by june on 2018/2/8.
 */

public class TimeFormater {

    /**
     * convert millis to looking like [00:00] or [00:00:00]
     * @param msec
     * @return
     */
    public static String formatMillisTime(long msec) {
        if (msec < 1000) {
            return "00:00";
        }

        long total_s = msec / 1000;
        int s = (int) (total_s % 60);// 秒

        long total_m = total_s / 60;
        int m = (int) (total_m % 60);

        long total_h = total_m / 60;
        int h = (int) total_h;

        if (h > 0) {
            return String.format("%02d:%02d:%02d", h, m, s);
        } else {
            return String.format("%02d:%02d", m, s);
        }
    }

    /**
     * Convert time to a string
     *
     * @param msec
     *            e.g.time/length from file
     * @return formated string "[hh]h[mm]min" / "[mm]min[s]s"
     */
    public static String millisToText(long msec) {
        return millisToString(msec, true);
    }

    public static String millisToString(long msec, boolean text) {
        boolean negative = msec < 0;
        msec = Math.abs(msec);

        msec /= 1000;
        int sec = (int) (msec % 60);
        msec /= 60;
        int min = (int) (msec % 60);
        msec /= 60;
        int hours = (int) msec;

        String time;
        DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        format.applyPattern("00");
        if (text) {
            if (msec > 0)
                time = (negative ? "-" : "") + hours + "h" + format.format(min) + "min";
            else if (min > 0)
                time = (negative ? "-" : "") + min + "min";
            else
                time = (negative ? "-" : "") + sec + "s";
        } else {
            if (msec > 0)
                time = (negative ? "-" : "") + hours + ":" + format.format(min) + ":" + format.format(sec);
            else
                time = (negative ? "-" : "") + min + ":" + format.format(sec);
        }
        return time;
    }

}
