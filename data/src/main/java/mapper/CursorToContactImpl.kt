/*
 * Copyright (c) 2018. Arash Hatami
 */
package mapper

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract.CommonDataKinds.Phone.*
import manager.PermissionManager
import model.Contact
import model.PhoneNumber
import javax.inject.Inject

class CursorToContactImpl @Inject constructor(
        private val context: Context,
        private val permissionManager: PermissionManager
) : CursorToContact {

    companion object {
        val URI = CONTENT_URI
        val PROJECTION = arrayOf(LOOKUP_KEY, NUMBER, TYPE, DISPLAY_NAME)

        const val COLUMN_LOOKUP_KEY = 0
        const val COLUMN_NUMBER = 1
        const val COLUMN_TYPE = 2
        const val COLUMN_DISPLAY_NAME = 3
    }

    override fun map(from: Cursor) = Contact().apply {
        lookupKey = from.getString(COLUMN_LOOKUP_KEY)
        name = from.getString(COLUMN_DISPLAY_NAME) ?: ""
        numbers.add(PhoneNumber().apply {
            address = from.getString(COLUMN_NUMBER) ?: ""
            type = context.getString(getTypeLabelResource(from.getInt(COLUMN_TYPE)))
        })
        lastUpdate = System.currentTimeMillis()
    }

    override fun getContactsCursor(): Cursor? {
        return when (permissionManager.hasContacts()) {
            true -> context.contentResolver.query(URI, PROJECTION, null, null, null)
            false -> null
        }
    }

}