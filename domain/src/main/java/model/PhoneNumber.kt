/*
 * Copyright (c) 2018. Arash Hatami
 */
package model

import io.realm.RealmObject

open class PhoneNumber(
        var address: String = "",
        var type: String = ""
) : RealmObject()