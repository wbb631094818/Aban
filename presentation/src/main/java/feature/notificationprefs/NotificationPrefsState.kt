/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.notificationprefs

import android.os.Build
import util.Preferences

data class NotificationPrefsState(
        val conversationTitle: String = "",
        val notificationsEnabled: Boolean = true,
        val previewSummary: String = "",
        val previewId: Int = Preferences.NOTIFICATION_PREVIEWS_ALL,
        val vibrationEnabled: Boolean = true,
        val ringtoneName: String = "",
        val qkReplyEnabled: Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.N,
        val qkReplyTapDismiss: Boolean = true)