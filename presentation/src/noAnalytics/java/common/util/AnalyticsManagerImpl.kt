/*
 * Copyright (c) 2018. Arash Hatami
 */
package common.util

import manager.AnalyticsManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsManagerImpl @Inject constructor() : AnalyticsManager {

    override fun track(event: String, vararg properties: Pair<String, String>) {
        // Do nothing
    }

    override fun setUserProperty(key: String, value: Any) {
        // Do nothing
    }

}
