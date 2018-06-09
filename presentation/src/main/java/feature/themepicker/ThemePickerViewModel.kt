/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.themepicker

import com.f2prateek.rx.preferences2.Preference
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.kotlin.autoDisposable
import common.Navigator
import common.base.AbanViewModel
import common.util.BillingManager
import common.util.Colors
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.withLatestFrom
import util.Preferences
import javax.inject.Inject
import javax.inject.Named

class ThemePickerViewModel @Inject constructor(
        @Named("threadId") threadId: Long,
        private val billingManager: BillingManager,
        private val colors: Colors,
        private val navigator: Navigator,
        private val prefs: Preferences
) : AbanViewModel<ThemePickerView, ThemePickerState>(ThemePickerState(threadId = threadId)) {

    private val theme: Preference<Int> = prefs.theme(threadId)

    override fun bindView(view: ThemePickerView) {
        super.bindView(view)

        theme.asObservable()
                .autoDisposable(view.scope())
                .subscribe { color -> view.setCurrentTheme(color) }

        // Update the theme when a material theme is clicked
        view.themeSelectedIntent
                .autoDisposable(view.scope())
                .subscribe { color -> theme.set(color) }

        // Update the color of the apply button
        view.hsvThemeSelectedIntent
                .doOnNext { color -> newState { it.copy(newColor = color) } }
                .map { color -> colors.textPrimaryOnThemeForColor(color) }
                .doOnNext { color -> newState { it.copy(newTextColor = color) } }
                .autoDisposable(view.scope())
                .subscribe()

        // Toggle the visibility of the apply group
        Observables.combineLatest(theme.asObservable(), view.hsvThemeSelectedIntent, { old, new -> old != new })
                .autoDisposable(view.scope())
                .subscribe { themeChanged -> newState { it.copy(applyThemeVisible = themeChanged) } }

        // Update the theme, when apply is clicked
        view.hsvThemeAppliedIntent
                .withLatestFrom(view.hsvThemeSelectedIntent, { _, color -> color })
                .withLatestFrom(billingManager.upgradeStatus, { color, upgraded ->
                    if (!upgraded) {
                        view.showAbansmsPlusSnackbar()
                    } else {
                        theme.set(color)
                    }
                })
                .autoDisposable(view.scope())
                .subscribe()

        // Show QKSMS+ activity
        view.viewAbansmsPlusIntent
                .autoDisposable(view.scope())
                .subscribe { navigator.showAbansmsPlusActivity() }

        // Reset the theme
        view.hsvThemeClearedIntent
                .withLatestFrom(theme.asObservable(), { _, color -> color })
                .autoDisposable(view.scope())
                .subscribe { color -> view.setCurrentTheme(color) }
    }

}