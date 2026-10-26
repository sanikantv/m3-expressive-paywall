@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.sanikant.m3.paywall.snippets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

// region countdown-state
/** Milliseconds left until [endsAtMillis], updated once per second, never below zero. */
@Composable
fun rememberRemainingMillis(endsAtMillis: Long): State<Long> =
    produceState(initialValue = (endsAtMillis - System.currentTimeMillis()).coerceAtLeast(0L), endsAtMillis) {
        while (value > 0L) {
            val now = System.currentTimeMillis()
            value = (endsAtMillis - now).coerceAtLeast(0L)
            delay(1_000 - now % 1_000) // tick on the second boundary
        }
    }
// endregion

// region countdown-ui
@Composable
fun OfferCountdown(endsAtMillis: Long, totalMillis: Long) {
    val remaining by rememberRemainingMillis(endsAtMillis)
    val seconds = remaining / 1_000
    val fraction by animateFloatAsState(
        targetValue = (remaining.toFloat() / totalMillis).coerceIn(0f, 1f),
        animationSpec = tween(1_000, easing = LinearEasing), // glide between ticks
        label = "offerProgress",
    )
    Column {
        Text(
            "%02d:%02d".format(seconds / 60, seconds % 60),
            style = MaterialTheme.typography.displaySmall,
        )
        LinearWavyProgressIndicator(progress = { fraction }, modifier = Modifier.fillMaxWidth())
    }
}
// endregion
