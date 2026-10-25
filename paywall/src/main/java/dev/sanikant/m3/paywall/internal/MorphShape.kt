@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.sanikant.m3.paywall.internal

import androidx.compose.animation.core.Animatable
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toPath
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon

/** A [Shape] that draws [morph] at [progress]. MaterialShapes polygons are normalized to 0..1. */
internal class MorphShape(
    private val morph: Morph,
    private val progress: Float,
) : Shape {
    private val matrix = Matrix()
    private val path = Path()

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        path.rewind()
        morph.toPath(progress = progress, path = path)
        matrix.reset()
        matrix.scale(size.width, size.height)
        path.transform(matrix)
        return Outline.Generic(path)
    }
}

/**
 * Returns a shape that springs from the previously shown polygon to [target] every time
 * [target] changes. Used for the selection radio and the plan picker spotlight.
 */
@Composable
internal fun rememberMorphingShape(target: RoundedPolygon): Shape {
    var from by remember { mutableStateOf(target) }
    var to by remember { mutableStateOf(target) }
    val progress = remember { Animatable(1f) }
    val spec = MaterialTheme.motionScheme.slowSpatialSpec<Float>()

    LaunchedEffect(target) {
        if (target !== to) {
            from = to
            to = target
            progress.snapTo(0f)
            progress.animateTo(1f, spec)
        }
    }
    val morph = remember(from, to) { Morph(from, to) }
    return MorphShape(morph, progress.value)
}

/**
 * Shapes used across the paywalls, kept in one place so designs stay consistent. Held as
 * single instances because [rememberMorphingShape] restarts its morph when the target changes.
 */
internal object PaywallShapes {
    val Unselected: RoundedPolygon by lazy { MaterialShapes.Circle }
    val Selected: RoundedPolygon by lazy { MaterialShapes.Cookie9Sided }
    val Spotlight: List<RoundedPolygon> by lazy {
        listOf(
            MaterialShapes.Cookie4Sided,
            MaterialShapes.Cookie9Sided,
            MaterialShapes.Sunny,
            MaterialShapes.Flower,
            MaterialShapes.Clover8Leaf,
        )
    }
}
