/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.plus

import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.kotlin.autoDisposable
import common.Navigator
import common.base.AbanViewModel
import common.util.BillingManager
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import manager.AnalyticsManager
import javax.inject.Inject

class PlusViewModel @Inject constructor(
        private val analyticsManager: AnalyticsManager,
        private val billingManager: BillingManager,
        private val navigator: Navigator
) : AbanViewModel<PlusView, PlusState>(PlusState()) {

    init {
        analyticsManager.track("Viewed QKSMS+")

        disposables += billingManager.upgradeStatus
                .subscribe { upgraded -> newState { it.copy(upgraded = upgraded) } }

        disposables += billingManager.products
                .subscribe { products ->
                    newState {
                        val upgrade = products.firstOrNull { it.sku == BillingManager.SKU_PLUS }
                        val upgradeDonate = products.firstOrNull { it.sku == BillingManager.SKU_PLUS_DONATE }
                        it.copy(upgradePrice = upgrade?.price ?: "", upgradeDonatePrice = upgradeDonate?.price ?: "",
                                currency = upgrade?.priceCurrencyCode ?: upgradeDonate?.priceCurrencyCode ?: "")
                    }
                }
    }

    override fun bindView(view: PlusView) {
        super.bindView(view)

        Observable.merge(
                view.upgradeIntent.map { BillingManager.SKU_PLUS },
                view.upgradeDonateIntent.map { BillingManager.SKU_PLUS_DONATE })
                .doOnNext { sku -> analyticsManager.track("Clicked Upgrade", Pair("sku", sku)) }
                .autoDisposable(view.scope())
                .subscribe { sku -> view.initiatePurchaseFlow(billingManager, sku) }

        view.donateIntent
                .autoDisposable(view.scope())
                .subscribe { navigator.showDonation() }
    }

}