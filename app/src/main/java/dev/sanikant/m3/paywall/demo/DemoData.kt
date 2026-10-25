package dev.sanikant.m3.paywall.demo

import dev.sanikant.m3.paywall.BenefitPage
import dev.sanikant.m3.paywall.ComparisonRow
import dev.sanikant.m3.paywall.PaywallFeature
import dev.sanikant.m3.paywall.PaywallIcons
import dev.sanikant.m3.paywall.PaywallLegal
import dev.sanikant.m3.paywall.PaywallPlan

/**
 * Fake data for "Lumen", an imaginary notes app. Prices are placeholders: in a real app map
 * them from Play Billing `ProductDetails` or RevenueCat `Package` objects.
 */
object DemoData {
    val monthly = PaywallPlan(id = "lumen_monthly", title = "Monthly", price = "$4.99", period = "per month")
    val yearly = PaywallPlan(
        id = "lumen_yearly",
        title = "Yearly",
        price = "$29.99",
        period = "per year",
        trialText = "7-day free trial",
        badge = "Best value",
        priceDetail = "$2.49/month, billed yearly",
    )
    val lifetime = PaywallPlan(
        id = "lumen_lifetime",
        title = "Lifetime",
        price = "$79.99",
        period = "one-time",
        priceDetail = "Pay once, yours forever",
        isSubscription = false,
    )
    val plans = listOf(monthly, yearly, lifetime)

    val offerPlan = yearly.copy(
        id = "lumen_yearly_offer",
        price = "$14.99",
        trialText = null,
        badge = null,
        priceDetail = "First year only, then $29.99 per year",
    )

    val legal = PaywallLegal(
        termsUrl = "https://example.com/terms",
        privacyUrl = "https://example.com/privacy",
    )

    val features = listOf(
        PaywallFeature("Unlimited notes", "No caps on notes, folders or attachments", PaywallIcons.Sparkle),
        PaywallFeature("Sync everywhere", "Phone, tablet and web, always up to date", PaywallIcons.Cloud),
        PaywallFeature("Smart reminders", "Nudges at the right time and place", PaywallIcons.Bell),
        PaywallFeature("Zero ads", "A calm space for your thoughts", PaywallIcons.Block),
    )

    val comparison = listOf(
        ComparisonRow("Notes", free = true, freeText = "50", proText = "Unlimited"),
        ComparisonRow("Basic themes", free = true),
        ComparisonRow("Cloud sync", free = false),
        ComparisonRow("Smart reminders", free = false),
        ComparisonRow("Insights and stats", free = false),
        ComparisonRow("Ad-free", free = false),
    )

    val onboardingPages = listOf(
        BenefitPage("Capture every idea", "Write, sketch and save in one tap, even offline.", PaywallIcons.Sparkle),
        BenefitPage("Remember on time", "Smart reminders bring notes back when you need them.", PaywallIcons.Bell),
        BenefitPage("See your progress", "Weekly insights show how your ideas grow.", PaywallIcons.Insights),
    )
}
