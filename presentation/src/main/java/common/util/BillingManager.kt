/*
 * Copyright (c) 2018. Arash Hatami
 */
package common.util

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponse
import com.android.billingclient.api.BillingClient.SkuType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import ir.hatamiarash.aban.BuildConfig
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import manager.AnalyticsManager
import javax.inject.Inject

class BillingManager @Inject constructor(
        context: Context,
        private val analyticsManager: AnalyticsManager
) : PurchasesUpdatedListener {

    companion object {
        const val SKU_PLUS = "remove_ads"
        const val SKU_PLUS_DONATE = "qksms_plus_donate"
    }

    val products: Observable<List<SkuDetails>> = BehaviorSubject.create()
    val upgradeStatus: Observable<Boolean>

    private val skus = listOf(SKU_PLUS, SKU_PLUS_DONATE)
    private val purchaseListObservable = BehaviorSubject.createDefault<List<Purchase>>(listOf())

    private val billingClient: BillingClient = BillingClient.newBuilder(context).setListener(this).build()
    private var isServiceConnected = false

    init {
        startServiceConnection {
            queryPurchases()
            querySkuDetailsAsync()
        }

        upgradeStatus = when (BuildConfig.FLAVOR) {
            "noAnalytics" -> BehaviorSubject.createDefault(true)

            else -> purchaseListObservable
                    .map { purchases -> purchases.any { it.sku == SKU_PLUS } || purchases.any { it.sku == SKU_PLUS_DONATE } }
                    .doOnNext { upgraded -> analyticsManager.setUserProperty("Upgraded", upgraded) }
        }
    }

    private fun queryPurchases() {
        executeServiceRequest {
            val purchasesResult = billingClient.queryPurchases(SkuType.INAPP)

            // Handle purchase result
            purchaseListObservable.onNext(purchasesResult.purchasesList.orEmpty())
        }
    }


    private fun startServiceConnection(onSuccess: () -> Unit) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(@BillingResponse billingResponseCode: Int) {
                if (billingResponseCode == BillingResponse.OK) {
                    isServiceConnected = true
                    onSuccess()
                }
            }

            override fun onBillingServiceDisconnected() {
                isServiceConnected = false
            }
        })
    }

    private fun querySkuDetailsAsync() {
        executeServiceRequest {
            val subParams = SkuDetailsParams.newBuilder().setSkusList(skus).setType(BillingClient.SkuType.INAPP)
            billingClient.querySkuDetailsAsync(subParams.build()) { responseCode, skuDetailsList ->
                if (responseCode == BillingResponse.OK) {
                    (products as Subject).onNext(skuDetailsList)
                }
            }
        }
    }

    fun initiatePurchaseFlow(activity: Activity, sku: String) {
        executeServiceRequest {
            val params = BillingFlowParams.newBuilder().setSku(sku).setType(SkuType.INAPP)
            billingClient.launchBillingFlow(activity, params.build())
        }
    }

    private fun executeServiceRequest(runnable: () -> Unit) {
        when (isServiceConnected) {
            true -> runnable()
            false -> startServiceConnection(runnable)
        }
    }

    override fun onPurchasesUpdated(resultCode: Int, purchases: List<Purchase>?) {
        if (resultCode == BillingResponse.OK) {
            purchaseListObservable.onNext(purchases.orEmpty())
        }
    }

}