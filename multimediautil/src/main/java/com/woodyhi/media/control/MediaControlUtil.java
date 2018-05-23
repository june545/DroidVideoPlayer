package com.woodyhi.media.control;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;

/**
 * Created by June on 2018/5/23.
 */
public class MediaControlUtil {

    public static final String SERVICECMD = "com.android.music.musicservicecommand";
    public static final String CMDNAME = "command";
    public static final String CMDTOGGLEPAUSE = "togglepause";
    public static final String CMDSTOP = "stop";
    public static final String CMDPAUSE = "pause";
    public static final String CMDPREVIOUS = "previous";
    public static final String CMDNEXT = "next";
    public static final String TOGGLEPAUSE_ACTION = "com.android.music.musicservicecommand.togglepause";
    public static final String PAUSE_ACTION = "com.android.music.musicservicecommand.pause";
    public static final String PREVIOUS_ACTION = "com.android.music.musicservicecommand.previous";
    public static final String NEXT_ACTION = "com.android.music.musicservicecommand.next";


    public void pauseMusic(Context context) {
        Intent freshIntent = new Intent();
        freshIntent.setAction("com.android.music.musicservicecommand.pause");
        freshIntent.putExtra("command", "pause");
        context.sendBroadcast(freshIntent);
    }


    public void sendKeyPressBroadcast(Context context, int keycode, String packageName) {
        Intent intent;
        switch (keycode) {
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                intent = new Intent("com.android.music.musicservicecommand.previous");
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                intent = new Intent("com.android.music.musicservicecommand.togglepause");
                break;
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                intent = new Intent("com.android.music.musicservicecommand.pause");
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                intent = new Intent("com.android.music.musicservicecommand.play");
                break;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                intent = new Intent("com.android.music.musicservicecommand.next");
                break;
            default:
                return;
        }

        if (packageName != null)
            intent.setPackage(packageName);

        context.sendOrderedBroadcast(intent, null);
    }

    public void sendKeyPressBroadcastString(Context context, int keycode, String packageName) {
        Intent intent = new Intent("com.android.music.musicservicecommand");
        switch (keycode) {
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                intent.putExtra("command", "previous");
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                intent.putExtra("command", "togglepause");
                break;
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                intent.putExtra("command", "pause");
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                intent.putExtra("command", "play");
                break;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                intent.putExtra("command", "next");
                break;
            case KeyEvent.KEYCODE_MEDIA_STOP:
                intent.putExtra("command", "stop");
                break;
            default:
                return;
        }

        if (packageName != null)
            intent.setPackage(packageName);

        context.sendOrderedBroadcast(intent, null);
    }


    // TODO test
    public boolean isMusicActive(Context context) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (manager.isMusicActive()) {
            return true;
        }
        return false;
    }
}
