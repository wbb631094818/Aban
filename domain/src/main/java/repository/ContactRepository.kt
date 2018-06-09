/*
 * Copyright (c) 2018. Arash Hatami
 */
package repository

import android.net.Uri
import io.reactivex.Flowable
import io.reactivex.Single
import model.Contact

interface ContactRepository {

    fun findContactUri(address: String): Single<Uri>

    fun getUnmanagedContacts(): Flowable<List<Contact>>

}