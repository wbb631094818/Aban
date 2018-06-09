/*
 * Copyright (c) 2018. Arash Hatami
 */
package model

import androidx.core.net.toUri
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey

open class MmsPart : RealmObject() {

    @PrimaryKey var id: Long = 0
    var type: String = ""
    var text: String? = null

    @LinkingObjects("parts")
    val messages: RealmResults<Message>? = null

    fun getUri() = "content://mms/part/$id".toUri()

}