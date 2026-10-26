package dev.sanikant.m3.paywall.snippets

// region imports
import androidx.compose.runtime.Composable
import dev.sanikant.m3.paywall.ClassicPlansPaywall
import dev.sanikant.m3.paywall.PaywallFeature
import dev.sanikant.m3.paywall.PaywallIcons
import dev.sanikant.m3.paywall.PaywallLegal
import dev.sanikant.m3.paywall.PaywallPlan
// endregion

// region quick-start
@Composable
fun UpgradeScreen(onBuy: (planId: String) -> Unit, onRestore: () -> Unit, onDismiss: () -> Unit) {
    ClassicPlansPaywall(
        plans = listOf(
            PaywallPlan(id = "monthly", title = "Monthly", price = "$4.99", period = "per month"),
            PaywallPlan(
                id = "yearly", title = "Yearly", price = "$29.99", period = "per year",
                trialText = "7-day free trial", badge = "Best value",
            ),
        ),
        features = listOf(
            PaywallFeature("Unlimited projects", icon = PaywallIcons.Sparkle),
            PaywallFeature("No ads", icon = PaywallIcons.Block),
        ),
        legal = PaywallLegal(termsUrl = "https://example.com/terms", privacyUrl = "https://example.com/privacy"),
        onPurchase = { plan -> onBuy(plan.id) },
        onRestore = onRestore,
        onClose = onDismiss,
    )
}
// endregion
