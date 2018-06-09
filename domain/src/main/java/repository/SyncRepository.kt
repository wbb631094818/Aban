/*
 * Copyright (c) 2018. Arash Hatami
 */
package repository

import android.net.Uri
import io.reactivex.Observable
import model.Message

interface SyncRepository {

    sealed class SyncProgress {
        class Idle : SyncProgress()
        class Running(progress: Float) : SyncProgress()
    }

    val syncProgress: Observable<SyncProgress>

    fun syncMessages()

    fun syncMessage(uri: Uri): Message?

    fun syncContacts()

}