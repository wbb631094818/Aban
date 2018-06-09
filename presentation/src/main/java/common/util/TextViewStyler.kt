/*
 * Copyright (c) 2018. Arash Hatami
 */
package common.util

import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import ir.hatamiarash.aban.R
import common.util.extensions.getColorCompat
import common.util.extensions.resolveThemeColor
import common.widget.AbanEditText
import common.widget.AbanTextView
import util.Preferences
import javax.inject.Inject

class TextViewStyler @Inject constructor(
        private val prefs: Preferences,
        private val colors: Colors,
        private val fontProvider: FontProvider
) {

    companion object {
        const val COLOR_PRIMARY = 0
        const val COLOR_SECONDARY = 1
        const val COLOR_TERTIARY = 2
        const val COLOR_PRIMARY_ON_THEME = 3
        const val COLOR_SECONDARY_ON_THEME = 4
        const val COLOR_TERTIARY_ON_THEME = 5
        const val COLOR_THEME = 6

        const val SIZE_PRIMARY = 0
        const val SIZE_SECONDARY = 1
        const val SIZE_TERTIARY = 2
        const val SIZE_TOOLBAR = 3
    }

    fun applyAttributes(textView: TextView, attrs: AttributeSet?) {
        textView.run {
            when (this) {
                is AbanTextView -> context.obtainStyledAttributes(attrs, R.styleable.AbanTextView)?.run {
                    val colorAttr = getInt(R.styleable.AbanTextView_textColor, -1)
                    val textSizeAttr = getInt(R.styleable.AbanTextView_textSize, -1)
                    applyAttributes(textView, colorAttr, textSizeAttr)
                    recycle()
                }

                is AbanEditText -> context.obtainStyledAttributes(attrs, R.styleable.AbanEditText)?.run {
                    val colorAttr = getInt(R.styleable.AbanEditText_textColor, -1)
                    val textSizeAttr = getInt(R.styleable.AbanEditText_textSize, -1)
                    applyAttributes(textView, colorAttr, textSizeAttr)
                    recycle()
                }
            }

            if (!prefs.systemFont.get()) {
                fontProvider.getLato { lato ->
                    setTypeface(lato, typeface?.style ?: Typeface.NORMAL)
                }
            }
        }
    }

    private fun applyAttributes(textView: TextView, colorAttr: Int, textSizeAttr: Int) {
        textView.run {
            if (isInEditMode) {
                setTextColor(context.getColorCompat(when (colorAttr) {
                    COLOR_PRIMARY -> R.color.textPrimary
                    COLOR_SECONDARY -> R.color.textSecondary
                    COLOR_TERTIARY -> R.color.textTertiary
                    COLOR_PRIMARY_ON_THEME -> R.color.textPrimaryDark
                    COLOR_SECONDARY_ON_THEME -> R.color.textSecondaryDark
                    COLOR_TERTIARY_ON_THEME -> R.color.textTertiaryDark
                    COLOR_THEME -> R.color.tools_theme
                    else -> R.color.textPrimary
                }))

                textSize = when (textSizeAttr) {
                    SIZE_PRIMARY -> 16f
                    SIZE_SECONDARY -> 14f
                    SIZE_TERTIARY -> 12f
                    SIZE_TOOLBAR -> 20f
                    else -> textSize
                }
            } else {
                setTextColor(when (colorAttr) {
                    COLOR_PRIMARY -> context.resolveThemeColor(android.R.attr.textColorPrimary)
                    COLOR_SECONDARY -> context.resolveThemeColor(android.R.attr.textColorSecondary)
                    COLOR_TERTIARY -> context.resolveThemeColor(android.R.attr.textColorTertiary)
                    COLOR_PRIMARY_ON_THEME -> colors.theme().textPrimary
                    COLOR_SECONDARY_ON_THEME -> colors.theme().textSecondary
                    COLOR_TERTIARY_ON_THEME -> colors.theme().textTertiary
                    COLOR_THEME -> colors.theme().theme
                    else -> currentTextColor
                })

                setTextSize(textView, textSizeAttr)
            }
        }
    }

    /**
     * @see SIZE_PRIMARY
     * @see SIZE_SECONDARY
     * @see SIZE_TERTIARY
     * @see SIZE_TOOLBAR
     */
    fun setTextSize(textView: TextView, textSizeAttr: Int) {
        val textSizePref = prefs.textSize.get()
        when (textSizeAttr) {
            SIZE_PRIMARY -> textView.textSize = when (textSizePref) {
                Preferences.TEXT_SIZE_SMALL -> 14f
                Preferences.TEXT_SIZE_NORMAL -> 16f
                Preferences.TEXT_SIZE_LARGE -> 18f
                Preferences.TEXT_SIZE_LARGER -> 20f
                else -> 16f
            }

            SIZE_SECONDARY -> textView.textSize = when (textSizePref) {
                Preferences.TEXT_SIZE_SMALL -> 12f
                Preferences.TEXT_SIZE_NORMAL -> 14f
                Preferences.TEXT_SIZE_LARGE -> 16f
                Preferences.TEXT_SIZE_LARGER -> 18f
                else -> 14f
            }

            SIZE_TERTIARY -> textView.textSize = when (textSizePref) {
                Preferences.TEXT_SIZE_SMALL -> 10f
                Preferences.TEXT_SIZE_NORMAL -> 12f
                Preferences.TEXT_SIZE_LARGE -> 14f
                Preferences.TEXT_SIZE_LARGER -> 16f
                else -> 12f
            }

            SIZE_TOOLBAR -> textView.textSize = when (textSizePref) {
                Preferences.TEXT_SIZE_SMALL -> 18f
                Preferences.TEXT_SIZE_NORMAL -> 20f
                Preferences.TEXT_SIZE_LARGE -> 22f
                Preferences.TEXT_SIZE_LARGER -> 26f
                else -> 20f
            }
        }
    }

}