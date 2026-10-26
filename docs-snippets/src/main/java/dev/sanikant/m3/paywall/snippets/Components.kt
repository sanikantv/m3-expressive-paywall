package dev.sanikant.m3.paywall.snippets

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import dev.sanikant.m3.paywall.ClassicPlansPaywall
import dev.sanikant.m3.paywall.ComparisonPaywall
import dev.sanikant.m3.paywall.LimitedOfferSheet
import dev.sanikant.m3.paywall.OnboardingPaywall
import dev.sanikant.m3.paywall.PaywallIcons
import dev.sanikant.m3.paywall.PaywallPlan
import dev.sanikant.m3.paywall.PlanPickerPaywall
import dev.sanikant.m3.paywall.TrialStep
import dev.sanikant.m3.paywall.TrialTimelinePaywall
import dev.sanikant.m3.paywall.defaultTrialSteps

@Composable
fun ClassicUsage(buy: (PaywallPlan) -> Unit, restore: () -> Unit, close: () -> Unit) {
    // region classic-usage
    ClassicPlansPaywall(
        plans = Sample.plans,
        features = Sample.features,
        legal = Sample.legal,
        onPurchase = { plan -> buy(plan) },
        onRestore = restore,
        onClose = close,
    )
    // endregion
}

@Composable
fun ClassicCustom(buy: (PaywallPlan) -> Unit, restore: () -> Unit, close: () -> Unit) {
    // region classic-custom
    ClassicPlansPaywall(
        plans = Sample.plans,
        features = Sample.features,
        legal = Sample.legal,
        onPurchase = buy,
        onRestore = restore,
        onClose = close,
        title = "Unlock Lumen Pro",
        subtitle = "Everything you need to think clearly.",
        initialPlanId = "pro_lifetime",
        heroIcon = PaywallIcons.Star,
    )
    // endregion
}

@Composable
fun ComparisonUsage(buy: (PaywallPlan) -> Unit, restore: () -> Unit, close: () -> Unit) {
    // region comparison-usage
    ComparisonPaywall(
        plans = listOf(Sample.monthly, Sample.yearly),
        rows = Sample.rows,
        legal = Sample.legal,
        onPurchase = buy,
        onRestore = restore,
        onClose = close,
        freeLabel = "Basic",
        proLabel = "Plus",
    )
    // endregion
}

@Composable
fun TrialUsage(buy: (PaywallPlan) -> Unit, restore: () -> Unit, close: () -> Unit) {
    // region trial-usage
    TrialTimelinePaywall(
        plans = listOf(Sample.yearly, Sample.monthly),
        legal = Sample.legal,
        onPurchase = buy,
        onRestore = restore,
        onClose = close,
    )
    // endregion
}

@Composable
fun TrialCustomSteps(buy: (PaywallPlan) -> Unit, restore: () -> Unit, close: () -> Unit) {
    // region trial-14-days
    TrialTimelinePaywall(
        plans = listOf(Sample.yearly.copy(trialText = "14-day free trial")),
        legal = Sample.legal,
        onPurchase = buy,
        onRestore = restore,
        onClose = close,
        steps = defaultTrialSteps(Sample.yearly, trialDays = 14, reminderDay = 12),
    )
    // endregion
}

@Composable
fun TrialOwnCopy(buy: (PaywallPlan) -> Unit, restore: () -> Unit, close: () -> Unit) {
    // region trial-own-steps
    val steps = listOf(
        TrialStep("Today", "Every Pro feature unlocks right away.", PaywallIcons.LockOpen),
        TrialStep("Day 5", "We email you a reminder. No surprise charges.", PaywallIcons.Bell),
        TrialStep("Day 7", "Your plan starts at ${Sample.yearly.price} ${Sample.yearly.period}.", PaywallIcons.Calendar),
    )
    TrialTimelinePaywall(
        plans = listOf(Sample.yearly, Sample.monthly),
        legal = Sample.legal,
        onPurchase = buy,
        onRestore = restore,
        onClose = close,
        steps = steps,
    )
    // endregion
}

@Composable
fun PickerUsage(buy: (PaywallPlan) -> Unit, restore: () -> Unit, close: () -> Unit) {
    // region picker-usage
    PlanPickerPaywall(
        plans = Sample.plans, // 2 to 4 plans with short titles fit the button group
        features = Sample.features.take(3),
        legal = Sample.legal,
        onPurchase = buy,
        onRestore = restore,
        onClose = close,
        initialPlanId = "pro_yearly",
    )
    // endregion
}

// region offer-usage
// LimitedOfferSheet takes a material3 SheetState, which material3 marks experimental.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferScreen(buy: (PaywallPlan) -> Unit, restore: () -> Unit) {
    var showOffer by rememberSaveable { mutableStateOf(true) }
    // Keep the deadline stable. In a real app, store it (e.g. in DataStore) so it survives restarts.
    val endsAt = remember { System.currentTimeMillis() + 15 * 60_000L }

    if (showOffer) {
        LimitedOfferSheet(
            plan = Sample.yearly.copy(
                price = "$14.99",
                trialText = null,
                priceDetail = "First year only, then $29.99 per year",
            ),
            originalPrice = "$29.99",
            discountLabel = "-50%",
            offerEndsAtMillis = endsAt,
            offerDurationMillis = 15 * 60_000L,
            legal = Sample.legal,
            onPurchase = buy,
            onRestore = restore,
            onClose = { showOffer = false },
        )
    }
}
// endregion

@Composable
fun OnboardingUsage(buy: (PaywallPlan) -> Unit, restore: () -> Unit, finishOnboarding: () -> Unit) {
    // region onboarding-usage
    OnboardingPaywall(
        pages = Sample.pages,
        plans = Sample.plans,
        legal = Sample.legal,
        onPurchase = buy,
        onRestore = restore,
        onClose = finishOnboarding,
    )
    // endregion
}

@Composable
fun OnboardingStartOnPlans(buy: (PaywallPlan) -> Unit, restore: () -> Unit, close: () -> Unit) {
    // region onboarding-pager
    // Open straight on the plan page, e.g. when the user taps "Upgrade" later in the app.
    val pagerState = rememberPagerState(initialPage = Sample.pages.size) { Sample.pages.size + 1 }
    OnboardingPaywall(
        pages = Sample.pages,
        plans = Sample.plans,
        legal = Sample.legal,
        onPurchase = buy,
        onRestore = restore,
        onClose = close,
        pagerState = pagerState,
    )
    // endregion
}
