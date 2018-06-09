/*
 * Copyright (c) 2018. Arash Hatami
 */
package interactor

import io.reactivex.Flowable
import repository.MessageRepository
import javax.inject.Inject

class DeleteConversations @Inject constructor(
        private val messageRepo: MessageRepository,
        private val updateBadge: UpdateBadge
) : Interactor<List<Long>>() {

    override fun buildObservable(params: List<Long>): Flowable<*> {
        return Flowable.just(params.toLongArray())
                .doOnNext { threadIds -> messageRepo.deleteConversations(*threadIds) }
                .flatMap { updateBadge.buildObservable(Unit) } // Update the badge
    }

}