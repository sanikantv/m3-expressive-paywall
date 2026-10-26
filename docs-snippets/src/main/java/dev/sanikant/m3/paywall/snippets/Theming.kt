@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.sanikant.m3.paywall.snippets

import android.os.Build
import androidx.annotation.FontRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.expressiveLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight

// region theme
@Composable
fun AppTheme(typography: Typography = Typography(), content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    val context = LocalContext.current
    val colors = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        dark -> darkColorScheme()
        else -> expressiveLightColorScheme()
    }
    MaterialExpressiveTheme(
        colorScheme = colors,
        motionScheme = MotionScheme.expressive(), // MotionScheme.standard() for calmer springs
        typography = typography,
        content = content,
    )
}
// endregion

// region font
/** A variable font (e.g. Google Sans Flex in res/font) as a FontFamily, one instance per weight. */
fun variableFontFamily(@FontRes font: Int): FontFamily = FontFamily(
    listOf(400, 500, 600, 700, 800, 900).map { weight ->
        Font(font, FontWeight(weight), variationSettings = FontVariation.Settings(FontVariation.weight(weight)))
    },
)

// Then: AppTheme(typography = Typography(variableFontFamily(R.font.google_sans_flex))) { ... }
// endregion
