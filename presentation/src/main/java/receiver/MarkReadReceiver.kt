/*
 * Copyright (c) 2018. Arash Hatami
 */
package receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import injection.appComponent
import interactor.MarkRead
import javax.inject.Inject

class MarkReadReceiver : BroadcastReceiver() {

    @Inject lateinit var markRead: MarkRead

    override fun onReceive(context: Context, intent: Intent) {
        appComponent.inject(this)

        val threadId = intent.getLongExtra("threadId", 0)
        markRead.execute(threadId)
    }

}