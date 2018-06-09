/*
 * Copyright (c) 2018. Arash Hatami
 */
package receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony.Sms
import injection.appComponent
import interactor.ReceiveSms
import javax.inject.Inject

class SmsReceiver : BroadcastReceiver() {

    @Inject lateinit var receiveMessage: ReceiveSms

    override fun onReceive(context: Context, intent: Intent) {
        appComponent.inject(this)

        Sms.Intents.getMessagesFromIntent(intent)?.let { messages ->
            val pendingResult = goAsync()
            receiveMessage.execute(messages, { pendingResult.finish() })
        }
    }

}