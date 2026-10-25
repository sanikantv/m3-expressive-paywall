@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.sanikant.m3.paywall

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.sanikant.m3.paywall.internal.ComplianceFooter
import dev.sanikant.m3.paywall.internal.ExpressiveCta
import dev.sanikant.m3.paywall.internal.PaywallHeader
import dev.sanikant.m3.paywall.internal.PaywallScaffold
import dev.sanikant.m3.paywall.internal.PlanTile
import dev.sanikant.m3.paywall.internal.PreviewData
import dev.sanikant.m3.paywall.internal.PreviewTheme
import dev.sanikant.m3.paywall.internal.ctaLabel
import dev.sanikant.m3.paywall.internal.initialPlan

private val ValueColumnWidth: Dp = 76.dp

/**
 * Design 2, Free vs Pro table: a comparison grid with a highlighted Pro column, then
 * side-by-side plan tiles. Works best with two or three plans.
 *
 * @param rows table rows. Set `freeText` or `proText` to show a value instead of an icon.
 * @see ClassicPlansPaywall for the shared parameters.
 */
@Composable
public fun ComparisonPaywall(
    plans: List<PaywallPlan>,
    rows: List<ComparisonRow>,
    legal: PaywallLegal,
    onPurchase: (PaywallPlan) -> Unit,
    onRestore: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.pw_comparison_title),
    subtitle: String? = stringResource(R.string.pw_comparison_subtitle),
    freeLabel: String = stringResource(R.string.pw_comparison_free),
    proLabel: String = stringResource(R.string.pw_comparison_pro),
    initialPlanId: String? = null,
) {
    var selectedId by rememberSaveable { mutableStateOf(plans.initialPlan(initialPlanId).id) }
    val selected = plans.firstOrNull { it.id == selectedId } ?: plans.first()

    PaywallScaffold(
        onClose = onClose,
        modifier = modifier,
        backdrop = MaterialTheme.colorScheme.tertiaryContainer,
        bottomBar = {
            ExpressiveCta(ctaLabel(selected), onClick = { onPurchase(selected) })
            ComplianceFooter(selected, legal, onRestore)
        },
    ) {
        PaywallHeader(title, subtitle)
        Spacer(Modifier.height(20.dp))
        ComparisonTable(rows, freeLabel, proLabel)
        Spacer(Modifier.height(20.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            plans.forEach { plan ->
                PlanTile(
                    plan = plan,
                    selected = plan.id == selected.id,
                    onSelect = { selectedId = plan.id },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun ComparisonTable(rows: List<ComparisonRow>, freeLabel: String, proLabel: String) {
    val colors = MaterialTheme.colorScheme
    Surface(
        color = colors.surfaceContainerLow,
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(Modifier.padding(vertical = 8.dp)) {
            // The Pro column is one tall pill drawn behind every row.
            Box(
                Modifier
                    .matchParentSize()
                    .padding(end = 8.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Box(
                    Modifier
                        .width(ValueColumnWidth)
                        .fillMaxHeight()
                        .background(colors.primaryContainer, RoundedCornerShape(24.dp)),
                )
            }
            Column(Modifier.fillMaxWidth()) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        stringResource(R.string.pw_comparison_feature),
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                    )
                    HeaderCell(freeLabel, colors.onSurfaceVariant)
                    HeaderCell(proLabel, colors.primary, bold = true)
                }
                rows.forEachIndexed { index, row ->
                    if (index > 0) {
                        HorizontalDivider(
                            color = colors.outlineVariant.copy(alpha = 0.5f),
                            modifier = Modifier.padding(start = 20.dp, end = ValueColumnWidth + 16.dp),
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .padding(start = 20.dp, end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            row.label,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colors.onSurface,
                            modifier = Modifier.weight(1f).padding(vertical = 6.dp),
                        )
                        ValueCell(row.free, row.freeText, highlighted = false)
                        ValueCell(row.pro, row.proText, highlighted = true)
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderCell(text: String, color: androidx.compose.ui.graphics.Color, bold: Boolean = false) {
    Text(
        text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = if (bold) FontWeight.ExtraBold else FontWeight.Medium,
        color = color,
        textAlign = TextAlign.Center,
        modifier = Modifier.width(ValueColumnWidth),
    )
}

@Composable
private fun ValueCell(included: Boolean, text: String?, highlighted: Boolean) {
    val colors = MaterialTheme.colorScheme
    val description = stringResource(if (included) R.string.pw_comparison_included else R.string.pw_comparison_not_included)
    Box(Modifier.width(ValueColumnWidth), contentAlignment = Alignment.Center) {
        when {
            text != null -> Text(
                text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (highlighted) FontWeight.Bold else FontWeight.Normal,
                color = if (highlighted) colors.onPrimaryContainer else colors.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            included && highlighted -> Box(
                Modifier
                    .size(26.dp)
                    .background(colors.primary, CircleShape)
                    .semantics { contentDescription = description },
                contentAlignment = Alignment.Center,
            ) {
                Icon(PaywallIcons.Check, null, tint = colors.onPrimary, modifier = Modifier.size(16.dp))
            }
            included -> Icon(PaywallIcons.Check, description, tint = colors.onSurfaceVariant, modifier = Modifier.size(22.dp))
            else -> Icon(PaywallIcons.Close, description, tint = colors.outline, modifier = Modifier.size(20.dp))
        }
    }
}

@Preview(name = "Comparison · light", showSystemUi = true)
@Preview(name = "Comparison · dark", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ComparisonPaywallPreview() {
    PreviewTheme {
        ComparisonPaywall(PreviewData.plans.take(2), PreviewData.rows, PreviewData.legal, {}, {}, {})
    }
}
