/*
 * Copyright (c) 2018. Arash Hatami
 */
package injection

import common.AbanApplication

internal lateinit var appComponent: AppComponent
    private set

internal object AppComponentManager {

    fun init(application: AbanApplication) {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(application))
                .build()
    }

}