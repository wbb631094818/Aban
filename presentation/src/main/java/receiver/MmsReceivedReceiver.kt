/*
 * Copyright (c) 2018. Arash Hatami
 */
package receiver

import android.net.Uri
import com.klinker.android.send_message.MmsReceivedReceiver
import injection.appComponent
import interactor.ReceiveMms
import javax.inject.Inject

class MmsReceivedReceiver : MmsReceivedReceiver() {

    @Inject lateinit var receiveMms: ReceiveMms

    override fun onMessageReceived(messageUri: Uri?) {
        appComponent.inject(this)

        messageUri?.let { uri ->
            val pendingResult = goAsync()
            receiveMms.execute(uri) { pendingResult.finish() }
        }
    }

}