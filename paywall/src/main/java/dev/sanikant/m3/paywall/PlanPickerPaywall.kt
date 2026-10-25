@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.sanikant.m3.paywall

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sanikant.m3.paywall.internal.ComplianceFooter
import dev.sanikant.m3.paywall.internal.ExpressiveCta
import dev.sanikant.m3.paywall.internal.FeatureRow
import dev.sanikant.m3.paywall.internal.PaywallHeader
import dev.sanikant.m3.paywall.internal.PaywallScaffold
import dev.sanikant.m3.paywall.internal.PaywallShapes
import dev.sanikant.m3.paywall.internal.PlanBadge
import dev.sanikant.m3.paywall.internal.PreviewData
import dev.sanikant.m3.paywall.internal.PreviewTheme
import dev.sanikant.m3.paywall.internal.ctaLabel
import dev.sanikant.m3.paywall.internal.initialPlan
import dev.sanikant.m3.paywall.internal.rememberMorphingShape

/**
 * Design 4, expressive plan picker: a connected button group switches plans, and the
 * spotlight card below morphs its shape and rolls the price for each choice.
 * Designed for two to four plans with short titles.
 *
 * @see ClassicPlansPaywall for the shared parameters.
 */
@Composable
public fun PlanPickerPaywall(
    plans: List<PaywallPlan>,
    features: List<PaywallFeature>,
    legal: PaywallLegal,
    onPurchase: (PaywallPlan) -> Unit,
    onRestore: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.pw_picker_title),
    subtitle: String? = stringResource(R.string.pw_picker_subtitle),
    initialPlanId: String? = null,
) {
    var selectedId by rememberSaveable { mutableStateOf(plans.initialPlan(initialPlanId).id) }
    val selectedIndex = plans.indexOfFirst { it.id == selectedId }.coerceAtLeast(0)
    val selected = plans[selectedIndex]

    PaywallScaffold(
        onClose = onClose,
        modifier = modifier,
        bottomBar = {
            ExpressiveCta(ctaLabel(selected), onClick = { onPurchase(selected) })
            ComplianceFooter(selected, legal, onRestore)
        },
    ) {
        PaywallHeader(title, subtitle)
        Spacer(Modifier.height(28.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
        ) {
            plans.forEachIndexed { index, plan ->
                ToggleButton(
                    checked = index == selectedIndex,
                    onCheckedChange = { selectedId = plan.id },
                    shapes = when (index) {
                        0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                        plans.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                        else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .semantics { role = Role.RadioButton },
                ) {
                    Text(plan.title, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Spotlight(selected, selectedIndex)
        Spacer(Modifier.height(28.dp))
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            features.forEach { FeatureRow(it) }
        }
    }
}

@Composable
private fun Spotlight(plan: PaywallPlan, index: Int) {
    val colors = MaterialTheme.colorScheme
    val motion = MaterialTheme.motionScheme
    val shapes = PaywallShapes.Spotlight
    val morphShape = rememberMorphingShape(shapes[index % shapes.size])
    val corner by animateDpAsState(if (index % 2 == 1) 48.dp else 32.dp, motion.defaultSpatialSpec(), label = "spotCorner")
    val container by animateColorAsState(
        if (index % 2 == 1) colors.primaryContainer else colors.tertiaryContainer,
        motion.defaultEffectsSpec(),
        label = "spotContainer",
    )
    val onContainer = if (index % 2 == 1) colors.onPrimaryContainer else colors.onTertiaryContainer
    val accent = if (index % 2 == 1) colors.primary else colors.tertiary
    val rotation by animateFloatAsState(index * 72f, motion.slowSpatialSpec(), label = "spotRotation")

    Surface(
        shape = RoundedCornerShape(corner),
        color = container,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(Modifier.height(212.dp)) {
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 36.dp, y = (-28).dp)
                    .size(184.dp)
                    .graphicsLayer { rotationZ = rotation }
                    .background(accent.copy(alpha = 0.22f), morphShape),
            )
            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .size(56.dp)
                    .graphicsLayer { rotationZ = -rotation }
                    .background(accent, morphShape),
            )
            Column(Modifier.padding(24.dp)) {
                if (plan.badge != null) {
                    PlanBadge(plan.badge)
                    Spacer(Modifier.height(12.dp))
                } else {
                    Spacer(Modifier.height(36.dp))
                }
                Text(plan.title, style = MaterialTheme.typography.titleLarge, color = onContainer)
                AnimatedContent(
                    targetState = plan,
                    transitionSpec = {
                        val up = targetState.price.length >= initialState.price.length
                        (slideInVertically(motion.defaultSpatialSpec()) { if (up) it else -it } + fadeIn(motion.defaultEffectsSpec()))
                            .togetherWith(slideOutVertically(motion.defaultSpatialSpec()) { if (up) -it else it } + fadeOut(motion.fastEffectsSpec()))
                    },
                    label = "price",
                ) { shown ->
                    Column {
                        Text(
                            shown.price,
                            style = MaterialTheme.typography.displayMediumEmphasized,
                            fontWeight = FontWeight.Black,
                            color = onContainer,
                        )
                        Text(
                            listOfNotNull(shown.period, shown.trialText ?: shown.priceDetail).joinToString(" · "),
                            style = MaterialTheme.typography.bodyMedium,
                            color = onContainer.copy(alpha = 0.8f),
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Plan picker · light", showSystemUi = true)
@Preview(name = "Plan picker · dark", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PlanPickerPaywallPreview() {
    PreviewTheme {
        PlanPickerPaywall(PreviewData.plans, PreviewData.features, PreviewData.legal, {}, {}, {})
    }
}
