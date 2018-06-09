/*
 * Copyright (c) 2018. Arash Hatami
 */
package util.extensions

import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmQuery
import io.realm.RealmResults
import timber.log.Timber

fun RealmModel.insertOrUpdate() {
    val realm = Realm.getDefaultInstance()
    realm.executeTransaction { realm.insertOrUpdate(this) }
    realm.close()
}

fun <T : RealmModel> Collection<T>.insertOrUpdate() {
    val realm = Realm.getDefaultInstance()
    realm.executeTransaction { realm.insertOrUpdate(this) }
    realm.close()
}

fun <T : RealmObject> RealmObject.asObservable(): Observable<T> {
    return asFlowable<T>().toObservable()
}

fun <T : RealmObject> RealmResults<T>.asObservable(): Observable<RealmResults<T>> {
    return asFlowable().toObservable()
}

fun <T : RealmObject> RealmQuery<T>.anyOf(fieldName: String, values: LongArray): RealmQuery<T> {
    values.forEach { Timber.v("vararg: $it") }

    return when (values.isEmpty()) {
        true -> equalTo(fieldName, -1L)
        false -> `in`(fieldName, values.toTypedArray())
    }
}