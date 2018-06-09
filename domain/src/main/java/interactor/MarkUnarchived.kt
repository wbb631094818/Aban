/*
 * Copyright (c) 2018. Arash Hatami
 */
package interactor

import io.reactivex.Flowable
import repository.MessageRepository
import javax.inject.Inject

class MarkUnarchived @Inject constructor(private val messageRepo: MessageRepository) : Interactor<List<Long>>() {

    override fun buildObservable(params: List<Long>): Flowable<*> {
        return Flowable.just(params.toLongArray())
                .doOnNext { threadIds -> messageRepo.markUnarchived(*threadIds) }
    }

}