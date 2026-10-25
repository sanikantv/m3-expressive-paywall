@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.sanikant.m3.paywall.demo.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.expressiveLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

enum class ThemeMode { System, Light, Dark }

/** Demo settings the user can change from the gallery's settings sheet. */
data class DemoSettings(
    val themeMode: ThemeMode = ThemeMode.System,
    val dynamicColor: Boolean = true,
    val expressiveMotion: Boolean = true,
)

val dynamicColorAvailable: Boolean get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@Composable
fun DemoSettings.isDark(): Boolean = when (themeMode) {
    ThemeMode.System -> isSystemInDarkTheme()
    ThemeMode.Light -> false
    ThemeMode.Dark -> true
}

/**
 * Material 3 Expressive theme: wallpaper colors on Android 12+, the baseline purple scheme
 * otherwise, Google Sans Flex type and the spring-based expressive motion scheme.
 */
@Composable
fun M3ExpressivePaywallTheme(
    settings: DemoSettings = DemoSettings(),
    content: @Composable () -> Unit,
) {
    val dark = settings.isDark()
    val colorScheme = when {
        settings.dynamicColor && dynamicColorAvailable -> {
            val context = LocalContext.current
            if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        dark -> darkColorScheme()
        else -> expressiveLightColorScheme()
    }

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        motionScheme = if (settings.expressiveMotion) MotionScheme.expressive() else MotionScheme.standard(),
        typography = AppTypography,
        content = content,
    )
}
