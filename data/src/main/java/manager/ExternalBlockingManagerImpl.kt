/*
 * Copyright (c) 2018. Arash Hatami
 */
package manager

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import androidx.core.os.bundleOf
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import timber.log.Timber
import util.Preferences
import util.tryOrNull
import javax.inject.Inject

class ExternalBlockingManagerImpl @Inject constructor(
        private val context: Context,
        private val prefs: Preferences
) : ExternalBlockingManager {

    companion object {
        const val RATING_UNKNOWN = 0
        const val RATING_POSITIVE = 1
        const val RATING_NEGATIVE = 2
        const val RATING_NEUTRAL = 3

        const val GET_NUMBER_RATING = 1
    }

    /**
     * Return a Single<Boolean> which emits whether or not the given [address] should be blocked
     */
    override fun shouldBlock(address: String): Single<Boolean> {
        return Binder(context, prefs, address).shouldBlock()
    }

    private class Binder(
            private val context: Context,
            private val prefs: Preferences,
            private val address: String
    ) : ServiceConnection {

        private val subject: SingleSubject<Boolean> = SingleSubject.create()
        private var serviceMessenger: Messenger? = null
        private var isBound: Boolean = false

        fun shouldBlock(): Single<Boolean> {

            var intent: Intent? = null

            // If either version of Should I Answer? is installed and SIA is enabled, build the
            // intent to request a rating
            if (prefs.sia.get()) {
                intent = tryOrNull {
                    context.packageManager.getApplicationInfo("org.mistergroup.shouldianswerpersonal", 0).enabled
                    Intent("org.mistergroup.shouldianswerpersonal.PublicService").setPackage("org.mistergroup.shouldianswerpersonal")
                } ?: tryOrNull {
                    context.packageManager.getApplicationInfo("org.mistergroup.muzutozvednout", 0).enabled
                    Intent("org.mistergroup.muzutozvednout.PublicService").setPackage("org.mistergroup.muzutozvednout")
                }
            }

            // If the intent isn't null, bind the service and wait for a result. Otherwise, don't block
            if (intent != null) {
                context.bindService(intent, this, Context.BIND_AUTO_CREATE)
            } else {
                subject.onSuccess(false)
            }

            return subject
        }

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            serviceMessenger = Messenger(service)
            isBound = true

            val msg = Message()
            msg.what = GET_NUMBER_RATING
            msg.data = bundleOf(Pair("number", address))
            msg.replyTo = Messenger(IncomingHandler { rating ->
                subject.onSuccess(rating == RATING_NEGATIVE)
                Timber.v("Should block: ${rating == RATING_NEGATIVE}")

                // We're done, so unbind the service
                if (isBound && serviceMessenger != null) {
                    context.unbindService(this)
                }
            })

            serviceMessenger?.send(msg)
        }

        override fun onServiceDisconnected(className: ComponentName) {
            serviceMessenger = null
            isBound = false
        }
    }

    private class IncomingHandler(private val callback: (rating: Int) -> Unit) : Handler() {

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                GET_NUMBER_RATING -> callback(msg.data.getInt("rating"))
                else -> super.handleMessage(msg)
            }
        }
    }

}
