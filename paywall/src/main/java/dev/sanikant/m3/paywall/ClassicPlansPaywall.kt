@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.sanikant.m3.paywall

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sanikant.m3.paywall.internal.ComplianceFooter
import dev.sanikant.m3.paywall.internal.ExpressiveCta
import dev.sanikant.m3.paywall.internal.FeatureRow
import dev.sanikant.m3.paywall.internal.HeroArt
import dev.sanikant.m3.paywall.internal.PaywallHeader
import dev.sanikant.m3.paywall.internal.PaywallScaffold
import dev.sanikant.m3.paywall.internal.PlanCard
import dev.sanikant.m3.paywall.internal.PreviewData
import dev.sanikant.m3.paywall.internal.PreviewTheme
import dev.sanikant.m3.paywall.internal.ctaLabel
import dev.sanikant.m3.paywall.internal.initialPlan

/**
 * Design 1, classic plans: an animated hero, a feature list and stacked plan cards
 * (for example monthly, yearly and lifetime). The badged plan is preselected.
 *
 * @param plans plans to offer, in display order.
 * @param features benefits listed under the title.
 * @param legal Terms and Privacy links shown in the footer.
 * @param onPurchase called with the selected plan when the button is tapped. Start your billing flow here.
 * @param onRestore called from the footer Restore link.
 * @param onClose called from the close button.
 * @param initialPlanId id of the plan to preselect. Defaults to the first plan with a badge.
 */
@Composable
public fun ClassicPlansPaywall(
    plans: List<PaywallPlan>,
    features: List<PaywallFeature>,
    legal: PaywallLegal,
    onPurchase: (PaywallPlan) -> Unit,
    onRestore: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.pw_classic_title),
    subtitle: String? = stringResource(R.string.pw_classic_subtitle),
    initialPlanId: String? = null,
    heroIcon: ImageVector = PaywallIcons.Sparkle,
) {
    var selectedId by rememberSaveable { mutableStateOf(plans.initialPlan(initialPlanId).id) }
    val selected = plans.firstOrNull { it.id == selectedId } ?: plans.first()

    PaywallScaffold(
        onClose = onClose,
        modifier = modifier,
        bottomBar = {
            ExpressiveCta(ctaLabel(selected), onClick = { onPurchase(selected) })
            ComplianceFooter(selected, legal, onRestore)
        },
    ) {
        HeroArt(heroIcon, size = 148.dp)
        Spacer(Modifier.height(16.dp))
        PaywallHeader(title, subtitle)
        Spacer(Modifier.height(20.dp))
        // Plans come before features so prices and the badged plan are visible without scrolling.
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            plans.forEach { plan ->
                PlanCard(plan, selected = plan.id == selected.id, onSelect = { selectedId = plan.id })
            }
        }
        Spacer(Modifier.height(28.dp))
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            features.forEach { FeatureRow(it) }
        }
    }
}

@Preview(name = "Classic · light", showSystemUi = true)
@Preview(name = "Classic · dark", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ClassicPlansPaywallPreview() {
    PreviewTheme {
        ClassicPlansPaywall(PreviewData.plans, PreviewData.features, PreviewData.legal, {}, {}, {})
    }
}
