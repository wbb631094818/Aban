/*
 * Copyright (c) 2018. Arash Hatami
 */
package receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import injection.appComponent
import common.util.NightModeManager
import javax.inject.Inject

class NightModeReceiver : BroadcastReceiver() {

    @Inject lateinit var nightModeManager: NightModeManager

    init {
        appComponent.inject(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        nightModeManager.updateCurrentTheme()
    }
}