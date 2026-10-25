package dev.sanikant.m3.paywall

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * One purchasable option shown on a paywall.
 *
 * The library never talks to a billing provider. Map your Play Billing `ProductDetails`,
 * RevenueCat `Package` or any other source to this class and pass formatted strings.
 *
 * @param id your own identifier, handed back in `onPurchase` so you can find the real product.
 * @param title short name, e.g. "Yearly".
 * @param price formatted price from the store, e.g. "$29.99". Never hard-code prices in production.
 * @param period billing period shown next to the price, e.g. "per year" or "one-time".
 * @param trialText free trial terms, e.g. "7-day free trial". `null` means no trial.
 * @param badge optional highlight, e.g. "Best value".
 * @param priceDetail optional helper line, e.g. "$2.49/month, billed yearly".
 * @param isSubscription `false` for one-time purchases such as lifetime access. It changes the
 * auto-renew disclosure shown under the button.
 */
@Immutable
public data class PaywallPlan(
    val id: String,
    val title: String,
    val price: String,
    val period: String,
    val trialText: String? = null,
    val badge: String? = null,
    val priceDetail: String? = null,
    val isSubscription: Boolean = true,
)

/** A benefit row with an icon, used by the classic, plan picker and onboarding paywalls. */
@Immutable
public data class PaywallFeature(
    val title: String,
    val description: String? = null,
    val icon: ImageVector = PaywallIcons.Check,
)

/**
 * Links Google Play expects on every subscription screen. They open with the platform
 * [androidx.compose.ui.platform.UriHandler].
 */
@Immutable
public data class PaywallLegal(
    val termsUrl: String,
    val privacyUrl: String,
)

/** One row of the Free vs Pro table. Text values replace the check or cross icon when set. */
@Immutable
public data class ComparisonRow(
    val label: String,
    val free: Boolean,
    val pro: Boolean = true,
    val freeText: String? = null,
    val proText: String? = null,
)

/** One step of the trial timeline. */
@Immutable
public data class TrialStep(
    val title: String,
    val description: String,
    val icon: ImageVector,
)

/** One page of the onboarding carousel that ends in the plan selection. */
@Immutable
public data class BenefitPage(
    val title: String,
    val description: String,
    val icon: ImageVector,
)
