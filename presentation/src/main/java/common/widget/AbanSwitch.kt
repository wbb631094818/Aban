/*
 * Copyright (c) 2018. Arash Hatami
 */
package common.widget

import android.content.Context
import android.content.res.ColorStateList
import android.support.v7.widget.SwitchCompat
import android.util.AttributeSet
import ir.hatamiarash.aban.R
import common.util.Colors
import common.util.extensions.getColorCompat
import common.util.extensions.withAlpha
import injection.appComponent
import util.Preferences
import javax.inject.Inject

class AbanSwitch @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : SwitchCompat(context, attrs) {

    @Inject lateinit var colors: Colors
    @Inject lateinit var prefs: Preferences

    init {
        if (!isInEditMode) {
            appComponent.inject(this)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (!isInEditMode) {
            val states = arrayOf(
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_checked),
                    intArrayOf())

            val themeColor = colors.theme().theme

            val switchThumbEnabled: Int = prefs.night.get()
                    .let { night -> if (night) R.color.switchThumbEnabledDark else R.color.switchThumbEnabledLight }
                    .let { res -> context.getColorCompat(res) }

            val switchThumbDisabled: Int = prefs.night.get()
                    .let { night -> if (night) R.color.switchThumbDisabledDark else R.color.switchThumbDisabledLight }
                    .let { res -> context.getColorCompat(res) }

            val switchTrackEnabled: Int = prefs.night.get()
                    .let { night -> if (night) R.color.switchTrackEnabledDark else R.color.switchTrackEnabledLight }
                    .let { res -> context.getColorCompat(res) }

            val switchTrackDisabled: Int = prefs.night.get()
                    .let { night -> if (night) R.color.switchTrackDisabledDark else R.color.switchTrackDisabledLight }
                    .let { res -> context.getColorCompat(res) }

            thumbTintList = ColorStateList(states, intArrayOf(switchThumbDisabled, themeColor, switchThumbEnabled))
            trackTintList = ColorStateList(states, intArrayOf(switchTrackDisabled, themeColor.withAlpha(0x4D), switchTrackEnabled))
        }
    }
}