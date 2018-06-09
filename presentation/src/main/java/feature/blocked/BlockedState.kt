/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.blocked

import io.realm.RealmResults
import model.Conversation

data class BlockedState(
        val siaEnabled: Boolean = false,
        val data: RealmResults<Conversation>? = null
)