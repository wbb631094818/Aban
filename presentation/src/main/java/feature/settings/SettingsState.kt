/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.settings

import util.Preferences

data class SettingsState(
        val syncing: Boolean = false,
        val theme: Int = 0,
        val nightModeSummary: String = "",
        val nightModeId: Int = Preferences.NIGHT_MODE_OFF,
        val nightStart: String = "",
        val nightEnd: String = "",
        val black: Boolean = false,
        val autoEmojiEnabled: Boolean = true,
        val notificationsEnabled: Boolean = true,
        val sendDelaySummary: String = "",
        val sendDelayId: Int = 0,
        val deliveryEnabled: Boolean = false,
        val textSizeSummary: String = "",
        val textSizeId: Int = Preferences.TEXT_SIZE_NORMAL,
        val systemFontEnabled: Boolean = false,
        val splitSmsEnabled: Boolean = false,
        val stripUnicodeEnabled: Boolean = false,
        val maxMmsSizeSummary: String = "100KB",
        val maxMmsSizeId: Int = 100
)