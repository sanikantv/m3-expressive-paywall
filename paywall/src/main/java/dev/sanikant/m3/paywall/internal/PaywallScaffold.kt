@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.sanikant.m3.paywall.internal

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.sanikant.m3.paywall.PaywallIcons
import dev.sanikant.m3.paywall.R

/**
 * Shared frame for every full-screen paywall: a soft tinted backdrop, a scrolling body,
 * a close button that stays on top, and a sticky bottom bar for the button and legal footer.
 */
@Composable
internal fun PaywallScaffold(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    backdrop: Color = MaterialTheme.colorScheme.primaryContainer,
    bottomBar: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    val scroll = rememberScrollState()
    val barElevation by animateDpAsState(
        targetValue = if (scroll.canScrollForward) 3.dp else 0.dp,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "barElevation",
    )
    val barColor by animateColorAsState(
        targetValue = if (scroll.canScrollForward) colors.surfaceContainerLow else colors.surface,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "barColor",
    )

    Box(
        modifier
            .fillMaxSize()
            .background(colors.surface)
            .background(
                Brush.verticalGradient(
                    0f to backdrop.copy(alpha = 0.55f),
                    0.45f to colors.surface.copy(alpha = 0f),
                ),
            ),
    ) {
        Column(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scroll),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                Spacer(Modifier.height(56.dp))
                Column(
                    Modifier
                        .widthIn(max = 560.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = content,
                )
                Spacer(Modifier.height(24.dp))
            }
            Surface(
                color = barColor,
                shadowElevation = barElevation,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    Modifier
                        .navigationBarsPadding()
                        .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = bottomBar,
                )
            }
        }
        CloseButton(
            onClose = onClose,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 12.dp, top = 8.dp),
        )
    }
}

@Composable
internal fun CloseButton(onClose: () -> Unit, modifier: Modifier = Modifier) {
    FilledTonalIconButton(
        onClick = onClose,
        modifier = modifier,
        shape = CircleShape,
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.85f),
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
    ) {
        Icon(PaywallIcons.Close, contentDescription = stringResource(R.string.pw_close))
    }
}
