/*
 * Copyright (c) 2018. Arash Hatami
 */

package com.android.internal.telephony;

/**
 * {@hide}
 */
@SuppressWarnings("serial")
public class EncodeException extends Exception {
    public EncodeException() {
        super();
    }

    public EncodeException(String s) {
        super(s);
    }

    public EncodeException(char c) {
        super("Unencodable char: '" + c + "'");
    }
}

