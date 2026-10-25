@file:OptIn(ExperimentalMaterial3Api::class)

package dev.sanikant.m3.paywall.demo

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.sanikant.m3.paywall.ClassicPlansPaywall
import dev.sanikant.m3.paywall.ComparisonPaywall
import dev.sanikant.m3.paywall.LimitedOfferSheet
import dev.sanikant.m3.paywall.OnboardingPaywall
import dev.sanikant.m3.paywall.PaywallPlan
import dev.sanikant.m3.paywall.PlanPickerPaywall
import dev.sanikant.m3.paywall.TrialTimelinePaywall
import dev.sanikant.m3.paywall.demo.ui.theme.DemoSettings
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

/**
 * Demo shell: the gallery, one paywall at a time on top of it, and a snackbar that stands in
 * for the billing flow. Navigation is plain state so the demo has no extra dependencies.
 */
@Composable
fun DemoApp(
    settings: DemoSettings,
    onSettingsChange: (DemoSettings) -> Unit,
    initialDesign: PaywallDesign? = null,
) {
    var current by rememberSaveable { mutableStateOf(initialDesign) }
    var showSettings by rememberSaveable { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val backProgress = remember { Animatable(0f) }
    // Hoisted so the gallery keeps its scroll position while a paywall is open.
    val galleryList = rememberLazyListState()
    val galleryAppBar = rememberTopAppBarState()

    val fullScreen = current?.takeIf { it != PaywallDesign.Offer }

    fun close() {
        current = null
    }

    fun purchase(plan: PaywallPlan) {
        close()
        scope.launch { snackbar.showSnackbar("Demo: start billing for ${plan.title} (${plan.price} ${plan.period})") }
    }

    fun restore() {
        scope.launch { snackbar.showSnackbar("Demo: restore purchases") }
    }

    // Predictive back: the paywall shrinks and rounds its corners as the user swipes.
    PredictiveBackHandler(enabled = fullScreen != null) { events ->
        try {
            events.collect { backProgress.snapTo(it.progress) }
            close()
        } catch (e: CancellationException) {
            backProgress.animateTo(0f)
            throw e
        }
    }

    Box(Modifier.fillMaxSize()) {
        val motion = MaterialTheme.motionScheme
        AnimatedContent(
            targetState = fullScreen,
            transitionSpec = {
                if (targetState != null) {
                    (fadeIn(motion.defaultEffectsSpec()) + scaleIn(motion.defaultSpatialSpec(), initialScale = 0.92f))
                        .togetherWith(fadeOut(motion.fastEffectsSpec()))
                } else {
                    fadeIn(motion.defaultEffectsSpec())
                        .togetherWith(fadeOut(motion.fastEffectsSpec()) + scaleOut(motion.defaultSpatialSpec(), targetScale = 0.92f))
                }
            },
            label = "screen",
        ) { design ->
            if (design == null) {
                GalleryScreen(
                    onOpen = {
                        scope.launch { backProgress.snapTo(0f) }
                        current = it
                    },
                    onOpenSettings = { showSettings = true },
                    listState = galleryList,
                    appBarState = galleryAppBar,
                )
            } else {
                val progress = backProgress.value
                Box(
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            val scale = 1f - 0.1f * progress
                            scaleX = scale
                            scaleY = scale
                        }
                        .clip(RoundedCornerShape((32 * progress).dp)),
                ) {
                    PaywallFor(design, ::purchase, ::restore, ::close)
                }
            }
        }

        if (current == PaywallDesign.Offer) {
            val endsAt = remember { System.currentTimeMillis() + 12 * 60_000L + 34_000L }
            LimitedOfferSheet(
                plan = DemoData.offerPlan,
                originalPrice = DemoData.yearly.price,
                discountLabel = "-50%",
                offerEndsAtMillis = endsAt,
                legal = DemoData.legal,
                onPurchase = ::purchase,
                onRestore = ::restore,
                onClose = ::close,
            )
        }

        if (showSettings) {
            SettingsSheet(settings, onSettingsChange, onDismiss = { showSettings = false })
        }

        SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun PaywallFor(
    design: PaywallDesign,
    onPurchase: (PaywallPlan) -> Unit,
    onRestore: () -> Unit,
    onClose: () -> Unit,
) {
    when (design) {
        PaywallDesign.Classic -> ClassicPlansPaywall(
            plans = DemoData.plans,
            features = DemoData.features,
            legal = DemoData.legal,
            onPurchase = onPurchase,
            onRestore = onRestore,
            onClose = onClose,
            title = "Unlock Lumen Pro",
        )
        PaywallDesign.Comparison -> ComparisonPaywall(
            plans = listOf(DemoData.monthly, DemoData.yearly),
            rows = DemoData.comparison,
            legal = DemoData.legal,
            onPurchase = onPurchase,
            onRestore = onRestore,
            onClose = onClose,
        )
        PaywallDesign.Trial -> TrialTimelinePaywall(
            plans = listOf(DemoData.yearly, DemoData.monthly),
            legal = DemoData.legal,
            onPurchase = onPurchase,
            onRestore = onRestore,
            onClose = onClose,
        )
        PaywallDesign.Picker -> PlanPickerPaywall(
            plans = DemoData.plans,
            features = DemoData.features.take(3),
            legal = DemoData.legal,
            onPurchase = onPurchase,
            onRestore = onRestore,
            onClose = onClose,
        )
        PaywallDesign.Onboarding -> OnboardingPaywall(
            pages = DemoData.onboardingPages,
            plans = DemoData.plans,
            legal = DemoData.legal,
            onPurchase = onPurchase,
            onRestore = onRestore,
            onClose = onClose,
        )
        PaywallDesign.Offer -> Unit
    }
}
