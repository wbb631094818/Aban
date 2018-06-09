/*
 * Copyright (c) 2018. Arash Hatami
 */
package manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.provider.Telephony
import android.support.v4.content.ContextCompat
import javax.inject.Inject

class PermissionManagerImpl @Inject constructor(private val context: Context) : PermissionManager {

    override fun isDefaultSms(): Boolean {
        return Telephony.Sms.getDefaultSmsPackage(context) == context.packageName
    }

    override fun hasSmsAndContacts(): Boolean {
        return hasSms() && hasContacts()
    }

    override fun hasSms(): Boolean = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PERMISSION_GRANTED

    override fun hasContacts(): Boolean = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PERMISSION_GRANTED

    override fun hasStorage(): Boolean = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED

}