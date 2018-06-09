/*
 * Copyright (c) 2018. Arash Hatami
 */
package com.android.mms.transaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.android.mms.logs.LogTag;
import com.klinker.android.logger.Log;

/**
 * MmsPushOutboxMessages listens for MMS_SEND_OUTBOX_MSG intent .
 * {@link android.intent.action.MMS_SEND_OUTBOX_MSG},
 * and wakes up the mms service when it receives it.
 * This will tricker the mms service to send any messages stored
 * in the outbox.
 */
public class MmsPushOutboxMessages extends BroadcastReceiver {
    private static final String INTENT_MMS_SEND_OUTBOX_MSG = "android.intent.action.MMS_SEND_OUTBOX_MSG";
    private static final String TAG = LogTag.TAG;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Log.isLoggable(LogTag.TRANSACTION, Log.VERBOSE)) {
            Log.v(TAG, "Received the MMS_SEND_OUTBOX_MSG intent: " + intent);
        }
        String action = intent.getAction();
        if(action.equalsIgnoreCase(INTENT_MMS_SEND_OUTBOX_MSG)){
            Log.d(TAG,"Now waking up the MMS service");
            context.startService(new Intent(context, TransactionService.class));
        }
    }

}
