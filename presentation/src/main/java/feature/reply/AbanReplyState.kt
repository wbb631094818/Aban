/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.reply

import io.realm.RealmResults
import model.Conversation
import model.Message

data class AbanReplyState(
        val hasError: Boolean = false,
        val title: String = "",
        val expanded: Boolean = false,
        val data: Pair<Conversation, RealmResults<Message>>? = null,
        val remaining: String = "",
        val canSend: Boolean = false
)