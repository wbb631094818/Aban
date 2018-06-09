/*
 * Copyright (c) 2018. Arash Hatami
 */
package manager

import io.reactivex.Single

interface ExternalBlockingManager {

    /**
     * Return a Single<Boolean> which emits whether or not the given [address] should be blocked
     */
    fun shouldBlock(address: String): Single<Boolean>

}