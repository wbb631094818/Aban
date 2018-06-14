/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.settings

import common.base.AbanView
import common.widget.PreferenceView
import io.reactivex.Observable
import io.reactivex.subjects.Subject

interface SettingsView : AbanView<SettingsState> {

    val preferenceClickIntent: Subject<PreferenceView>
    val viewAbansmsPlusIntent: Subject<Unit>
    val nightModeSelectedIntent: Observable<Int>
    val startTimeSelectedIntent: Subject<Pair<Int, Int>>
    val endTimeSelectedIntent: Subject<Pair<Int, Int>>
    val textSizeSelectedIntent: Subject<Int>
    val sendDelayChangedIntent: Observable<Int>
    val mmsSizeSelectedIntent: Observable<Int>

    //fun showAbansmsPlusSnackbar()
    fun showNightModeDialog()
    fun showStartTimePicker(hour: Int, minute: Int)
    fun showEndTimePicker(hour: Int, minute: Int)
    fun showTextSizePicker()
    fun showDelayDurationDialog()
    fun showMmsSizePicker()
}
