package dev.sanikant.m3.paywall.demo

import androidx.compose.ui.graphics.vector.ImageVector
import dev.sanikant.m3.paywall.PaywallIcons

/** The six designs shown in the gallery. [key] is used by the `screen` intent extra. */
enum class PaywallDesign(
    val key: String,
    val title: String,
    val description: String,
    val apis: List<String>,
    val icon: ImageVector,
) {
    Classic(
        key = "classic",
        title = "Classic plans",
        description = "Hero, features and monthly, yearly and lifetime cards.",
        apis = listOf("MaterialShapes", "Shape morph"),
        icon = PaywallIcons.Star,
    ),
    Comparison(
        key = "comparison",
        title = "Free vs Pro",
        description = "A comparison grid with a highlighted Pro column.",
        apis = listOf("Expressive type", "Plan tiles"),
        icon = PaywallIcons.Check,
    ),
    Trial(
        key = "trial",
        title = "Trial timeline",
        description = "Today, reminder and billing steps that build trust.",
        apis = listOf("MaterialShapes", "Motion scheme"),
        icon = PaywallIcons.Calendar,
    ),
    Picker(
        key = "picker",
        title = "Plan picker",
        description = "A connected button group with a morphing spotlight card.",
        apis = listOf("ButtonGroup", "Morph"),
        icon = PaywallIcons.Bolt,
    ),
    Offer(
        key = "offer",
        title = "Limited-time offer",
        description = "A discount sheet with a countdown and wavy progress.",
        apis = listOf("Wavy progress", "Bottom sheet"),
        icon = PaywallIcons.Clock,
    ),
    Onboarding(
        key = "onboarding",
        title = "Onboarding end",
        description = "A benefits carousel that ends in plan selection.",
        apis = listOf("Pager", "Expressive type"),
        icon = PaywallIcons.Heart,
    ),
    ;

    companion object {
        fun fromKey(key: String?): PaywallDesign? = entries.firstOrNull { it.key == key }
    }
}
