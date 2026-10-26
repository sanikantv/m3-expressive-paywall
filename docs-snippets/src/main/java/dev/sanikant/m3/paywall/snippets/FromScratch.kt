package dev.sanikant.m3.paywall.snippets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// region scratch-model
data class Plan(val id: String, val title: String, val price: String, val period: String, val trial: String? = null)
// endregion

// region scratch-paywall
@Composable
fun SimplePaywall(
    plans: List<Plan>,
    onPurchase: (Plan) -> Unit,
    onRestore: () -> Unit,
    termsUrl: String,
    privacyUrl: String,
) {
    var selected by rememberSaveable { mutableStateOf(plans.first().id) }
    val plan = plans.first { it.id == selected }
    val uri = LocalUriHandler.current

    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Go Pro", style = MaterialTheme.typography.headlineLarge)

        // Plans: a radio group, so TalkBack announces "1 of 2, selected".
        Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            plans.forEach { p ->
                OutlinedCard(
                    Modifier
                        .fillMaxWidth()
                        .selectable(selected = p.id == selected, role = Role.RadioButton) { selected = p.id },
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = p.id == selected, onClick = null)
                        Text(p.title, Modifier.padding(start = 12.dp).weight(1f))
                        Text("${p.price} ${p.period}")
                    }
                }
            }
        }

        Button(onClick = { onPurchase(plan) }, Modifier.fillMaxWidth()) {
            Text(if (plan.trial != null) "Start free trial" else "Continue")
        }

        // Google Play: price, period, trial terms and how to cancel, next to the button.
        val terms = plan.trial?.let { "$it, then " }.orEmpty()
        Text(
            "$terms${plan.price} ${plan.period}. Renews automatically. Cancel anytime in Google Play.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextButton(onClick = onRestore) { Text("Restore") }
            TextButton(onClick = { uri.openUri(termsUrl) }) { Text("Terms") }
            TextButton(onClick = { uri.openUri(privacyUrl) }) { Text("Privacy") }
        }
        Spacer(Modifier.padding(4.dp))
    }
}
// endregion
