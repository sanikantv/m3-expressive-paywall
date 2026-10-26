package dev.sanikant.m3.paywall.snippets

import dev.sanikant.m3.paywall.BenefitPage
import dev.sanikant.m3.paywall.ComparisonRow
import dev.sanikant.m3.paywall.PaywallFeature
import dev.sanikant.m3.paywall.PaywallIcons
import dev.sanikant.m3.paywall.PaywallLegal
import dev.sanikant.m3.paywall.PaywallPlan

/** Shared data for the component snippets. Real apps map these from their billing SDK. */
object Sample {
    // region plans
    val monthly = PaywallPlan(id = "pro_monthly", title = "Monthly", price = "$4.99", period = "per month")
    val yearly = PaywallPlan(
        id = "pro_yearly",
        title = "Yearly",
        price = "$29.99",
        period = "per year",
        trialText = "7-day free trial",
        badge = "Best value",
        priceDetail = "$2.49/month, billed yearly",
    )
    val lifetime = PaywallPlan(
        id = "pro_lifetime",
        title = "Lifetime",
        price = "$79.99",
        period = "one-time",
        priceDetail = "Pay once, yours forever",
        isSubscription = false,
    )
    val plans = listOf(monthly, yearly, lifetime)
    // endregion

    // region legal
    val legal = PaywallLegal(
        termsUrl = "https://your.app/terms",
        privacyUrl = "https://your.app/privacy",
    )
    // endregion

    // region features
    val features = listOf(
        PaywallFeature("Unlimited notes", "No caps on notes, folders or attachments", PaywallIcons.Sparkle),
        PaywallFeature("Sync everywhere", "Phone, tablet and web, always up to date", PaywallIcons.Cloud),
        PaywallFeature("Smart reminders", "Nudges at the right time and place", PaywallIcons.Bell),
        PaywallFeature("Zero ads", "A calm space for your thoughts", PaywallIcons.Block),
    )
    // endregion

    // region comparison-rows
    val rows = listOf(
        ComparisonRow("Notes", free = true, freeText = "50", proText = "Unlimited"),
        ComparisonRow("Basic themes", free = true),
        ComparisonRow("Cloud sync", free = false),
        ComparisonRow("Smart reminders", free = false),
        ComparisonRow("Ad-free", free = false),
    )
    // endregion

    // region pages
    val pages = listOf(
        BenefitPage("Capture every idea", "Write, sketch and save in one tap, even offline.", PaywallIcons.Sparkle),
        BenefitPage("Remember on time", "Smart reminders bring notes back when you need them.", PaywallIcons.Bell),
        BenefitPage("See your progress", "Weekly insights show how your ideas grow.", PaywallIcons.Insights),
    )
    // endregion
}
