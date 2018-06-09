/*
 * Copyright (c) 2018. Arash Hatami
 */
package interactor

import io.reactivex.Flowable
import repository.MessageRepository
import javax.inject.Inject

class MarkAllSeen @Inject constructor(private val messageRepo: MessageRepository) : Interactor<Unit>() {

    override fun buildObservable(params: Unit): Flowable<Unit> {
        return Flowable.just(Unit)
                .doOnNext { messageRepo.markAllSeen() }
    }

}