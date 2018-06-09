/*
 * Copyright (c) 2018. Arash Hatami
 */

package com.android.mms.transaction;

import com.google.android.mms.MmsException;

public interface MessageSender {
    public static final String RECIPIENTS_SEPARATOR = ";";

    /**
     * Send the message through MMS or SMS protocol.
     * @param token The token to identify the sending progress.
     *
     * @return True if the message was sent through MMS or false if it was
     *         sent through SMS.
     * @throws com.google.android.mms.MmsException Error occurred while sending the message.
     */
    boolean sendMessage(long token) throws MmsException;
}
