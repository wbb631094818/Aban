/*
 * Copyright (c) 2018. Arash Hatami
 */

package com.google.android.mms.util_alt;

import com.google.android.mms.pdu_alt.GenericPdu;

public final class PduCacheEntry {
    private final GenericPdu mPdu;
    private final int mMessageBox;
    private final long mThreadId;

    public PduCacheEntry(GenericPdu pdu, int msgBox, long threadId) {
        mPdu = pdu;
        mMessageBox = msgBox;
        mThreadId = threadId;
    }

    public GenericPdu getPdu() {
        return mPdu;
    }

    public int getMessageBox() {
        return mMessageBox;
    }

    public long getThreadId() {
        return mThreadId;
    }
}
