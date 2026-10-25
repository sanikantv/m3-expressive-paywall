@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.sanikant.m3.paywall.internal

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.expressiveLightColorScheme
import androidx.compose.runtime.Composable
import dev.sanikant.m3.paywall.BenefitPage
import dev.sanikant.m3.paywall.ComparisonRow
import dev.sanikant.m3.paywall.PaywallFeature
import dev.sanikant.m3.paywall.PaywallIcons
import dev.sanikant.m3.paywall.PaywallLegal
import dev.sanikant.m3.paywall.PaywallPlan

/** Theme and fake data for `@Preview`s only. */
@Composable
internal fun PreviewTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialExpressiveTheme(
        colorScheme = if (dark) darkColorScheme() else expressiveLightColorScheme(),
        motionScheme = MotionScheme.expressive(),
        content = content,
    )
}

internal object PreviewData {
    val plans = listOf(
        PaywallPlan("monthly", "Monthly", "$4.99", "per month"),
        PaywallPlan("yearly", "Yearly", "$29.99", "per year", trialText = "7-day free trial", badge = "Best value", priceDetail = "$2.49/month"),
        PaywallPlan("lifetime", "Lifetime", "$79.99", "one-time", isSubscription = false),
    )
    val legal = PaywallLegal("https://example.com/terms", "https://example.com/privacy")
    val features = listOf(
        PaywallFeature("Unlimited projects", "Create as many as you like", PaywallIcons.Sparkle),
        PaywallFeature("No ads", "A calm, focused experience", PaywallIcons.Block),
        PaywallFeature("Cloud sync", "Your work on every device", PaywallIcons.Cloud),
    )
    val rows = listOf(
        ComparisonRow("Projects", free = true, freeText = "3", proText = "Unlimited"),
        ComparisonRow("Cloud sync", free = false),
        ComparisonRow("Ad-free", free = false),
        ComparisonRow("Basic themes", free = true),
    )
    val pages = listOf(
        BenefitPage("Plan your day", "Everything in one calm place.", PaywallIcons.Calendar),
        BenefitPage("Stay on track", "Gentle reminders, never noise.", PaywallIcons.Bell),
    )
}
