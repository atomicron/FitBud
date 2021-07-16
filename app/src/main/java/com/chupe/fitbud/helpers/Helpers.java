package com.chupe.fitbud.helpers;

import android.app.Activity;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Helpers {
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private static String kekwJava(String str) {
         StringBuilder lol = new StringBuilder(str);
         lol.setCharAt(0, '0');
         str = lol.toString();
         return str;
    }

    public static String toTime(int duration) {
        int s = duration;
        int m = s/60;
        int h = m/60;

        String dur = String.valueOf(s);
        String seconds = dur.substring(Math.max(dur.length() - 2, 0));
        if (seconds.charAt(0) > '5') seconds = kekwJava(seconds);
        if (seconds.length() < 2) seconds = "0" + seconds;

        String min = String.valueOf(m);
        String minutes = min.substring(Math.max(min.length() - 2, 0));
        if (minutes.charAt(0) > '5') minutes = kekwJava(seconds);
        if (minutes.length() < 2)  minutes = "0" + minutes;

        String hou = String.valueOf(h);
        String hours = hou.substring(Math.max(hou.length() - 2, 0));

        return hours + ":" + minutes + ":" + seconds;
    }

    public static void playNotification(Context context) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
