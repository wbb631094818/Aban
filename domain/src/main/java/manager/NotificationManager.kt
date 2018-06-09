/*
 * Copyright (c) 2018. Arash Hatami
 */
package manager

interface NotificationManager {

    fun update(threadId: Long)

    fun notifyFailed(threadId: Long)

    fun createNotificationChannel(threadId: Long)

    fun buildNotificationChannelId(threadId: Long): String

}