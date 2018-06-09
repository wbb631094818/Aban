/*
 * Copyright (c) 2018. Arash Hatami
 */
package receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import injection.appComponent
import interactor.RetrySending
import repository.MessageRepository
import javax.inject.Inject

class SendSmsReceiver : BroadcastReceiver() {

    @Inject lateinit var messageRepo: MessageRepository
    @Inject lateinit var retrySending: RetrySending

    override fun onReceive(context: Context, intent: Intent) {
        appComponent.inject(this)

        val id = intent.getLongExtra("id", 0L)
        messageRepo.getMessage(id)?.let { message ->
            val result = goAsync()
            retrySending.execute(message) { result.finish() }
        }
    }

}