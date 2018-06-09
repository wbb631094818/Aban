/*
 * Copyright (c) 2018. Arash Hatami
 */
package manager

import io.realm.Realm
import model.Message
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeyManagerImpl @Inject constructor() : KeyManager {

    private var maxValue: Long = 0L

    init {
        val realm = Realm.getDefaultInstance()
        maxValue = realm.where(Message::class.java).max("id")?.toLong() ?: 0L
        realm.close()
    }

    /**
     * Should be called when a new sync is being started
     */
    override fun reset() {
        maxValue = 0L
    }

    /**
     * Returns a valid ID that can be used to store a new message
     */
    override fun newId(): Long {
        maxValue++
        return maxValue
    }

}