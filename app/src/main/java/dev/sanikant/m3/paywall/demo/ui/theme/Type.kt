package dev.sanikant.m3.paywall.demo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import dev.sanikant.m3.paywall.demo.R

/**
 * Google Sans Flex (SIL OFL 1.1, see licenses/GoogleSansFlex-OFL.txt), bundled as the original
 * variable font. Each weight is an instance of the `wght` axis, so one file covers them all.
 */
val GoogleSansFlex = FontFamily(
    listOf(100, 200, 300, 400, 500, 600, 700, 800, 900).map { weight ->
        Font(
            resId = R.font.google_sans_flex,
            weight = FontWeight(weight),
            variationSettings = FontVariation.Settings(FontVariation.weight(weight)),
        )
    },
)

/**
 * The Material 3 type scale, including the Expressive emphasized styles, set in Google Sans Flex.
 * The paywalls read `MaterialTheme.typography`, so they pick this up with no extra code.
 */
val AppTypography = Typography(GoogleSansFlex)
