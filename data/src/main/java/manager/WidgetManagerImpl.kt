/*
 * Copyright (c) 2018. Arash Hatami
 */
package manager

import android.content.Context
import android.content.Intent
import com.klinker.android.send_message.BroadcastUtils
import me.leolin.shortcutbadger.ShortcutBadger
import javax.inject.Inject

class WidgetManagerImpl @Inject constructor(private val context: Context) : WidgetManager {

    override fun updateUnreadCount(count: Int) {
        ShortcutBadger.applyCount(context, count)

        BroadcastUtils.sendExplicitBroadcast(context, Intent(), WidgetManager.ACTION_NOTIFY_DATASET_CHANGED)
    }

}