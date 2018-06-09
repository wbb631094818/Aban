/*
 * Copyright (c) 2018. Arash Hatami
 */
package mapper

import android.database.Cursor
import model.Contact

interface CursorToContact : Mapper<Cursor, Contact> {

    fun getContactsCursor(): Cursor?

}