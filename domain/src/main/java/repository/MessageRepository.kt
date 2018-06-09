/*
 * Copyright (c) 2018. Arash Hatami
 */
package repository

import io.reactivex.Maybe
import io.realm.RealmResults
import model.Conversation
import model.Message
import model.MmsPart
import model.SearchResult

interface MessageRepository {

    fun getConversations(archived: Boolean = false): RealmResults<Conversation>

    fun getConversationsSnapshot(): List<Conversation>

    fun searchConversations(query: String): List<SearchResult>

    fun getBlockedConversations(): RealmResults<Conversation>

    fun getConversationAsync(threadId: Long): Conversation

    fun getConversation(threadId: Long): Conversation?

    fun getOrCreateConversation(threadId: Long): Conversation?

    fun getOrCreateConversation(address: String): Maybe<Conversation>

    fun getOrCreateConversation(addresses: List<String>): Maybe<Conversation>

    fun saveDraft(threadId: Long, draft: String)

    fun getMessages(threadId: Long, query: String = ""): RealmResults<Message>

    fun getMessage(id: Long): Message?

    fun getMessageForPart(id: Long): Message?

    fun getUnreadCount(): Long

    fun getPart(id: Long): MmsPart?

    fun getPartsForConversation(threadId: Long): RealmResults<MmsPart>

    /**
     * Retrieves the list of messages which should be shown in the notification
     * for a given conversation
     */
    fun getUnreadUnseenMessages(threadId: Long): RealmResults<Message>

    /**
     * Retrieves the list of messages which should be shown in the quickreply popup
     * for a given conversation
     */
    fun getUnreadMessages(threadId: Long): RealmResults<Message>

    /**
     * Updates message-related fields in the conversation, like the date and snippet
     */
    fun updateConversations(vararg threadIds: Long)

    fun markArchived(vararg threadIds: Long)

    fun markUnarchived(vararg threadIds: Long)

    fun markBlocked(vararg threadIds: Long)

    fun markUnblocked(vararg threadIds: Long)

    fun markAllSeen()

    fun markSeen(threadId: Long)

    fun markRead(threadId: Long)

    /**
     * Persists the SMS and attempts to send it
     */
    fun sendSmsAndPersist(threadId: Long, address: String, body: String)

    /**
     * Attempts to send the SMS message. This can be called if the message has already been persisted
     */
    fun sendSms(message: Message)

    /**
     * Attempts to cancel sending the message with the given id
     */
    fun cancelDelayedSms(id: Long)

    fun insertSentSms(threadId: Long, address: String, body: String, date: Long): Message

    fun insertReceivedSms(address: String, body: String, sentTime: Long): Message

    /**
     * Marks the message as sending, in case we need to retry sending it
     */
    fun markSending(id: Long)

    fun markSent(id: Long)

    fun markFailed(id: Long, resultCode: Int)

    fun markDelivered(id: Long)

    fun markDeliveryFailed(id: Long, resultCode: Int)

    fun deleteMessages(vararg messageIds: Long)

    fun deleteConversations(vararg threadIds: Long)

}