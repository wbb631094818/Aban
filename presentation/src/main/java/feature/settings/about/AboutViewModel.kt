/*
 * Copyright (c) 2018. Arash Hatami
 */

package feature.settings.about

import ir.hatamiarash.aban.R
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.kotlin.autoDisposable
import common.Navigator
import common.base.AbanViewModel
import javax.inject.Inject

class AboutViewModel @Inject constructor(
        private val navigator: Navigator
) : AbanViewModel<AboutView, Unit>(Unit) {

    override fun bindView(view: AboutView) {
        super.bindView(view)

        view.preferenceClickIntent
                .autoDisposable(view.scope())
                .subscribe { preference ->
                    when (preference.id) {
                        R.id.developer -> navigator.showDeveloper()

                        R.id.source -> navigator.showSourceCode()

                        R.id.changelog -> navigator.showChangelog()

                        R.id.contact -> navigator.showSupport()

                        R.id.license -> navigator.showLicense()
                    }
                }
    }

}