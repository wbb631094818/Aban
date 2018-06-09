/*
 * Copyright (c) 2018. Arash Hatami
 */
package manager

interface PermissionManager {

    fun isDefaultSms(): Boolean

    fun hasSmsAndContacts(): Boolean

    fun hasSms(): Boolean

    fun hasContacts(): Boolean

    fun hasStorage(): Boolean

}