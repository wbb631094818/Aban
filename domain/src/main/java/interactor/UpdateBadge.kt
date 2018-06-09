/*
 * Copyright (c) 2018. Arash Hatami
 */
package interactor

import io.reactivex.Flowable
import manager.WidgetManager
import repository.MessageRepository
import javax.inject.Inject

class UpdateBadge @Inject constructor(
        private val messageRepo: MessageRepository,
        private val widgetManager: WidgetManager
) : Interactor<Unit>() {

    override fun buildObservable(params: Unit): Flowable<*> {
        return Flowable.just(params)
                .map { messageRepo.getUnreadCount() }
                .map { count -> count.toInt() }
                .doOnNext { count -> widgetManager.updateUnreadCount(count) }
    }

}