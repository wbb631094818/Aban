/*
 * Copyright (c) 2018. Arash Hatami
 */
package interactor

import android.net.Uri
import io.reactivex.Flowable
import model.Message
import repository.MessageRepository
import repository.SyncRepository
import util.extensions.mapNotNull
import javax.inject.Inject

class SyncMessage @Inject constructor(
        private val messageRepo: MessageRepository,
        private val syncManager: SyncRepository
) : Interactor<Uri>() {

    override fun buildObservable(params: Uri): Flowable<Message> {
        return Flowable.just(params)
                .mapNotNull { uri -> syncManager.syncMessage(uri) }
                .doOnNext { message -> messageRepo.updateConversations(message.threadId) }
    }

}