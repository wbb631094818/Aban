/*
 * Copyright (c) 2018. Arash Hatami
 */
package interactor

import io.reactivex.Flowable
import repository.SyncRepository
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class ContactSync @Inject constructor(private val syncManager: SyncRepository) : Interactor<Unit>() {

    override fun buildObservable(params: Unit): Flowable<Long> {
        return Flowable.just(System.currentTimeMillis())
                .doOnNext { syncManager.syncContacts() }
                .map { startTime -> System.currentTimeMillis() - startTime }
                .map { elapsed -> TimeUnit.MILLISECONDS.toSeconds(elapsed) }
                .doOnNext { seconds -> Timber.v("Completed sync in $seconds seconds") }
    }

}