package dev.sanikant.m3.paywall.snippets

import android.app.Activity
import android.content.Context
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.PackageType
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.getOfferingsWith
import com.revenuecat.purchases.models.Period
import com.revenuecat.purchases.purchaseWith
import com.revenuecat.purchases.restorePurchasesWith
import dev.sanikant.m3.paywall.PaywallPlan

// region rc-configure
// Read the public SDK key from local.properties through BuildConfig. Never commit it.
fun configureRevenueCat(context: Context, apiKey: String) {
    Purchases.configure(PurchasesConfiguration.Builder(context, apiKey).build())
}
// endregion

// region rc-map
fun Package.toPaywallPlan(): PaywallPlan = PaywallPlan(
    id = identifier,
    title = when (packageType) {
        PackageType.ANNUAL -> "Yearly"
        PackageType.MONTHLY -> "Monthly"
        PackageType.LIFETIME -> "Lifetime"
        else -> product.title
    },
    price = product.price.formatted,
    period = product.period?.let { "per ${it.unit.name.lowercase()}" } ?: "one-time",
    trialText = product.defaultOption?.freePhase?.billingPeriod?.let { "${it.value}-${it.unit.word()} free trial" },
    badge = if (packageType == PackageType.ANNUAL) "Best value" else null,
    isSubscription = product.period != null,
)

private fun Period.Unit.word(): String = name.lowercase()
// endregion

// region rc-load
fun loadPlans(onLoaded: (packages: List<Package>, plans: List<PaywallPlan>) -> Unit) {
    Purchases.sharedInstance.getOfferingsWith(
        onError = { error -> println("Offerings failed: ${error.message}") },
        onSuccess = { offerings ->
            val packages = offerings.current?.availablePackages.orEmpty()
            onLoaded(packages, packages.map { it.toPaywallPlan() })
        },
    )
}
// endregion

// region rc-purchase
fun purchase(activity: Activity, packages: List<Package>, plan: PaywallPlan, onUnlocked: () -> Unit) {
    val pkg = packages.first { it.identifier == plan.id }
    Purchases.sharedInstance.purchaseWith(
        PurchaseParams.Builder(activity, pkg).build(),
        onError = { error, userCancelled -> if (!userCancelled) println(error.message) },
        onSuccess = { _, customerInfo ->
            if (customerInfo.entitlements["pro"]?.isActive == true) onUnlocked()
        },
    )
}

fun restore(onUnlocked: () -> Unit) {
    Purchases.sharedInstance.restorePurchasesWith(
        onError = { error -> println(error.message) },
        onSuccess = { customerInfo ->
            if (customerInfo.entitlements["pro"]?.isActive == true) onUnlocked()
        },
    )
}
// endregion
