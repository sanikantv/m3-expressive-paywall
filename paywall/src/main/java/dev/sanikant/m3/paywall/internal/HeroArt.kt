@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.sanikant.m3.paywall.internal

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Hero illustration built only from MaterialShapes, so it themes itself and needs no image
 * assets. Layers rotate slowly in opposite directions.
 */
@Composable
internal fun HeroArt(icon: ImageVector, modifier: Modifier = Modifier, size: Dp = 196.dp) {
    val colors = MaterialTheme.colorScheme
    val transition = rememberInfiniteTransition(label = "hero")
    val spin by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 24_000, easing = LinearEasing)),
        label = "spin",
    )
    val breathe by transition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 2_400), RepeatMode.Reverse),
        label = "breathe",
    )

    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        ShapeLayer(MaterialShapes.Cookie12Sided.toShape(), colors.primaryContainer, size, rotation = spin)
        ShapeLayer(MaterialShapes.Sunny.toShape(), colors.tertiaryContainer, size * 0.72f, rotation = -spin * 1.5f)
        Box(
            Modifier
                .size(size * 0.48f)
                .graphicsLayer {
                    scaleX = breathe
                    scaleY = breathe
                }
                .background(colors.primary, MaterialShapes.Cookie4Sided.toShape()),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = colors.onPrimary, modifier = Modifier.size(size * 0.22f))
        }
        // A small satellite adds depth without stealing focus.
        ShapeLayer(
            MaterialShapes.Flower.toShape(),
            colors.tertiary,
            size * 0.14f,
            rotation = spin * 2,
            modifier = Modifier.align(Alignment.TopEnd).offset(x = (-6).dp, y = 14.dp),
        )
    }
}

@Composable
private fun ShapeLayer(shape: Shape, color: Color, size: Dp, rotation: Float, modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(size)
            .graphicsLayer { rotationZ = rotation }
            .background(color, shape),
    )
}
