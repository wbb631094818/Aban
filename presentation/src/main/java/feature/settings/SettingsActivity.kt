/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.settings

import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.format.DateFormat
import com.franmontiel.localechanger.LocaleChanger
import com.jakewharton.rxbinding2.view.clicks
import ir.hatamiarash.aban.BuildConfig
import ir.hatamiarash.aban.R
import com.uber.autodispose.AutoDispose.autoDisposable
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.kotlin.autoDisposable
import common.AbanDialog
import common.base.AbanThemedActivity
import common.util.extensions.setBackgroundTint
import common.util.extensions.setVisible
import common.widget.PreferenceView
import dagger.android.AndroidInjection
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.settings_activity.*
import kotlinx.android.synthetic.main.settings_switch_widget.view.*
import kotlinx.android.synthetic.main.settings_theme_widget.*
import util.Preferences
import java.util.*
import javax.inject.Inject

class SettingsActivity : AbanThemedActivity(), SettingsView {

    @Inject lateinit var nightModeDialog: AbanDialog
    @Inject lateinit var textSizeDialog: AbanDialog
    @Inject lateinit var sendDelayDialog: AbanDialog
    @Inject lateinit var mmsSizeDialog: AbanDialog
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override val preferenceClickIntent: Subject<PreferenceView> = PublishSubject.create()
    override val viewAbansmsPlusIntent: Subject<Unit> = PublishSubject.create()
    override val nightModeSelectedIntent by lazy { nightModeDialog.adapter.menuItemClicks }
    override val startTimeSelectedIntent: Subject<Pair<Int, Int>> = PublishSubject.create()
    override val endTimeSelectedIntent: Subject<Pair<Int, Int>> = PublishSubject.create()
    override val textSizeSelectedIntent by lazy { textSizeDialog.adapter.menuItemClicks }
    override val sendDelayChangedIntent by lazy { sendDelayDialog.adapter.menuItemClicks }
    override val mmsSizeSelectedIntent: Subject<Int> by lazy { mmsSizeDialog.adapter.menuItemClicks }

    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory)[SettingsViewModel::class.java] }

    // TODO remove this
    private val progressDialog by lazy {
        ProgressDialog(this).apply {
            isIndeterminate = true
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        var base = newBase
        base = LocaleChanger.configureBaseContext(base)
        super.attachBaseContext(base)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        LocaleChanger.setLocale(Locale("fa", "IR"));
        setContentView(R.layout.settings_activity)
        setTitle(R.string.title_settings)
        showBackButton(true)
        viewModel.bindView(this)

        nightModeDialog.adapter.setData(R.array.night_modes)
        textSizeDialog.adapter.setData(R.array.text_sizes)
        sendDelayDialog.adapter.setData(R.array.delayed_sending_labels)
        mmsSizeDialog.adapter.setData(R.array.mms_sizes, R.array.mms_sizes_ids)

        about.summary = getString(R.string.settings_version, BuildConfig.VERSION_NAME)

        // Listen to clicks for all of the preferences
        (0 until preferences.childCount)
                .map { index -> preferences.getChildAt(index) }
                .filter { view -> view is PreferenceView }
                .forEach { preference ->
                    preference.clicks().subscribe {
                        preferenceClickIntent.onNext(preference as PreferenceView)
                    }
                }

        theme
                .skip(1)
                .autoDisposable(scope())
                .subscribe { recreate() }
    }

    override fun render(state: SettingsState) {
        if (progressDialog.isShowing && !state.syncing) progressDialog.dismiss()
        else if (!progressDialog.isShowing && state.syncing) progressDialog.show()

        themePreview.setBackgroundTint(state.theme)
        night.summary = state.nightModeSummary
        nightModeDialog.adapter.selectedItem = state.nightModeId
        nightStart.setVisible(state.nightModeId == Preferences.NIGHT_MODE_AUTO)
        nightStart.summary = state.nightStart
        nightEnd.setVisible(state.nightModeId == Preferences.NIGHT_MODE_AUTO)
        nightEnd.summary = state.nightEnd

        black.setVisible(state.nightModeId != Preferences.NIGHT_MODE_OFF)
        black.checkbox.isChecked = state.black

        autoEmoji.checkbox.isChecked = state.autoEmojiEnabled

        delayed.summary = state.sendDelaySummary
        sendDelayDialog.adapter.selectedItem = state.sendDelayId

        delivery.checkbox.isChecked = state.deliveryEnabled

        textSize.summary = state.textSizeSummary
        textSizeDialog.adapter.selectedItem = state.textSizeId
        systemFont.checkbox.isChecked = state.systemFontEnabled

        unicode.checkbox.isChecked = state.stripUnicodeEnabled

        mmsSize.summary = state.maxMmsSizeSummary
        mmsSizeDialog.adapter.selectedItem = state.maxMmsSizeId
    }

//    override fun showAbansmsPlusSnackbar() {
//        Snackbar.make(contentView, R.string.toast_qksms_plus, Snackbar.LENGTH_LONG).run {
//            setAction(R.string.button_more, { viewAbansmsPlusIntent.onNext(Unit) })
//            show()
//        }
//    }

    // TODO change this to a PopupWindow
    override fun showNightModeDialog() = nightModeDialog.show(this)

    override fun showStartTimePicker(hour: Int, minute: Int) {
        TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, newHour, newMinute ->
            startTimeSelectedIntent.onNext(Pair(newHour, newMinute))
        }, hour, minute, DateFormat.is24HourFormat(this)).show()
    }

    override fun showEndTimePicker(hour: Int, minute: Int) {
        TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, newHour, newMinute ->
            endTimeSelectedIntent.onNext(Pair(newHour, newMinute))
        }, hour, minute, DateFormat.is24HourFormat(this)).show()
    }

    override fun showTextSizePicker() = textSizeDialog.show(this)

    override fun showDelayDurationDialog() = sendDelayDialog.show(this)

    override fun showMmsSizePicker() = mmsSizeDialog.show(this)

}