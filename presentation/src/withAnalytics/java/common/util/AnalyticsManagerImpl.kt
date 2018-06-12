package common.util

import android.content.Context
import com.amplitude.api.Amplitude
import com.amplitude.api.AmplitudeClient
import com.amplitude.api.Identify
import com.mixpanel.android.mpmetrics.MixpanelAPI
import ir.hatamiarash.aban.BuildConfig
import manager.AnalyticsManager
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsManagerImpl @Inject constructor(context: Context) : AnalyticsManager {

    private val amplitude: AmplitudeClient = Amplitude.getInstance().initialize(context, BuildConfig.AMPLITUDE_API_KEY)
    private val mixpanel: MixpanelAPI = MixpanelAPI.getInstance(context, BuildConfig.MIXPANEL_API_KEY)

    init {
        amplitude.trackSessionEvents(true)
    }

    override fun track(event: String, vararg properties: Pair<String, String>) {
        val propertiesJson = JSONObject(properties
                .associateBy { pair -> pair.first }
                .mapValues { pair -> pair.value.second })

        amplitude.logEvent(event, propertiesJson)

        synchronized(mixpanel, {
            mixpanel.track(event, propertiesJson)
        })
    }

    override fun setUserProperty(key: String, value: Any) {

        // Set the value in Mixpanel
        val properties = JSONObject()
        properties.put(key, value)
        mixpanel.registerSuperProperties(properties)

        // Set the value in Amplitude
        when (value) {
            is Boolean -> Identify().set(key, value)
            is BooleanArray -> Identify().set(key, value)
            is Double -> Identify().set(key, value)
            is DoubleArray -> Identify().set(key, value)
            is Float -> Identify().set(key, value)
            is FloatArray -> Identify().set(key, value)
            is Int -> Identify().set(key, value)
            is IntArray -> Identify().set(key, value)
            is Long -> Identify().set(key, value)
            is LongArray -> Identify().set(key, value)
            is String -> Identify().set(key, value)
            is JSONArray -> Identify().set(key, value)
            is JSONObject -> Identify().set(key, value)
            else -> Timber.e("Value of type ${value::class.java} not supported")
        }
    }

}
