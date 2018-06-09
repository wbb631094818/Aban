/*
 * Copyright (c) 2018. Arash Hatami
 */
package model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Contact(
        @PrimaryKey var lookupKey: String = "",
        var numbers: RealmList<PhoneNumber> = RealmList(),
        var name: String = "",
        var lastUpdate: Long = 0
) : RealmObject()