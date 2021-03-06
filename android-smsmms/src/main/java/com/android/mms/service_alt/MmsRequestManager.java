/*
 * Copyright (c) 2018. Arash Hatami
 */

package com.android.mms.service_alt;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;
import com.android.mms.transaction.NotificationTransaction;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu_alt.PduParser;
import com.google.android.mms.pdu_alt.PduPersister;
import com.google.android.mms.pdu_alt.RetrieveConf;
import com.google.android.mms.util_alt.SqliteWrapper;

public class MmsRequestManager implements MmsRequest.RequestManager {

    private static final String TAG = "MmsRequestManager";

    private Context context;
    private byte[] pduData;

    public MmsRequestManager(Context context) {
        this(context, null);
    }

    public MmsRequestManager(Context context, byte[] pduData) {
        this.context = context;
        this.pduData = pduData;
    }

    @Override
    public void addSimRequest(MmsRequest request) {
        // Do nothing, this will not be invoked ever.
    }

    @Override
    public boolean getAutoPersistingPref() {
        return NotificationTransaction.allowAutoDownload(context);
    }

    @Override
    public byte[] readPduFromContentUri(Uri contentUri, int maxSize) {
        return pduData;
    }

    @Override
    public boolean writePduToContentUri(Uri contentUri, byte[] response) {
        if (response == null || response.length < 1) {
            Log.e(TAG, "empty response");
            return false;
        }
        try {
            // Parse M-Retrieve.conf
            RetrieveConf retrieveConf = (RetrieveConf) new PduParser(response).parse();
            if (null == retrieveConf) {
                throw new MmsException("Invalid M-Retrieve.conf PDU.");
            }

            Uri msgUri;

            // Store M-Retrieve.conf into Inbox
            PduPersister persister = PduPersister.getPduPersister(context);
            msgUri = persister.persist(retrieveConf, Telephony.Mms.Inbox.CONTENT_URI, true,
                    true, null);

            // Use local time instead of PDU time
            ContentValues values = new ContentValues(2);
            values.put(Telephony.Mms.DATE, System.currentTimeMillis() / 1000L);
            values.put(Telephony.Mms.MESSAGE_SIZE, response.length);
            SqliteWrapper.update(context, context.getContentResolver(),
                    msgUri, values, null, null);

            // Send ACK to the Proxy-Relay to indicate we have fetched the
            // MM successfully.
            // Don't mark the transaction as failed if we failed to send it.
            // sendAcknowledgeInd(retrieveConf);
        } catch (Throwable t) {
            com.klinker.android.logger.Log.e(TAG, "error", t);
        }

        return false;
    }
}
