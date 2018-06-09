/*
 * Copyright (c) 2018. Arash Hatami
 */
package manager

interface AnalyticsManager {

    fun track(event: String, vararg properties: Pair<String, String>)

    fun setUserProperty(key: String, value: Any)

}