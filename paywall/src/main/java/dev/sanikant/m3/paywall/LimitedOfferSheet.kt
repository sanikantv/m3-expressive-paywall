@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package dev.sanikant.m3.paywall

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sanikant.m3.paywall.internal.CloseButton
import dev.sanikant.m3.paywall.internal.ComplianceFooter
import dev.sanikant.m3.paywall.internal.ExpressiveCta
import dev.sanikant.m3.paywall.internal.PreviewData
import dev.sanikant.m3.paywall.internal.PreviewTheme
import kotlinx.coroutines.delay

/**
 * Design 5, limited-time offer: a modal bottom sheet with a big discount, the struck-through
 * original price, a live countdown and a wavy progress bar that drains as time runs out.
 * Show it on top of any screen; it dismisses through [onClose].
 *
 * @param plan the discounted plan. Its `price` is the offer price.
 * @param originalPrice regular price shown struck through, e.g. "$59.99".
 * @param discountLabel short discount text, e.g. "-50%".
 * @param offerEndsAtMillis wall-clock time (`System.currentTimeMillis()`) when the offer ends.
 * @param offerDurationMillis full length of the offer, used to scale the wavy progress bar.
 * @see ClassicPlansPaywall for the shared parameters.
 */
@Composable
public fun LimitedOfferSheet(
    plan: PaywallPlan,
    originalPrice: String,
    discountLabel: String,
    offerEndsAtMillis: Long,
    legal: PaywallLegal,
    onPurchase: (PaywallPlan) -> Unit,
    onRestore: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.pw_offer_title),
    subtitle: String = stringResource(R.string.pw_offer_subtitle),
    offerDurationMillis: Long = 15 * 60_000L,
    sheetState: SheetState = rememberBottomSheetState(SheetValue.Hidden, setOf(SheetValue.Hidden, SheetValue.Expanded)),
) {
    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        LimitedOfferContent(
            plan, originalPrice, discountLabel, offerEndsAtMillis, offerDurationMillis,
            legal, onPurchase, onRestore, onClose, title, subtitle,
        )
    }
}

@Composable
private fun LimitedOfferContent(
    plan: PaywallPlan,
    originalPrice: String,
    discountLabel: String,
    offerEndsAtMillis: Long,
    offerDurationMillis: Long,
    legal: PaywallLegal,
    onPurchase: (PaywallPlan) -> Unit,
    onRestore: () -> Unit,
    onClose: () -> Unit,
    title: String,
    subtitle: String,
) {
    val colors = MaterialTheme.colorScheme
    val now by produceState(System.currentTimeMillis(), offerEndsAtMillis) {
        while (true) {
            value = System.currentTimeMillis()
            delay(1_000 - value % 1_000)
        }
    }
    val remaining = (offerEndsAtMillis - now).coerceAtLeast(0L)
    val expired = remaining == 0L
    val fraction by animateFloatAsState(
        targetValue = (remaining.toFloat() / offerDurationMillis).coerceIn(0f, 1f),
        animationSpec = tween(1_000, easing = LinearEasing),
        label = "offerProgress",
    )

    Box {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DiscountBurst(discountLabel)
            Spacer(Modifier.height(16.dp))
            Text(title, style = MaterialTheme.typography.headlineMediumEmphasized, color = colors.onSurface, textAlign = TextAlign.Center)
            Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = colors.onSurfaceVariant, textAlign = TextAlign.Center)
            Spacer(Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
                Text(
                    stringResource(R.string.pw_offer_was, originalPrice),
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = TextDecoration.LineThrough,
                    color = colors.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 6.dp),
                )
                Spacer(Modifier.width(12.dp))
                Text(plan.price, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black, color = colors.primary)
            }
            Text(
                listOfNotNull(plan.title, plan.period).joinToString(" · "),
                style = MaterialTheme.typography.labelLarge,
                color = colors.onSurfaceVariant,
            )
            if (plan.priceDetail != null) {
                Text(plan.priceDetail, style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant, textAlign = TextAlign.Center)
            }
            Spacer(Modifier.height(24.dp))
            Countdown(remaining, expired)
            Spacer(Modifier.height(16.dp))
            LinearWavyProgressIndicator(
                progress = { fraction },
                modifier = Modifier.fillMaxWidth().height(14.dp),
                color = if (expired) colors.outline else colors.primary,
                trackColor = colors.secondaryContainer,
            )
            Spacer(Modifier.height(24.dp))
            ExpressiveCta(
                text = stringResource(if (expired) R.string.pw_offer_expired else R.string.pw_offer_claim),
                onClick = { onPurchase(plan) },
                enabled = !expired,
            )
            ComplianceFooter(plan, legal, onRestore)
            Spacer(Modifier.height(8.dp))
        }
        CloseButton(onClose, Modifier.align(Alignment.TopEnd).padding(end = 12.dp))
    }
}

@Composable
private fun DiscountBurst(label: String) {
    val colors = MaterialTheme.colorScheme
    val transition = rememberInfiniteTransition(label = "burst")
    val spin by transition.animateFloat(0f, 360f, infiniteRepeatable(tween(18_000, easing = LinearEasing)), label = "burstSpin")
    val pulse by transition.animateFloat(0.95f, 1.05f, infiniteRepeatable(tween(900), RepeatMode.Reverse), label = "burstPulse")
    Box(Modifier.size(168.dp), contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .size(168.dp)
                .graphicsLayer {
                    rotationZ = spin
                    scaleX = pulse
                    scaleY = pulse
                }
                .background(colors.tertiary, MaterialShapes.SoftBurst.toShape()),
        )
        Text(
            label,
            style = MaterialTheme.typography.displaySmallEmphasized,
            fontWeight = FontWeight.Black,
            color = colors.onTertiary,
        )
    }
}

@Composable
private fun Countdown(remainingMillis: Long, expired: Boolean) {
    val totalSeconds = remainingMillis / 1_000
    val minutes = (totalSeconds / 60).toString().padStart(2, '0')
    val seconds = (totalSeconds % 60).toString().padStart(2, '0')
    val endsIn = stringResource(R.string.pw_offer_ends_in)
    val minLabel = stringResource(R.string.pw_offer_minutes)
    val secLabel = stringResource(R.string.pw_offer_seconds)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clearAndSetSemantics { contentDescription = "$endsIn $minutes $minLabel $seconds $secLabel" },
    ) {
        Text(
            if (expired) stringResource(R.string.pw_offer_expired) else endsIn,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            TimeBox(minutes, minLabel)
            Text(
                ":",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            TimeBox(seconds, secLabel)
        }
    }
}

@Composable
private fun TimeBox(value: String, label: String) {
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(20.dp))
            .padding(horizontal = 18.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            value,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSecondaryContainer)
    }
}

@Preview(name = "Limited offer · light", showBackground = true)
@Preview(name = "Limited offer · dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LimitedOfferPreview() {
    PreviewTheme {
        LimitedOfferContent(
            plan = PreviewData.plans[1].copy(price = "$14.99"),
            originalPrice = "$29.99",
            discountLabel = "-50%",
            offerEndsAtMillis = System.currentTimeMillis() + 9 * 60_000L,
            offerDurationMillis = 15 * 60_000L,
            legal = PreviewData.legal,
            onPurchase = {},
            onRestore = {},
            onClose = {},
            title = "Limited-time offer",
            subtitle = "A special price, just for you.",
        )
    }
}
