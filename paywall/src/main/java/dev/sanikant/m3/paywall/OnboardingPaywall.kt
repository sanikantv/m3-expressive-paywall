@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.sanikant.m3.paywall

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.graphics.shapes.RoundedPolygon
import dev.sanikant.m3.paywall.internal.CloseButton
import dev.sanikant.m3.paywall.internal.ComplianceFooter
import dev.sanikant.m3.paywall.internal.ExpressiveCta
import dev.sanikant.m3.paywall.internal.PaywallHeader
import dev.sanikant.m3.paywall.internal.PlanCard
import dev.sanikant.m3.paywall.internal.PreviewData
import dev.sanikant.m3.paywall.internal.PreviewTheme
import dev.sanikant.m3.paywall.internal.ctaLabel
import dev.sanikant.m3.paywall.internal.initialPlan
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

private val PageShapes = listOf(
    MaterialShapes.Cookie9Sided,
    MaterialShapes.Flower,
    MaterialShapes.Sunny,
    MaterialShapes.Clover8Leaf,
    MaterialShapes.Cookie12Sided,
)

/**
 * Design 6, onboarding-end paywall: a short carousel of benefit pages that ends in the plan
 * selection. Use it as the last step of onboarding. The legal footer appears on the plan page,
 * where purchase becomes possible.
 *
 * @param pages benefit pages shown before the plans. Three or four work best.
 * @see ClassicPlansPaywall for the shared parameters.
 */
@Composable
public fun OnboardingPaywall(
    pages: List<BenefitPage>,
    plans: List<PaywallPlan>,
    legal: PaywallLegal,
    onPurchase: (PaywallPlan) -> Unit,
    onRestore: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    plansTitle: String = stringResource(R.string.pw_onboarding_plans_title),
    plansSubtitle: String? = stringResource(R.string.pw_onboarding_plans_subtitle),
    initialPlanId: String? = null,
    pagerState: PagerState = rememberPagerState { pages.size + 1 },
) {
    var selectedId by rememberSaveable { mutableStateOf(plans.initialPlan(initialPlanId).id) }
    val selected = plans.firstOrNull { it.id == selectedId } ?: plans.first()
    val scope = rememberCoroutineScope()
    val colors = MaterialTheme.colorScheme
    val motion = MaterialTheme.motionScheme
    val onPlans = pagerState.currentPage == pages.size

    Box(
        modifier
            .fillMaxSize()
            .background(colors.surface)
            .background(Brush.verticalGradient(0f to colors.primaryContainer.copy(alpha = 0.5f), 0.5f to colors.surface.copy(alpha = 0f))),
    ) {
        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CloseButton(onClose)
                Spacer(Modifier.weight(1f))
                if (!onPlans) {
                    TextButton(onClick = { scope.launch { pagerState.animateScrollToPage(pages.size) } }) {
                        Text(stringResource(R.string.pw_onboarding_skip))
                    }
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f).fillMaxWidth(),
            ) { page ->
                // Read the scroll offset only inside draw-phase lambdas so swiping doesn't recompose.
                val offset = { (pagerState.currentPage - page + pagerState.currentPageOffsetFraction).absoluteValue.coerceIn(0f, 1f) }
                Box(
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = lerp(1f, 0.4f, offset())
                            scaleX = lerp(1f, 0.9f, offset())
                            scaleY = lerp(1f, 0.9f, offset())
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    if (page < pages.size) {
                        BenefitPageContent(pages[page], PageShapes[page % PageShapes.size], spin = { offset() * 90f })
                    } else {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            PaywallHeader(plansTitle, plansSubtitle)
                            Spacer(Modifier.height(24.dp))
                            Column(Modifier.widthIn(max = 560.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                plans.forEach { plan ->
                                    PlanCard(plan, selected = plan.id == selected.id, onSelect = { selectedId = plan.id })
                                }
                            }
                        }
                    }
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PageIndicator(pageCount = pages.size + 1, current = pagerState.currentPage)
                Spacer(Modifier.height(16.dp))
                AnimatedContent(
                    targetState = onPlans,
                    transitionSpec = {
                        fadeIn(motion.defaultEffectsSpec()).togetherWith(fadeOut(motion.fastEffectsSpec()))
                    },
                    label = "onboardingBottom",
                ) { showPlans ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (showPlans) {
                            ExpressiveCta(ctaLabel(selected), onClick = { onPurchase(selected) })
                            ComplianceFooter(selected, legal, onRestore)
                        } else {
                            FilledTonalButton(
                                onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                                shapes = ButtonDefaults.shapes(),
                                modifier = Modifier.fillMaxWidth().height(ButtonDefaults.MediumContainerHeight),
                            ) {
                                Text(
                                    stringResource(R.string.pw_onboarding_next),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                            Spacer(Modifier.height(64.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BenefitPageContent(page: BenefitPage, shape: RoundedPolygon, spin: () -> Float) {
    val colors = MaterialTheme.colorScheme
    Column(
        Modifier.padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                Modifier
                    .size(248.dp)
                    .graphicsLayer { rotationZ = spin() }
                    .background(colors.primaryContainer, shape.toShape()),
            )
            Box(
                Modifier
                    .size(128.dp)
                    .graphicsLayer { rotationZ = -spin() * 2 }
                    .background(colors.primary, MaterialShapes.Cookie4Sided.toShape()),
                contentAlignment = Alignment.Center,
            ) {
                Icon(page.icon, contentDescription = null, tint = colors.onPrimary, modifier = Modifier.size(56.dp).graphicsLayer { rotationZ = spin() * 2 })
            }
        }
        Spacer(Modifier.height(40.dp))
        Text(
            page.title,
            style = MaterialTheme.typography.displaySmallEmphasized,
            color = colors.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(12.dp))
        Text(page.description, style = MaterialTheme.typography.bodyLarge, color = colors.onSurfaceVariant, textAlign = TextAlign.Center)
    }
}

/** Dots that stretch into a pill for the current page. */
@Composable
private fun PageIndicator(pageCount: Int, current: Int) {
    val label = stringResource(R.string.pw_onboarding_page, current + 1, pageCount)
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.semantics { contentDescription = label },
    ) {
        repeat(pageCount) { index ->
            val active = index == current
            val width by animateDpAsState(if (active) 32.dp else 10.dp, MaterialTheme.motionScheme.fastSpatialSpec(), label = "dotWidth")
            val color by animateColorAsState(
                if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                MaterialTheme.motionScheme.defaultEffectsSpec(),
                label = "dotColor",
            )
            Box(Modifier.size(width = width, height = 10.dp).background(color, CircleShape))
        }
    }
}

@Preview(name = "Onboarding · light", showSystemUi = true)
@Preview(name = "Onboarding · dark", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OnboardingPaywallPreview() {
    PreviewTheme {
        OnboardingPaywall(PreviewData.pages, PreviewData.plans, PreviewData.legal, {}, {}, {})
    }
}
