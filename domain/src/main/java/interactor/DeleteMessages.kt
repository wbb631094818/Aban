/*
 * Copyright (c) 2018. Arash Hatami
 */
package interactor

import io.reactivex.Flowable
import repository.MessageRepository
import javax.inject.Inject

class DeleteMessages @Inject constructor(
        private val messageRepo: MessageRepository,
        private val updateBadge: UpdateBadge
) : Interactor<DeleteMessages.Params>() {

    data class Params(val messageIds: List<Long>, val threadId: Long? = null)

    override fun buildObservable(params: Params): Flowable<*> {
        return Flowable.just(params.messageIds.toLongArray())
                .doOnNext { messageIds -> messageRepo.deleteMessages(*messageIds) } // Delete the messages
                .doOnNext { params.threadId?.let { messageRepo.updateConversations(it) } } // Update the conversation
                .flatMap { updateBadge.buildObservable(Unit) } // Update the badge
    }

}