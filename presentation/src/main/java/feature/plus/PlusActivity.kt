/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.plus

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import com.franmontiel.localechanger.LocaleChanger
import com.jakewharton.rxbinding2.view.clicks
import ir.hatamiarash.aban.BuildConfig
import ir.hatamiarash.aban.R
import common.base.AbanThemedActivity
import common.util.BillingManager
import common.util.FontProvider
import common.util.extensions.resolveThemeColor
import common.util.extensions.setBackgroundTint
import common.util.extensions.setTint
import common.util.extensions.setVisible
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.qksms_plus_activity.*
import java.util.*
import javax.inject.Inject

class PlusActivity : AbanThemedActivity(), PlusView {

    @Inject lateinit var fontProvider: FontProvider
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory)[PlusViewModel::class.java] }

    override val upgradeIntent by lazy { upgrade.clicks() }
    override val upgradeDonateIntent by lazy { upgradeDonate.clicks() }
    override val donateIntent by lazy { donate.clicks() }

    override fun attachBaseContext(newBase: Context) {
        var base = newBase
        base = LocaleChanger.configureBaseContext(base)
        super.attachBaseContext(base)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        LocaleChanger.setLocale(Locale("fa", "IR"));
        setContentView(R.layout.qksms_plus_activity)
        setTitle(R.string.title_qksms_plus)
        showBackButton(true)
        viewModel.bindView(this)

        free.setVisible(false)

        if (!prefs.systemFont.get()) {
            fontProvider.getLato { lato ->
                val typeface = Typeface.create(lato, Typeface.BOLD)
                collapsingToolbar.setCollapsedTitleTypeface(typeface)
                collapsingToolbar.setExpandedTitleTypeface(typeface)
            }
        }

        val textPrimary = resolveThemeColor(android.R.attr.textColorPrimary)
        collapsingToolbar.setCollapsedTitleTextColor(textPrimary)
        collapsingToolbar.setExpandedTitleColor(textPrimary)

        val theme = colors.theme().theme
        donate.setBackgroundTint(theme)
        upgrade.setBackgroundTint(theme)
        thanksIcon.setTint(theme)
    }

    override fun render(state: PlusState) {
        description.text = getString(R.string.qksms_plus_description_summary, state.upgradePrice)
        upgrade.text = getString(R.string.qksms_plus_upgrade, state.upgradePrice, state.currency)
        upgradeDonate.text = getString(R.string.qksms_plus_upgrade_donate, state.upgradeDonatePrice, state.currency)

        val fdroid = BuildConfig.FLAVOR == "noAnalytics"

        free.setVisible(fdroid)
        toUpgrade.setVisible(!fdroid && !state.upgraded)
        upgraded.setVisible(!fdroid && state.upgraded)
    }

    override fun initiatePurchaseFlow(billingManager: BillingManager, sku: String) {
        billingManager.initiatePurchaseFlow(this, sku)
    }

}