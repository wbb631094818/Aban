/*
 * Copyright (c) 2018. Arash Hatami
 */
package migration

import io.realm.DynamicRealm
import io.realm.RealmMigration


class AbanRealmMigration : RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        var version = oldVersion

        if (version == 0L) {
            realm.schema.get("MmsPart")
                    ?.removeField("image")

            version++
        }

        if (version < newVersion) {
            throw IllegalStateException("Migration missing from v$oldVersion to v$newVersion")
        }
    }

}