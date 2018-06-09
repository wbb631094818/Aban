/*
 * Copyright (c) 2018. Arash Hatami
 */

package com.android.mms.logs;

import com.klinker.android.logger.Log;

public class LogTag {
    public static final String TAG = "Mms";

    public static final String TRANSACTION = TAG;
    public static final String APP = TAG;
    public static final String THREAD_CACHE = TAG;
    public static final String THUMBNAIL_CACHE = TAG;
    public static final String PDU_CACHE = TAG;
    public static final String WIDGET = TAG;
    public static final String CONTACT = TAG;

    /**
     * Log tag for enabling/disabling StrictMode violation log.
     * To enable: adb shell setprop log.tag.Mms:strictmode DEBUG
     */
    public static final String STRICT_MODE_TAG = TAG;
    public static final boolean VERBOSE = false;
    public static final boolean SEVERE_WARNING = true;                  // Leave this true
    private static final boolean SHOW_SEVERE_WARNING_DIALOG = false;    // Set to false before ship
    public static final boolean DEBUG_SEND = false;    // Set to false before ship
    public static final boolean DEBUG_DUMP = false;    // Set to false before ship
    public static final boolean ALLOW_DUMP_IN_LOGS = false;  // Set to false before ship

    private static String prettyArray(String[] array) {
        if (array.length == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        int len = array.length-1;
        for (int i = 0; i < len; i++) {
            sb.append(array[i]);
            sb.append(", ");
        }
        sb.append(array[len]);
        sb.append("]");

        return sb.toString();
    }

    private static String logFormat(String format, Object... args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String[]) {
                args[i] = prettyArray((String[])args[i]);
            }
        }
        String s = String.format(format, args);
        s = "[" + Thread.currentThread().getId() + "] " + s;
        return s;
    }

    public static void debug(String format, Object... args) {
        Log.d(TAG, logFormat(format, args));
    }

    public static void warn(String format, Object... args) {
        Log.w(TAG, logFormat(format, args));
    }

    public static void error(String format, Object... args) {
        Log.e(TAG, logFormat(format, args));
    }

}
