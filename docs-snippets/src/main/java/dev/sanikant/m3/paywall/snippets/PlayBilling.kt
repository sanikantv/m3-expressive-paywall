package dev.sanikant.m3.paywall.snippets

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import dev.sanikant.m3.paywall.PaywallPlan

// region play-query
/** Loads one subscription product. Call after the BillingClient is connected. */
suspend fun BillingClient.loadSubscription(productId: String): ProductDetails? {
    val product = QueryProductDetailsParams.Product.newBuilder()
        .setProductId(productId)
        .setProductType(BillingClient.ProductType.SUBS)
        .build()
    val params = QueryProductDetailsParams.newBuilder().setProductList(listOf(product)).build()
    return queryProductDetails(params).productDetailsList?.firstOrNull()
}
// endregion

// region play-map
/** One PaywallPlan per base plan or offer. The offer token becomes the plan id. */
fun ProductDetails.toPaywallPlans(): List<PaywallPlan> =
    subscriptionOfferDetails.orEmpty().map { offer ->
        val phases = offer.pricingPhases.pricingPhaseList
        val recurring = phases.last()                            // the price after any trial or intro phase
        val freeTrial = phases.firstOrNull { it.priceAmountMicros == 0L }
        val yearly = recurring.billingPeriod == "P1Y"
        PaywallPlan(
            id = offer.offerToken,
            title = if (yearly) "Yearly" else "Monthly",
            price = recurring.formattedPrice,                    // already localized by Google Play
            period = if (yearly) "per year" else "per month",
            trialText = freeTrial?.let { "${isoPeriodToText(it.billingPeriod)} free trial" },
            badge = if (yearly) "Best value" else null,
        )
    }

/** "P7D" -> "7-day", "P1M" -> "1-month". Google Play uses ISO 8601 periods. */
fun isoPeriodToText(iso: String): String {
    val match = Regex("P(\\d+)([DWMY])").find(iso) ?: return iso
    val (amount, unit) = match.destructured
    val word = when (unit) { "D" -> "day"; "W" -> "week"; "M" -> "month"; else -> "year" }
    return "$amount-$word"
}
// endregion

// region play-launch
fun launchPurchase(
    activity: Activity,
    billingClient: BillingClient,
    product: ProductDetails,
    plan: PaywallPlan,
): BillingResult {
    val productParams = BillingFlowParams.ProductDetailsParams.newBuilder()
        .setProductDetails(product)
        .setOfferToken(plan.id)
        .build()
    val flowParams = BillingFlowParams.newBuilder()
        .setProductDetailsParamsList(listOf(productParams))
        .build()
    return billingClient.launchBillingFlow(activity, flowParams)
}
// endregion

// region play-restore
/** "Restore" on Google Play means: ask Play which subscriptions this account already owns. */
suspend fun BillingClient.activeSubscriptions(): List<Purchase> {
    val params = QueryPurchasesParams.newBuilder()
        .setProductType(BillingClient.ProductType.SUBS)
        .build()
    return queryPurchasesAsync(params).purchasesList
        .filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }
}
// endregion
