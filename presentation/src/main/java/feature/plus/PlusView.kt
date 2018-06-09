/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.plus

import common.base.AbanView
import common.util.BillingManager
import io.reactivex.Observable

interface PlusView : AbanView<PlusState> {

    val upgradeIntent: Observable<Unit>
    val upgradeDonateIntent: Observable<Unit>
    val donateIntent: Observable<*>

    fun initiatePurchaseFlow(billingManager: BillingManager, sku: String)

}