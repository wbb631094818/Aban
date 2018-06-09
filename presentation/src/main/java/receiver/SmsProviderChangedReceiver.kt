/*
 * Copyright (c) 2018. Arash Hatami
 */
package receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import injection.appComponent
import interactor.SyncMessage
import javax.inject.Inject

/**
 * When the SMS contentprovider is changed by a process other than us, we need to sync the Uri that
 * was changed.
 *
 * This can happen if a message is sent through something like Pushbullet or Google Assistant.
 *
 * This only works on API 24+, so to fully solve this problem we'll need a smarter way of running
 * partial syncs on older devices.
 *
 * https://developer.android.com/reference/android/provider/Telephony.Sms.Intents.html#ACTION_EXTERNAL_PROVIDER_CHANGE
 */
class SmsProviderChangedReceiver : BroadcastReceiver() {

    @Inject lateinit var syncMessage: SyncMessage

    override fun onReceive(context: Context, intent: Intent) {
        appComponent.inject(this)

        // Obtain the uri for the changed data
        // If the value is null, don't continue
        val uri = intent.data ?: return

        // Sync the message to our realm
        val pendingResult = goAsync()
        syncMessage.execute(uri) { pendingResult.finish() }
    }

}