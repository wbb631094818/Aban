/*
 * Copyright (c) 2018. Arash Hatami
 */

package com.android.internal.net;

import android.app.PendingIntent;
import android.net.NetworkInfo;
import android.os.Parcel;
import android.os.Parcelable;
import com.klinker.android.logger.Log;

/**
 * A simple container used to carry information of the ongoing legacy VPN.
 * Internal use only.
 *
 * @hide
 */
public class LegacyVpnInfo implements Parcelable {
    private static final String TAG = "LegacyVpnInfo";

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_INITIALIZING = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_TIMEOUT = 4;
    public static final int STATE_FAILED = 5;

    public String key;
    public int state = -1;
    public PendingIntent intent;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(key);
        out.writeInt(state);
        out.writeParcelable(intent, flags);
    }

    public static final Parcelable.Creator<LegacyVpnInfo> CREATOR =
            new Parcelable.Creator<LegacyVpnInfo>() {
                @Override
                public LegacyVpnInfo createFromParcel(Parcel in) {
                    LegacyVpnInfo info = new LegacyVpnInfo();
                    info.key = in.readString();
                    info.state = in.readInt();
                    info.intent = in.readParcelable(null);
                    return info;
                }

                @Override
                public LegacyVpnInfo[] newArray(int size) {
                    return new LegacyVpnInfo[size];
                }
            };

    /**
     * Return best matching {@link LegacyVpnInfo} state based on given
     * {@link NetworkInfo}.
     */
    public static int stateFromNetworkInfo(NetworkInfo info) {
        switch (info.getDetailedState()) {
            case CONNECTING:
                return STATE_CONNECTING;
            case CONNECTED:
                return STATE_CONNECTED;
            case DISCONNECTED:
                return STATE_DISCONNECTED;
            case FAILED:
                return STATE_FAILED;
            default:
                Log.w(TAG, "Unhandled state " + info.getDetailedState()
                        + " ; treating as disconnected");
                return STATE_DISCONNECTED;
        }
    }
}
