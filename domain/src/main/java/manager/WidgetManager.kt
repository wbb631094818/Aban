/*
 * Copyright (c) 2018. Arash Hatami
 */
package manager

interface WidgetManager {

    companion object {
        const val ACTION_NOTIFY_DATASET_CHANGED = "ir.hatamiarash.aban.intent.action.ACTION_NOTIFY_DATASET_CHANGED"
    }

    fun updateUnreadCount(count: Int)

}