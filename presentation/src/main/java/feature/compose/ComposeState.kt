/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.compose

import io.realm.RealmResults
import model.Contact
import model.Conversation
import model.Message

data class ComposeState(
        val hasError: Boolean = false,
        val editingMode: Boolean = false,
        val contacts: List<Contact> = ArrayList(),
        val contactsVisible: Boolean = false,
        val selectedConversation: Long = 0,
        val selectedContacts: List<Contact> = ArrayList(),
        val conversationtitle: String = "",
        val query: String = "",
        val searchSelectionId: Long = -1,
        val searchSelectionPosition: Int = 0,
        val searchResults: Int = 0,
        val messages: Pair<Conversation, RealmResults<Message>>? = null,
        val selectedMessages: Int = 0,
        val attachments: List<Attachment> = ArrayList(),
        val attaching: Boolean = false,
        val remaining: String = "",
        val canSend: Boolean = false
)