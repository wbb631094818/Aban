/*
 * Copyright (c) 2018. Arash Hatami
 */
package interactor

import io.reactivex.Flowable
import manager.NotificationManager
import repository.MessageRepository
import javax.inject.Inject

class MarkRead @Inject constructor(
        private val messageRepo: MessageRepository,
        private val notificationManager: NotificationManager,
        private val updateBadge: UpdateBadge
) : Interactor<Long>() {

    override fun buildObservable(params: Long): Flowable<*> {
        return Flowable.just(Unit)
                .doOnNext { messageRepo.markRead(params) }
                .doOnNext { messageRepo.updateConversations(params) } // Update the conversation
                .doOnNext { notificationManager.update(params) }
                .flatMap { updateBadge.buildObservable(Unit) } // Update the badge
    }

}