/*
 * Copyright (c) 2018. Arash Hatami
 */
package interactor

import io.reactivex.Flowable
import model.Message
import repository.MessageRepository
import javax.inject.Inject

class RetrySending @Inject constructor(private val messageRepo: MessageRepository) : Interactor<Message>() {

    override fun buildObservable(params: Message): Flowable<Message> {

        // We don't want to touch the supplied message on another thread in case it's a live realm
        // object, so copy the required fields into a new object that is safe to pass around threads
        val message = Message().apply {
            id = params.id
            type = params.type
            address = params.address
            body = params.body
        }

        return Flowable.just(message)
                .filter { message.isSms() } // TODO support resending failed MMS
                .doOnNext { messageRepo.markSending(message.id) }
                .doOnNext { messageRepo.sendSms(message) }
    }

}