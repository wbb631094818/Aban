/*
 * Copyright (c) 2018. Arash Hatami
 */

package com.android.mms.util;

import com.android.mms.logs.LogTag;
import com.klinker.android.logger.Log;

import java.util.HashMap;

public class SendingProgressTokenManager {
    private static final String TAG = LogTag.TAG;
    private static final boolean DEBUG = false;
    private static final boolean LOCAL_LOGV = false;
    private static final HashMap<Object, Long> TOKEN_POOL;

    public static final long NO_TOKEN = -1L;

    static {
        TOKEN_POOL = new HashMap<Object, Long>();
    }

    synchronized public static long get(Object key) {
        Long token = TOKEN_POOL.get(key);
        if (LOCAL_LOGV) {
            Log.v(TAG, "TokenManager.get(" + key + ") -> " + token);
        }
        return token != null ? token : NO_TOKEN;
    }

    synchronized public static void put(Object key, long token) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "TokenManager.put(" + key + ", " + token + ")");
        }
        TOKEN_POOL.put(key, token);
    }

    synchronized public static void remove(Object key) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "TokenManager.remove(" + key + ")");
        }
        TOKEN_POOL.remove(key);
    }
}
