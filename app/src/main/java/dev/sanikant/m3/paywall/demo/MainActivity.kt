package dev.sanikant.m3.paywall.demo

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import dev.sanikant.m3.paywall.demo.ui.theme.DemoSettings
import dev.sanikant.m3.paywall.demo.ui.theme.M3ExpressivePaywallTheme
import dev.sanikant.m3.paywall.demo.ui.theme.ThemeMode
import dev.sanikant.m3.paywall.demo.ui.theme.isDark

/**
 * Optional intent extras, used by `scripts/capture.sh` for screenshots:
 * - `screen`: classic, comparison, trial, picker, offer or onboarding
 * - `dynamic`: false to use the baseline purple scheme instead of wallpaper colors
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialDesign = PaywallDesign.fromKey(intent.getStringExtra("screen"))
        val initialDynamic = intent.getBooleanExtra("dynamic", true)

        setContent {
            var settings by rememberSaveable(stateSaver = DemoSettingsSaver) {
                mutableStateOf(DemoSettings(dynamicColor = initialDynamic))
            }
            val dark = settings.isDark()
            DisposableEffect(dark) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { dark },
                    navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { dark },
                )
                onDispose {}
            }
            M3ExpressivePaywallTheme(settings) {
                DemoApp(settings, onSettingsChange = { settings = it }, initialDesign = initialDesign)
            }
        }
    }
}

private val DemoSettingsSaver = Saver<DemoSettings, List<Any>>(
    save = { listOf(it.themeMode.ordinal, it.dynamicColor, it.expressiveMotion) },
    restore = { DemoSettings(ThemeMode.entries[it[0] as Int], it[1] as Boolean, it[2] as Boolean) },
)
