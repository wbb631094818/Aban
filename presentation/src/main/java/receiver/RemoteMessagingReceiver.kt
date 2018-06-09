/*
 * Copyright (c) 2018. Arash Hatami
 */
package receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.RemoteInput
import injection.appComponent
import interactor.MarkRead
import interactor.SendMessage
import javax.inject.Inject

class RemoteMessagingReceiver : BroadcastReceiver() {

    @Inject lateinit var sendMessage: SendMessage
    @Inject lateinit var markRead: MarkRead

    override fun onReceive(context: Context, intent: Intent) {
        appComponent.inject(this)

        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        val bundle = intent.extras
        if (remoteInput != null && bundle != null) {
            val address = bundle.getString("address")
            val threadId = bundle.getLong("threadId")
            val body = remoteInput.getCharSequence("body").toString()
            markRead.execute(threadId)
            sendMessage.execute(SendMessage.Params(threadId, listOf(address), body))
        }
    }
}
