/*
 * Copyright (c) 2018. Arash Hatami
 */
package interactor

import io.reactivex.Flowable
import manager.NotificationManager
import repository.MessageRepository
import javax.inject.Inject

class MarkFailed @Inject constructor(
        private val messageRepo: MessageRepository,
        private val notificationManager: NotificationManager
) : Interactor<MarkFailed.Params>() {

    data class Params(val id: Long, val resultCode: Int)

    override fun buildObservable(params: Params): Flowable<Unit> {
        return Flowable.just(Unit)
                .doOnNext { messageRepo.markFailed(params.id, params.resultCode) }
                .doOnNext { notificationManager.notifyFailed(params.id) }
    }

}