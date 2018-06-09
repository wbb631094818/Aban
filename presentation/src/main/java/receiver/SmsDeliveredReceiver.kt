/*
 * Copyright (c) 2018. Arash Hatami
 */
package receiver

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import injection.appComponent
import interactor.MarkDelivered
import interactor.MarkDeliveryFailed
import javax.inject.Inject

class SmsDeliveredReceiver : BroadcastReceiver() {

    @Inject lateinit var markDelivered: MarkDelivered
    @Inject lateinit var markDeliveryFailed: MarkDeliveryFailed

    init {
        appComponent.inject(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getLongExtra("id", 0L)

        when (resultCode) {
        // TODO notify about delivery
            Activity.RESULT_OK -> {
                val pendingResult = goAsync()
                markDelivered.execute(id) { pendingResult.finish() }
            }

        // TODO notify about delivery failure
            Activity.RESULT_CANCELED -> {
                val pendingResult = goAsync()
                markDeliveryFailed.execute(MarkDeliveryFailed.Params(id, resultCode)) { pendingResult.finish() }
            }
        }
    }

}