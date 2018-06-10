/*
 * Copyright (c) 2018. Arash Hatami
 */

package common

/*
 * Copyright (C) 2017 Moez Bhatti <moez.bhatti@gmail.com>
 *
 * This file is part of QKSMS.
 *
 * QKSMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * QKSMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with QKSMS.  If not, see <http://www.gnu.org/licenses/>.
 */
//import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter.setLocale
import android.app.Activity
import android.app.Application
import android.support.text.emoji.EmojiCompat
import android.support.text.emoji.FontRequestEmojiCompatConfig
import android.support.v4.provider.FontRequest
import com.bugsnag.android.Bugsnag
import com.bugsnag.android.Configuration
import com.franmontiel.localechanger.LocaleChanger
import common.util.NightModeManager
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import injection.AppComponentManager
import injection.appComponent
import io.realm.Realm
import io.realm.RealmConfiguration
import ir.hatamiarash.aban.BuildConfig
import ir.hatamiarash.aban.R
import manager.AnalyticsManager
import migration.AbanRealmMigration
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class AbanApplication : Application(), HasActivityInjector {

    /**
     * Inject this so that it is forced to initialize
     */
    @Suppress("unused")
    @Inject
    lateinit var analyticsManager: AnalyticsManager

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var nightModeManager: NightModeManager

    private val packages = arrayOf("common", "feature", "injection", "filter", "interactor", "manager", "mapper",
            "migration", "model", "receiver", "repository", "service", "util")

    override fun onCreate() {
        super.onCreate()


        Bugsnag.init(this, Configuration(BuildConfig.BUGSNAG_API_KEY).apply {
            appVersion = BuildConfig.VERSION_NAME
            projectPackages = packages
        })

        //RxJava2Debug.enableRxJava2AssemblyTracking(packages)

        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
                .compactOnLaunch()
                .migration(AbanRealmMigration())
                .schemaVersion(1)
                .build())

        AppComponentManager.init(this)
        appComponent.inject(this)

        nightModeManager.updateCurrentTheme()

        val fontRequest = FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs)

        EmojiCompat.init(FontRequestEmojiCompatConfig(this, fontRequest))

        Timber.plant(Timber.DebugTree())

        LocaleChanger.initialize(getApplicationContext(), Arrays.asList(
                Locale("en", "US"),
                Locale("fa", "IR")
        ));
    }


    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleChanger.onConfigurationChanged()
    }
}