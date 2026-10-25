@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package dev.sanikant.m3.paywall.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import dev.sanikant.m3.paywall.demo.ui.theme.DemoSettings
import dev.sanikant.m3.paywall.demo.ui.theme.ThemeMode
import dev.sanikant.m3.paywall.demo.ui.theme.dynamicColorAvailable

@Composable
fun SettingsSheet(
    settings: DemoSettings,
    onChange: (DemoSettings) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberBottomSheetState(SheetValue.Hidden, setOf(SheetValue.Hidden, SheetValue.Expanded)),
    ) {
        Column(Modifier.padding(horizontal = 24.dp).padding(bottom = 24.dp)) {
            Text("Appearance", style = MaterialTheme.typography.headlineSmallEmphasized)
            Spacer(Modifier.height(20.dp))
            SectionLabel("Theme")
            ConnectedChoice(
                options = ThemeMode.entries.map { it.name },
                selected = settings.themeMode.ordinal,
                onSelect = { onChange(settings.copy(themeMode = ThemeMode.entries[it])) },
            )
            Spacer(Modifier.height(20.dp))
            SectionLabel("Motion")
            ConnectedChoice(
                options = listOf("Expressive", "Standard"),
                selected = if (settings.expressiveMotion) 0 else 1,
                onSelect = { onChange(settings.copy(expressiveMotion = it == 0)) },
            )
            Spacer(Modifier.height(20.dp))
            Surface(shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surfaceContainerHigh) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .toggleable(
                            value = settings.dynamicColor && dynamicColorAvailable,
                            enabled = dynamicColorAvailable,
                            role = Role.Switch,
                            onValueChange = { onChange(settings.copy(dynamicColor = it)) },
                        )
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Dynamic color", style = MaterialTheme.typography.titleMedium)
                        Text(
                            if (dynamicColorAvailable) "Use colors from your wallpaper" else "Needs Android 12 or newer",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(
                        checked = settings.dynamicColor && dynamicColorAvailable,
                        onCheckedChange = null,
                        enabled = dynamicColorAvailable,
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            Text(
                "Paywalls follow your app's MaterialTheme, so these switches show how they adapt.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
    )
}

@Composable
private fun ConnectedChoice(options: List<String>, selected: Int, onSelect: (Int) -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        options.forEachIndexed { index, label ->
            ToggleButton(
                checked = index == selected,
                onCheckedChange = { onSelect(index) },
                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                },
                modifier = Modifier
                    .weight(1f)
                    .semantics { role = Role.RadioButton },
            ) {
                Text(label)
            }
        }
    }
}
