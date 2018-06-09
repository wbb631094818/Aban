/*
 * Copyright (c) 2018. Arash Hatami
 */
package mapper

import android.database.Cursor
import model.Recipient

interface CursorToRecipient : Mapper<Cursor, Recipient> {

    fun getRecipientCursor(): Cursor?

    fun getRecipientCursor(id: Long): Cursor?

}