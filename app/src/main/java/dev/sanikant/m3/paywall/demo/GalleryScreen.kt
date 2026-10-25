@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package dev.sanikant.m3.paywall.demo

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.sanikant.m3.paywall.PaywallIcons
import kotlinx.coroutines.delay

/** Cards spring in once per launch, not every time the user comes back to the gallery. */
private var entered = false

private val DesignShapes = listOf(
    MaterialShapes.Cookie9Sided,
    MaterialShapes.Clover4Leaf,
    MaterialShapes.Sunny,
    MaterialShapes.Cookie6Sided,
    MaterialShapes.SoftBurst,
    MaterialShapes.Flower,
)

@Composable
fun GalleryScreen(
    onOpen: (PaywallDesign) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    appBarState: TopAppBarState = rememberTopAppBarState(),
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(appBarState)
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeFlexibleTopAppBar(
                title = { Text("Paywalls", fontWeight = FontWeight.SemiBold) },
                subtitle = { Text("Material 3 Expressive · Compose") },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(DemoIcons.Settings, contentDescription = "Settings")
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = padding.calculateTopPadding() + 4.dp,
                bottom = padding.calculateBottomPadding() + 24.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item { HeroCard(Modifier.widthIn(max = 640.dp)) }
            item {
                Text(
                    "Templates",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .widthIn(max = 640.dp)
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 12.dp, bottom = 4.dp),
                )
            }
            itemsIndexed(PaywallDesign.entries, key = { _, design -> design.key }) { index, design ->
                DesignCard(
                    design = design,
                    index = index,
                    onClick = { onOpen(design) },
                    modifier = Modifier.widthIn(max = 640.dp),
                )
            }
            item {
                Text(
                    "Fake prices · No billing code · MIT licensed",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun HeroCard(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val spin by rememberInfiniteTransition(label = "heroCard").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(30_000, easing = LinearEasing)),
        label = "heroSpin",
    )
    Surface(
        color = colors.primaryContainer,
        shape = RoundedCornerShape(36.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Box(Modifier.height(196.dp)) {
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 48.dp, y = (-40).dp)
                    .size(200.dp)
                    .graphicsLayer { rotationZ = spin }
                    .background(colors.primary.copy(alpha = 0.18f), MaterialShapes.Cookie12Sided.toShape()),
            )
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-24).dp, y = 24.dp)
                    .size(64.dp)
                    .graphicsLayer { rotationZ = -spin * 2 }
                    .background(colors.tertiary, MaterialShapes.Sunny.toShape()),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    PaywallIcons.Sparkle,
                    contentDescription = null,
                    tint = colors.onTertiary,
                    modifier = Modifier.size(32.dp).graphicsLayer { rotationZ = spin * 2 },
                )
            }
            Column(Modifier.padding(24.dp)) {
                Text(
                    "6 Expressive\npaywalls",
                    style = MaterialTheme.typography.headlineMediumEmphasized,
                    color = colors.onPrimaryContainer,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Bring your own billing.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.onPrimaryContainer.copy(alpha = 0.8f),
                )
                Spacer(Modifier.weight(1f))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Pill("Light & dark", colors.surface.copy(alpha = 0.7f), colors.onSurface)
                    Pill("Dynamic color", colors.surface.copy(alpha = 0.7f), colors.onSurface)
                    Pill("Play-compliant", colors.surface.copy(alpha = 0.7f), colors.onSurface)
                }
            }
        }
    }
}

@Composable
private fun DesignCard(
    design: PaywallDesign,
    index: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val motion = MaterialTheme.motionScheme
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f, motion.fastSpatialSpec(), label = "cardScale")
    val corner by animateDpAsState(if (pressed) 40.dp else 28.dp, motion.fastSpatialSpec(), label = "cardCorner")

    // Staggered entrance: each card springs up a little after the one above.
    val enter = remember { Animatable(if (entered) 1f else 0f) }
    LaunchedEffect(Unit) {
        if (enter.value < 1f) {
            delay(60L * index)
            enter.animateTo(1f, motion.slowSpatialSpec())
        }
        entered = true
    }

    val (container, onContainer) = when (index % 3) {
        0 -> colors.primaryContainer to colors.onPrimaryContainer
        1 -> colors.secondaryContainer to colors.onSecondaryContainer
        else -> colors.tertiaryContainer to colors.onTertiaryContainer
    }

    Surface(
        color = colors.surfaceContainerLow,
        shape = RoundedCornerShape(corner),
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = enter.value.coerceIn(0f, 1f)
                translationY = (1f - enter.value) * 48.dp.toPx()
            }
            .clickable(interactionSource = interaction, indication = null, role = Role.Button, onClick = onClick),
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(64.dp)
                    .background(container, DesignShapes[index % DesignShapes.size].toShape()),
                contentAlignment = Alignment.Center,
            ) {
                Icon(design.icon, contentDescription = null, tint = onContainer, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "%02d".format(index + 1),
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.primary,
                    fontWeight = FontWeight.Bold,
                )
                Text(design.title, style = MaterialTheme.typography.titleLarge, color = colors.onSurface)
                Text(design.description, style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    design.apis.forEach { Pill(it, colors.surfaceContainerHighest, colors.onSurfaceVariant) }
                }
            }
        }
    }
}

@Composable
private fun Pill(text: String, container: Color, content: Color) {
    Text(
        text,
        style = MaterialTheme.typography.labelMedium,
        color = content,
        modifier = Modifier
            .background(container, CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    )
}
