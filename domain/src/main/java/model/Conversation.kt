/*
 * Copyright (c) 2018. Arash Hatami
 */
package model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class Conversation : RealmObject() {

    @PrimaryKey var id: Long = 0
    @Index var archived: Boolean = false
    @Index var blocked: Boolean = false
    var recipients: RealmList<Recipient> = RealmList()
    var count: Int = 0
    var date: Long = 0
    var snippet: String = ""
    var read: Boolean = true
    var me: Boolean = false
    var draft: String = ""

    fun getTitle() = recipients.joinToString { recipient -> recipient.getDisplayName() }
}
