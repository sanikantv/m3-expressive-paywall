@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.sanikant.m3.paywall.internal

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.sanikant.m3.paywall.PaywallFeature
import dev.sanikant.m3.paywall.PaywallIcons
import dev.sanikant.m3.paywall.PaywallLegal
import dev.sanikant.m3.paywall.PaywallPlan
import dev.sanikant.m3.paywall.R

/** Picks the plan to preselect: the requested id, else the first badged plan, else the first. */
internal fun List<PaywallPlan>.initialPlan(initialPlanId: String?): PaywallPlan {
    require(isNotEmpty()) { "A paywall needs at least one PaywallPlan" }
    return firstOrNull { it.id == initialPlanId } ?: firstOrNull { it.badge != null } ?: first()
}

@Composable
internal fun PaywallHeader(title: String, subtitle: String?, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLargeEmphasized,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        if (subtitle != null) {
            Spacer(Modifier.size(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/** Large primary button. Its corners squish on press, and it gives a confirm haptic. */
@Composable
internal fun ExpressiveCta(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val haptics = LocalHapticFeedback.current
    Button(
        onClick = {
            haptics.performHapticFeedback(HapticFeedbackType.Confirm)
            onClick()
        },
        shapes = ButtonDefaults.shapes(),
        enabled = enabled,
        contentPadding = ButtonDefaults.MediumContentPadding,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = ButtonDefaults.MediumContainerHeight),
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
internal fun ctaLabel(plan: PaywallPlan): String =
    stringResource(if (plan.trialText != null) R.string.pw_start_trial else R.string.pw_continue)

/** The auto-renew sentence Google Play expects next to the purchase button. */
@Composable
internal fun disclosure(plan: PaywallPlan): String = when {
    !plan.isSubscription -> stringResource(R.string.pw_disclosure_one_time, plan.price)
    plan.trialText != null -> stringResource(R.string.pw_disclosure_trial, plan.trialText, plan.price, plan.period)
    else -> stringResource(R.string.pw_disclosure_subscription, plan.price, plan.period)
}

/**
 * Price, period and trial disclosure, then Restore, Terms and Privacy. Every paywall in the
 * library ends with this, so the Google Play subscription items are always on screen.
 */
@Composable
internal fun ComplianceFooter(
    plan: PaywallPlan,
    legal: PaywallLegal,
    onRestore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = disclosure(plan),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp, start = 8.dp, end = 8.dp),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            FooterLink(stringResource(R.string.pw_restore), onRestore)
            FooterDot()
            FooterLink(stringResource(R.string.pw_terms)) { uriHandler.openUri(legal.termsUrl) }
            FooterDot()
            FooterLink(stringResource(R.string.pw_privacy)) { uriHandler.openUri(legal.privacyUrl) }
        }
    }
}

@Composable
private fun FooterLink(text: String, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
private fun FooterDot() {
    Box(
        Modifier
            .size(3.dp)
            .background(MaterialTheme.colorScheme.outlineVariant, CircleShape),
    )
}

/** Radio indicator that morphs from a circle into a cookie when selected. */
@Composable
internal fun MorphRadio(selected: Boolean, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val shape = rememberMorphingShape(if (selected) PaywallShapes.Selected else PaywallShapes.Unselected)
    val fill by animateColorAsState(
        if (selected) colors.primary else colors.surface,
        MaterialTheme.motionScheme.defaultEffectsSpec(),
        label = "radioFill",
    )
    val checkAlpha by animateFloatAsState(
        if (selected) 1f else 0f,
        MaterialTheme.motionScheme.defaultEffectsSpec(),
        label = "radioCheck",
    )
    Box(
        modifier
            .size(28.dp)
            .background(fill, shape)
            .border(if (selected) 0.dp else 2.dp, colors.outline, shape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            PaywallIcons.Check,
            contentDescription = null,
            tint = colors.onPrimary,
            modifier = Modifier
                .size(16.dp)
                .graphicsLayer {
                    alpha = checkAlpha
                    scaleX = 0.6f + 0.4f * checkAlpha
                    scaleY = 0.6f + 0.4f * checkAlpha
                },
        )
    }
}

@Composable
internal fun PlanBadge(text: String, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary,
        shape = CircleShape,
        modifier = modifier,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        )
    }
}

/**
 * Full-width selectable plan row. Selection springs the corner radius, tints the container
 * and morphs the radio. Announced as a radio button by TalkBack.
 */
@Composable
internal fun PlanCard(
    plan: PaywallPlan,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val motion = MaterialTheme.motionScheme
    val corner by animateDpAsState(if (selected) 28.dp else 20.dp, motion.fastSpatialSpec(), label = "corner")
    val container by animateColorAsState(
        if (selected) colors.primaryContainer else colors.surfaceContainerHigh,
        motion.defaultEffectsSpec(),
        label = "container",
    )
    val borderColor by animateColorAsState(
        if (selected) colors.primary else colors.surfaceContainerHigh,
        motion.defaultEffectsSpec(),
        label = "border",
    )
    val onContainer = if (selected) colors.onPrimaryContainer else colors.onSurface
    val shape = RoundedCornerShape(corner)

    Box(modifier.fillMaxWidth().padding(top = if (plan.badge != null) 10.dp else 0.dp)) {
        Surface(
            shape = shape,
            color = container,
            border = BorderStroke(2.dp, borderColor),
            modifier = Modifier
                .fillMaxWidth()
                .selectable(selected = selected, role = Role.RadioButton, onClick = onSelect),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
            ) {
                MorphRadio(selected)
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text(plan.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = onContainer)
                    val detail = plan.trialText ?: plan.priceDetail
                    if (detail != null) {
                        Text(
                            detail,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (selected) colors.onPrimaryContainer.copy(alpha = 0.8f) else colors.onSurfaceVariant,
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Text(plan.price, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = onContainer)
                    Text(
                        plan.period,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (selected) colors.onPrimaryContainer.copy(alpha = 0.8f) else colors.onSurfaceVariant,
                    )
                }
            }
        }
        if (plan.badge != null) {
            PlanBadge(
                plan.badge,
                Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-20).dp, y = (-12).dp),
            )
        }
    }
}

/** Compact vertical plan tile for side-by-side layouts. */
@Composable
internal fun PlanTile(
    plan: PaywallPlan,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val motion = MaterialTheme.motionScheme
    val corner by animateDpAsState(if (selected) 32.dp else 20.dp, motion.fastSpatialSpec(), label = "tileCorner")
    val container by animateColorAsState(
        if (selected) colors.primaryContainer else colors.surfaceContainerHigh,
        motion.defaultEffectsSpec(),
        label = "tileContainer",
    )
    val borderColor by animateColorAsState(
        if (selected) colors.primary else colors.surfaceContainerHigh,
        motion.defaultEffectsSpec(),
        label = "tileBorder",
    )
    val onContainer = if (selected) colors.onPrimaryContainer else colors.onSurface
    Box(modifier.padding(top = 10.dp)) {
        Surface(
            shape = RoundedCornerShape(corner),
            color = container,
            border = BorderStroke(2.dp, borderColor),
            modifier = Modifier
                .fillMaxWidth()
                .selectable(selected = selected, role = Role.RadioButton, onClick = onSelect),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 18.dp),
            ) {
                MorphRadio(selected)
                Spacer(Modifier.size(10.dp))
                Text(plan.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = onContainer)
                Spacer(Modifier.size(2.dp))
                Text(plan.price, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = onContainer)
                Text(
                    plan.period,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selected) colors.onPrimaryContainer.copy(alpha = 0.8f) else colors.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
        if (plan.badge != null) {
            PlanBadge(plan.badge, Modifier.align(Alignment.TopCenter).offset(y = (-12).dp))
        }
    }
}

/** Feature row with a tonal, cookie-shaped icon chip. */
@Composable
internal fun FeatureRow(feature: PaywallFeature, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconChip(feature.icon)
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(feature.title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
            if (feature.description != null) {
                Text(
                    feature.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
internal fun IconChip(icon: ImageVector, modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(44.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer, MaterialShapes.Cookie6Sided.toShape()),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.size(22.dp))
    }
}
