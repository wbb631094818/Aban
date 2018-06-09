/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.notificationprefs

import android.net.Uri
import common.base.AbanView
import common.widget.PreferenceView
import io.reactivex.Observable
import io.reactivex.subjects.Subject

interface NotificationPrefsView : AbanView<NotificationPrefsState> {

    val preferenceClickIntent: Subject<PreferenceView>
    val previewModeSelectedIntent: Subject<Int>
    val ringtoneSelectedIntent: Observable<String>

    fun showPreviewModeDialog()
    fun showRingtonePicker(default: Uri?)
}
