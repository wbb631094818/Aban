/*
 * Copyright (c) 2018. Arash Hatami
 */
package service

import android.app.IntentService
import android.content.Intent
import android.net.Uri
import android.telephony.TelephonyManager
import injection.appComponent
import interactor.SendMessage
import repository.MessageRepository
import javax.inject.Inject

class HeadlessSmsSendService : IntentService("HeadlessSmsSendService") {

    @Inject lateinit var messageRepo: MessageRepository
    @Inject lateinit var sendMessage: SendMessage

    override fun onHandleIntent(intent: Intent?) {
        if (intent?.action != TelephonyManager.ACTION_RESPOND_VIA_MESSAGE) return

        appComponent.inject(this)
        intent.extras?.getString(Intent.EXTRA_TEXT)?.takeIf { it.isNotBlank() }?.let { body ->
            val intentUri = intent.data
            val recipients = getRecipients(intentUri).split(";")
            val threadId = messageRepo.getOrCreateConversation(recipients).blockingGet()?.id ?: 0L
            sendMessage.execute(SendMessage.Params(threadId, recipients, body))
        }
    }

    private fun getRecipients(uri: Uri): String {
        val base = uri.schemeSpecificPart
        val position = base.indexOf('?')
        return if (position == -1) base else base.substring(0, position)
    }

}