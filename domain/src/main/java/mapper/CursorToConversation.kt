/*
 * Copyright (c) 2018. Arash Hatami
 */
package mapper

import android.database.Cursor
import model.Conversation

interface CursorToConversation : Mapper<Cursor, Conversation> {

    fun getConversationsCursor(lastSync: Long = 0): Cursor?

    fun getConversationCursor(threadId: Long): Cursor?

}