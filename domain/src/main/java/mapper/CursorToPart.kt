/*
 * Copyright (c) 2018. Arash Hatami
 */
package mapper

import android.database.Cursor
import model.MmsPart

interface CursorToPart : Mapper<Cursor, MmsPart> {

    fun getPartsCursor(messageId: Long): Cursor?

}