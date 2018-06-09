/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.themepicker

data class ThemePickerState(
        val threadId: Long = 0,
        val applyThemeVisible: Boolean = false,
        val newColor: Int = -1,
        val newTextColor: Int = -1
)