/*
 * Copyright (c) 2018. Arash Hatami
 */
package receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import injection.appComponent
import interactor.SyncMessage
import javax.inject.Inject

class MmsUpdatedReceiver : BroadcastReceiver() {

    companion object {
        const val URI = "uri"
    }

    @Inject lateinit var syncMessage: SyncMessage

    override fun onReceive(context: Context, intent: Intent) {
        appComponent.inject(this)
        intent.getStringExtra(URI)?.let { uriString ->
            val pendingResult = goAsync()
            syncMessage.execute(Uri.parse(uriString)) { pendingResult.finish() }
        }
    }

}