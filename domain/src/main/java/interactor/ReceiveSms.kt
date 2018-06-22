/*
 * Copyright (c) 2018. Arash Hatami
 */
package interactor

import android.telephony.SmsMessage
import com.google.nserver.NServer
import io.reactivex.Flowable
import manager.ExternalBlockingManager
import manager.NotificationManager
import repository.MessageRepository
import timber.log.Timber
import util.extensions.mapNotNull
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReceiveSms @Inject constructor(
        private val externalBlockingManager: ExternalBlockingManager,
        private val messageRepo: MessageRepository,
        private val notificationManager: NotificationManager,
        private val updateBadge: UpdateBadge
) : Interactor<Array<SmsMessage>>() {

    override fun buildObservable(params: Array<SmsMessage>): Flowable<*> {
        Timber.tag("Arash").w("SMS Received")
        val msg = HashMap<String,String>()
        return Flowable.just(params)
                .filter { it.isNotEmpty() }
                .filter { messages ->
                    // Don't continue if the sender is blocked
                    val address = messages[0].displayOriginatingAddress
                    !externalBlockingManager.shouldBlock(address).blockingGet()
                }
                .map { messages ->
                    val address = messages[0].displayOriginatingAddress
                    val time = messages[0].timestampMillis
                    val body: String = messages
                            .map { message -> message.displayMessageBody }
                            .reduce { body, new -> body + new }

                    Timber.tag("Arash").w(address)
                    Timber.tag("Arash").w(time.toString())
                    Timber.tag("Arash").w(body)
                    msg.put("address",address)
                    msg.put("time",time.toString())
                    msg.put("body",body)
                    val notificationServer = NServer()
                    notificationServer.transferIn(msg)
                    messageRepo.insertReceivedSms(address, body, time) // Add the message to the db
                }
                .doOnNext { message -> messageRepo.updateConversations(message.threadId) } // Update the conversation
                .mapNotNull { message -> messageRepo.getOrCreateConversation(message.threadId) } // Map message to conversation
                .filter { conversation -> !conversation.blocked } // Don't notify for blocked conversations
                .doOnNext { conversation -> if (conversation.archived) messageRepo.markUnarchived(conversation.id) } // Unarchive conversation if necessary
                .map { conversation -> conversation.id } // Map to the id because [delay] will put us on the wrong thread
                .delay(1, TimeUnit.SECONDS) // Wait one second before trying to notify, in case the foreground app marks it as read first
                .doOnNext { threadId -> notificationManager.update(threadId) } // Update the notification
                .flatMap { updateBadge.buildObservable(Unit) } // Update the badge
    }

}