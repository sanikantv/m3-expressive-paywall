@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.sanikant.m3.paywall

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sanikant.m3.paywall.internal.ComplianceFooter
import dev.sanikant.m3.paywall.internal.ExpressiveCta
import dev.sanikant.m3.paywall.internal.PaywallHeader
import dev.sanikant.m3.paywall.internal.PaywallScaffold
import dev.sanikant.m3.paywall.internal.PlanCard
import dev.sanikant.m3.paywall.internal.PreviewData
import dev.sanikant.m3.paywall.internal.PreviewTheme
import dev.sanikant.m3.paywall.internal.ctaLabel
import dev.sanikant.m3.paywall.internal.initialPlan

/**
 * Default "Today, reminder, billing" steps for a trial of [trialDays] days on [plan].
 * Pass your own list to [TrialTimelinePaywall] to change the copy or icons.
 */
@Composable
public fun defaultTrialSteps(
    plan: PaywallPlan,
    trialDays: Int = 7,
    reminderDay: Int = trialDays - 2,
): List<TrialStep> = listOf(
    TrialStep(stringResource(R.string.pw_trial_today), stringResource(R.string.pw_trial_today_desc), PaywallIcons.LockOpen),
    TrialStep(stringResource(R.string.pw_trial_day, reminderDay), stringResource(R.string.pw_trial_reminder_desc), PaywallIcons.Bell),
    TrialStep(
        stringResource(R.string.pw_trial_day, trialDays),
        stringResource(R.string.pw_trial_billing_desc, plan.price, plan.period),
        PaywallIcons.Star,
    ),
)

/**
 * Design 3, trial timeline: explains exactly when the user is reminded and charged, which
 * builds trust for free trials. The timeline describes the first plan that has a trial and
 * fades when a plan without a trial is selected.
 *
 * @param steps timeline steps. Defaults to [defaultTrialSteps] for the trial plan.
 * @see ClassicPlansPaywall for the shared parameters.
 */
@Composable
public fun TrialTimelinePaywall(
    plans: List<PaywallPlan>,
    legal: PaywallLegal,
    onPurchase: (PaywallPlan) -> Unit,
    onRestore: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.pw_trial_title),
    subtitle: String? = stringResource(R.string.pw_trial_subtitle),
    steps: List<TrialStep> = defaultTrialSteps(plans.firstOrNull { it.trialText != null } ?: plans.first()),
    initialPlanId: String? = null,
) {
    var selectedId by rememberSaveable {
        mutableStateOf(initialPlanId ?: plans.firstOrNull { it.trialText != null }?.id ?: plans.initialPlan(null).id)
    }
    val selected = plans.firstOrNull { it.id == selectedId } ?: plans.first()
    val timelineAlpha by animateFloatAsState(
        targetValue = if (selected.trialText != null) 1f else 0.38f,
        animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec(),
        label = "timelineAlpha",
    )

    PaywallScaffold(
        onClose = onClose,
        modifier = modifier,
        backdrop = MaterialTheme.colorScheme.secondaryContainer,
        bottomBar = {
            ExpressiveCta(ctaLabel(selected), onClick = { onPurchase(selected) })
            ComplianceFooter(selected, legal, onRestore)
        },
    ) {
        PaywallHeader(title, subtitle)
        Spacer(Modifier.height(32.dp))
        Column(Modifier.fillMaxWidth().graphicsLayer { alpha = timelineAlpha }) {
            steps.forEachIndexed { index, step ->
                TimelineRow(step, index = index, isLast = index == steps.lastIndex)
            }
        }
        Spacer(Modifier.height(16.dp))
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            plans.forEach { plan ->
                PlanCard(plan, selected = plan.id == selected.id, onSelect = { selectedId = plan.id })
            }
        }
    }
}

private val NodeShapes = listOf(MaterialShapes.Cookie4Sided, MaterialShapes.Cookie6Sided, MaterialShapes.Sunny)

@Composable
private fun TimelineRow(step: TrialStep, index: Int, isLast: Boolean) {
    val colors = MaterialTheme.colorScheme
    val active = index == 0
    Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        Column(Modifier.width(52.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier
                    .size(52.dp)
                    .background(
                        if (active) colors.primary else colors.primaryContainer,
                        NodeShapes[index % NodeShapes.size].toShape(),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    step.icon,
                    contentDescription = null,
                    tint = if (active) colors.onPrimary else colors.onPrimaryContainer,
                    modifier = Modifier.size(24.dp),
                )
            }
            if (!isLast) {
                Box(
                    Modifier
                        .padding(vertical = 4.dp)
                        .width(6.dp)
                        .weight(1f)
                        .background(
                            Brush.verticalGradient(listOf(colors.primary, colors.primary.copy(alpha = 0.25f))),
                            CircleShape,
                        ),
                )
            }
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f).padding(top = 4.dp, bottom = if (isLast) 8.dp else 28.dp)) {
            Text(step.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = colors.onSurface)
            Spacer(Modifier.height(4.dp))
            Text(step.description, style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant)
        }
    }
}

@Preview(name = "Trial timeline · light", showSystemUi = true)
@Preview(name = "Trial timeline · dark", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TrialTimelinePaywallPreview() {
    PreviewTheme {
        TrialTimelinePaywall(PreviewData.plans.take(2).reversed(), PreviewData.legal, {}, {}, {})
    }
}
