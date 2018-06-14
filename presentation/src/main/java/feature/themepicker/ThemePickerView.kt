/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.themepicker

import common.base.AbanView
import io.reactivex.Observable
import io.reactivex.subjects.Subject

interface ThemePickerView : AbanView<ThemePickerState> {

    val themeSelectedIntent: Observable<Int>
    val hsvThemeSelectedIntent: Observable<Int>
    val hsvThemeClearedIntent: Observable<*>
    val hsvThemeAppliedIntent: Observable<*>
    val viewAbansmsPlusIntent: Subject<Unit>

    fun setCurrentTheme(color: Int)
    //fun showAbansmsPlusSnackbar()

}