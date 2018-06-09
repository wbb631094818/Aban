/*
 * Copyright (c) 2018. Arash Hatami
 */
package interactor

import io.reactivex.Flowable
import repository.ImageRepository
import repository.MessageRepository
import javax.inject.Inject

class SaveImage @Inject constructor(
        private val imageRepository: ImageRepository,
        private val messageRepo: MessageRepository
) : Interactor<Long>() {

    override fun buildObservable(params: Long): Flowable<*> {
        return Flowable.just(params)
                .map { partId -> messageRepo.getPart(partId) }
                .map { part -> part.getUri() }
                .doOnNext { uri -> imageRepository.saveImage(uri) }
    }

}