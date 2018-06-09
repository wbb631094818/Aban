/*
 * Copyright (c) 2018. Arash Hatami
 */
package model

import io.realm.RealmObject

open class SyncLog : RealmObject() {

    var date: Long = System.currentTimeMillis()

}