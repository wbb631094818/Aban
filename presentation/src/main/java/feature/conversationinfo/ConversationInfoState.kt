/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.conversationinfo

import io.realm.RealmList
import io.realm.RealmResults
import model.MmsPart
import model.Recipient

data class ConversationInfoState(
        val recipients: RealmList<Recipient>? = null,
        val threadId: Long = 0,
        val archived: Boolean = false,
        val blocked: Boolean = false,
        val media: RealmResults<MmsPart>? = null,
        val hasError: Boolean = false
)