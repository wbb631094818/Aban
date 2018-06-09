/*
 * Copyright (c) 2018. Arash Hatami
 */
package receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import injection.appComponent
import interactor.MarkSeen
import javax.inject.Inject

class MarkSeenReceiver : BroadcastReceiver() {

    @Inject lateinit var markSeen: MarkSeen

    override fun onReceive(context: Context, intent: Intent) {
        appComponent.inject(this)

        val threadId = intent.getLongExtra("threadId", 0)
        markSeen.execute(threadId)
    }

}