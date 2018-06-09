/*
 * Copyright (c) 2018. Arash Hatami
 */
package interactor

import io.reactivex.Flowable
import repository.MessageRepository
import javax.inject.Inject

class CancelDelayedMessage @Inject constructor(private val messageRepo: MessageRepository) : Interactor<Long>() {

    override fun buildObservable(params: Long): Flowable<*> {
        return Flowable.just(params)
                .doOnNext { id -> messageRepo.cancelDelayedSms(id) }
                .doOnNext { id -> messageRepo.deleteMessages(id) }
    }

}