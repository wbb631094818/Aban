/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.notificationprefs

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.jakewharton.rxbinding2.view.clicks
import ir.hatamiarash.aban.R
import common.AbanDialog
import common.base.AbanThemedActivity
import common.util.extensions.setVisible
import common.widget.PreferenceView
import dagger.android.AndroidInjection
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.notification_prefs_activity.*
import kotlinx.android.synthetic.main.settings_switch_widget.view.*
import javax.inject.Inject

class NotificationPrefsActivity : AbanThemedActivity(), NotificationPrefsView {

    @Inject lateinit var previewModeDialog: AbanDialog
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override val preferenceClickIntent: Subject<PreferenceView> = PublishSubject.create()
    override val previewModeSelectedIntent by lazy { previewModeDialog.adapter.menuItemClicks }
    override val ringtoneSelectedIntent: Subject<String> = PublishSubject.create()

    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory)[NotificationPrefsViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification_prefs_activity)
        setTitle(R.string.title_notification_prefs)
        showBackButton(true)
        viewModel.bindView(this)

        val hasOreo = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

        notificationsO.setVisible(hasOreo)
        notifications.setVisible(!hasOreo)
        vibration.setVisible(!hasOreo)
        ringtone.setVisible(!hasOreo)

        previewModeDialog.setTitle(R.string.settings_notification_previews_title)
        previewModeDialog.adapter.setData(R.array.notification_preview_options)

        // Listen to clicks for all of the preferences
        (0 until preferences.childCount)
                .map { index -> preferences.getChildAt(index) }
                .filter { view -> view is PreferenceView }
                .forEach { preference ->
                    preference.clicks().subscribe {
                        preferenceClickIntent.onNext(preference as PreferenceView)
                    }
                }
    }

    override fun render(state: NotificationPrefsState) {
        if (state.conversationTitle.isNotEmpty()) {
            title = state.conversationTitle
        }

        notifications.checkbox.isChecked = state.notificationsEnabled
        notificationPreviews.summary = state.previewSummary
        previewModeDialog.adapter.selectedItem = state.previewId
        vibration.checkbox.isChecked = state.vibrationEnabled
        ringtone.summary = state.ringtoneName
        qkreply.checkbox.isChecked = state.qkReplyEnabled
        qkreplyTapDismiss.setVisible(state.qkReplyEnabled)
        qkreplyTapDismiss.checkbox.isChecked = state.qkReplyTapDismiss
    }

    override fun showPreviewModeDialog() = previewModeDialog.show(this)

    override fun showRingtonePicker(default: Uri?) {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, default)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
        startActivityForResult(intent, 123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            ringtoneSelectedIntent.onNext(uri?.toString() ?: "")
        }
    }

}