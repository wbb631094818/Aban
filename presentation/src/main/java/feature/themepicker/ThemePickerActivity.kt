/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.themepicker

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.view.clicks
import ir.hatamiarash.aban.R
import common.base.AbanThemedActivity
import common.util.extensions.setBackgroundTint
import common.util.extensions.setVisible
import dagger.android.AndroidInjection
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.theme_picker_activity.*
import kotlinx.android.synthetic.main.theme_picker_hsv.*
import javax.inject.Inject

class ThemePickerActivity : AbanThemedActivity(), ThemePickerView {

    @Inject lateinit var themeAdapter: ThemeAdapter
    @Inject lateinit var themePagerAdapter: ThemePagerAdapter
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override val themeSelectedIntent by lazy { themeAdapter.colorSelected }
    override val hsvThemeSelectedIntent by lazy { picker.selectedColor }
    override val hsvThemeClearedIntent by lazy { clear.clicks() }
    override val hsvThemeAppliedIntent by lazy { apply.clicks() }
    override val viewAbansmsPlusIntent: Subject<Unit> = PublishSubject.create()

    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory)[ThemePickerViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.theme_picker_activity)
        setTitle(R.string.title_theme)
        showBackButton(true)
        viewModel.bindView(this)

        pager.offscreenPageLimit = 1
        pager.adapter = themePagerAdapter
        tabs.pager = pager

        themeAdapter.data = colors.materialColors

        materialColors.layoutManager = LinearLayoutManager(this)
        materialColors.adapter = themeAdapter
    }

    override fun showAbansmsPlusSnackbar() {
        Snackbar.make(contentView, R.string.toast_qksms_plus, Snackbar.LENGTH_LONG).run {
            setAction(R.string.button_more, { viewAbansmsPlusIntent.onNext(Unit) })
            show()
        }
    }

    override fun render(state: ThemePickerState) {
        tabs.setThreadId(state.threadId)

        hex.setText(Integer.toHexString(state.newColor).takeLast(6))

        applyGroup.setVisible(state.applyThemeVisible)
        apply.setBackgroundTint(state.newColor)
        apply.setTextColor(state.newTextColor)
    }

    override fun setCurrentTheme(color: Int) {
        picker.setColor(color)
        themeAdapter.selectedColor = color
    }
}