<p align="center">
  <img src="art/icon/icon-rounded-512.png" alt="M3 Expressive Paywall icon" width="96" />
</p>

<h1 align="center">M3 Expressive Paywall</h1>

<p align="center"><b>6 ready-to-use Material 3 Expressive paywalls for Jetpack Compose. Bring your own billing.</b></p>

<p align="center">
  <a href="https://github.com/sanikantv/m3-expressive-paywall/actions/workflows/build.yml"><img src="https://github.com/sanikantv/m3-expressive-paywall/actions/workflows/build.yml/badge.svg" alt="Build" /></a>
  <a href="https://jitpack.io/#sanikantv/m3-expressive-paywall"><img src="https://jitpack.io/v/sanikantv/m3-expressive-paywall.svg" alt="JitPack" /></a>
  <a href="https://github.com/sanikantv/m3-expressive-paywall/releases/latest"><img src="https://img.shields.io/github/v/release/sanikantv/m3-expressive-paywall?label=APK" alt="Latest release" /></a>
  <img src="https://img.shields.io/badge/material3-1.5.0--beta02-6750A4" alt="material3 1.5.0-beta02" />
  <img src="https://img.shields.io/badge/minSdk-26-3DDC84" alt="minSdk 26" />
  <a href="LICENSE"><img src="https://img.shields.io/github/license/sanikantv/m3-expressive-paywall" alt="MIT license" /></a>
</p>

<p align="center">
  <img src="art/hero.gif" alt="Demo: plan picker morphing between plans, predictive back, limited-time offer sheet" width="300" />
</p>

Copy-paste paywall screens built with the newest **Material 3 Expressive** APIs: `ButtonGroup`,
`ToggleButton`, `MaterialShapes` with shape morphing, `LinearWavyProgressIndicator`,
`MotionScheme.expressive()` springs and the emphasized type scale. Every screen works in light
and dark mode, follows your app's `MaterialTheme` (dynamic color included) and shows everything
Google Play asks for on a subscription screen.

**No billing SDK inside.** Each paywall takes a list of plans and three callbacks. Connect it to
Google Play Billing, RevenueCat, Adapty, Qonversion or your own backend.

## Try it

Download the demo APK from [Releases](https://github.com/sanikantv/m3-expressive-paywall/releases/latest)
and install it on any Android 8.0+ device. The demo uses fake prices and never charges anything.

## The 6 designs

| 1. Classic plans | 2. Free vs Pro | 3. Trial timeline |
|:---:|:---:|:---:|
| <img src="art/screenshots/01-classic-light.png" width="240" alt="Classic plans paywall, light" /> | <img src="art/screenshots/02-comparison-light.png" width="240" alt="Free vs Pro comparison paywall, light" /> | <img src="art/screenshots/03-trial-light.png" width="240" alt="Trial timeline paywall, light" /> |
| Hero, plan cards with a morphing radio, a "Best value" badge and features. | Comparison grid with a highlighted Pro column and check and cross icons. | "Today, Day 5 reminder, Day 7 billing" steps that build trust. |

| 4. Plan picker | 5. Limited-time offer | 6. Onboarding end |
|:---:|:---:|:---:|
| <img src="art/screenshots/04-picker-light.png" width="240" alt="Plan picker paywall with button group, light" /> | <img src="art/screenshots/05-offer-light.png" width="240" alt="Limited-time offer bottom sheet, light" /> | <img src="art/screenshots/06-onboarding-light.png" width="240" alt="Onboarding carousel paywall, light" /> |
| Connected `ButtonGroup`; the spotlight card morphs and the price rolls. | Discount sheet with a live countdown and a wavy progress bar. | Benefits carousel that ends in the plan selection. |

<details>
<summary><b>Dark mode</b></summary>

| Classic | Free vs Pro | Trial | Plan picker | Offer | Onboarding |
|:---:|:---:|:---:|:---:|:---:|:---:|
| <img src="art/screenshots/01-classic-dark.png" width="130" alt="Classic, dark" /> | <img src="art/screenshots/02-comparison-dark.png" width="130" alt="Free vs Pro, dark" /> | <img src="art/screenshots/03-trial-dark.png" width="130" alt="Trial timeline, dark" /> | <img src="art/screenshots/04-picker-dark.png" width="130" alt="Plan picker, dark" /> | <img src="art/screenshots/05-offer-dark.png" width="130" alt="Limited-time offer, dark" /> | <img src="art/screenshots/06-onboarding-dark.png" width="130" alt="Onboarding, dark" /> |

</details>

<details>
<summary><b>Demo gallery</b></summary>

| Light | Dark |
|:---:|:---:|
| <img src="art/screenshots/00-gallery-light.png" width="240" alt="Demo gallery, light" /> | <img src="art/screenshots/00-gallery-dark.png" width="240" alt="Demo gallery, dark" /> |

</details>

## Google Play compliance built in

The templates follow Google Play's subscription rules. Many paywall samples skip these items;
here a shared footer puts them on every screen, so you can't forget one:

- [x] **Price** of the selected plan, from your store data
- [x] **Billing period** ("per month", "per year", "one-time")
- [x] **Trial terms**: "7-day free trial, then $29.99 per year"
- [x] **Auto-renew disclosure**: "Renews automatically until canceled. Cancel anytime in Google Play."
- [x] **Restore** button
- [x] **Terms** and **Privacy** links

One-time plans (`isSubscription = false`) get a "no subscription, no renewals" line instead.
Read Google's [subscription policy](https://support.google.com/googleplay/android-developer/answer/9900533)
before you ship. You are responsible for the final copy and prices in your app.

## Setup

**1. Add JitPack** in `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

**2. Add the dependency** in `gradle/libs.versions.toml`:

```toml
[versions]
m3Paywall = "1.0.0"

[libraries]
m3-paywall = { module = "com.github.sanikantv:paywall", version.ref = "m3Paywall" }
```

```kotlin
dependencies {
    implementation(libs.m3.paywall)
}
```

The library brings `androidx.compose.material3:material3:1.5.0-beta02` with it, which needs
`compileSdk = 37`.

**Prefer copy-paste?** Copy any file from
[`paywall/src/main/java/dev/sanikant/m3/paywall/`](paywall/src/main/java/dev/sanikant/m3/paywall/)
plus the `internal/` folder and `res/values/strings.xml` into your app.

## Usage

```kotlin
ClassicPlansPaywall(
    plans = listOf(
        PaywallPlan(id = "monthly", title = "Monthly", price = "$4.99", period = "per month"),
        PaywallPlan(
            id = "yearly", title = "Yearly", price = "$29.99", period = "per year",
            trialText = "7-day free trial", badge = "Best value",
        ),
        PaywallPlan(id = "lifetime", title = "Lifetime", price = "$79.99", period = "one-time", isSubscription = false),
    ),
    features = listOf(
        PaywallFeature("Unlimited notes", "No caps on anything", PaywallIcons.Sparkle),
        PaywallFeature("Zero ads", icon = PaywallIcons.Block),
    ),
    legal = PaywallLegal(termsUrl = "https://your.app/terms", privacyUrl = "https://your.app/privacy"),
    onPurchase = { plan -> billing.buy(plan.id) },
    onRestore = { billing.restore() },
    onClose = { navController.popBackStack() },
)
```

All six share the same contract:

| Composable | Extra inputs | Expressive APIs used |
|---|---|---|
| `ClassicPlansPaywall` | `features` | `MaterialShapes`, `Morph`, `MotionScheme` |
| `ComparisonPaywall` | `rows: List<ComparisonRow>` | Emphasized type, morphing plan tiles |
| `TrialTimelinePaywall` | `steps` (defaults from the trial plan) | `MaterialShapes` timeline nodes |
| `PlanPickerPaywall` | `features` | `ButtonGroupDefaults` + `ToggleButton`, `Morph` |
| `LimitedOfferSheet` | `plan`, `originalPrice`, `discountLabel`, `offerEndsAtMillis` | `LinearWavyProgressIndicator`, `ModalBottomSheet` |
| `OnboardingPaywall` | `pages: List<BenefitPage>` | `HorizontalPager`, emphasized type |

Every string has a default in `strings.xml` (prefixed `pw_`) and can be overridden through
parameters such as `title` and `subtitle`, so the paywalls are ready to translate.

## Connect your billing

The library only needs formatted strings, so mapping is a few lines. Two sketches:

<details>
<summary><b>Google Play Billing</b></summary>

```kotlin
// productDetails comes from billingClient.queryProductDetails(...)
val plans = productDetails.subscriptionOfferDetails.orEmpty().map { offer ->
    val phases = offer.pricingPhases.pricingPhaseList
    val base = phases.last()
    PaywallPlan(
        id = offer.offerToken,
        title = if (base.billingPeriod == "P1Y") "Yearly" else "Monthly",
        price = base.formattedPrice,
        period = if (base.billingPeriod == "P1Y") "per year" else "per month",
        trialText = phases.firstOrNull { it.priceAmountMicros == 0L }?.let { "Free trial" },
    )
}

onPurchase = { plan ->
    val params = BillingFlowParams.ProductDetailsParams.newBuilder()
        .setProductDetails(productDetails)
        .setOfferToken(plan.id)
        .build()
    billingClient.launchBillingFlow(activity, BillingFlowParams.newBuilder().setProductDetailsParamsList(listOf(params)).build())
}
```

</details>

<details>
<summary><b>RevenueCat</b></summary>

```kotlin
// Keep your RevenueCat API key in local.properties and read it through BuildConfig. Never commit it.
Purchases.sharedInstance.getOfferingsWith { offerings ->
    val packages = offerings.current?.availablePackages.orEmpty()
    plans = packages.map { pkg ->
        PaywallPlan(
            id = pkg.identifier,
            title = pkg.packageType.name.lowercase().replaceFirstChar { it.uppercase() },
            price = pkg.product.price.formatted,
            period = pkg.product.period?.let { "per ${it.unit.name.lowercase()}" } ?: "one-time",
            isSubscription = pkg.product.period != null,
        )
    }
}

onPurchase = { plan ->
    val pkg = packages.first { it.identifier == plan.id }
    Purchases.sharedInstance.purchaseWith(PurchaseParams.Builder(activity, pkg).build()) { _, _ -> /* unlock */ }
}
onRestore = { Purchases.sharedInstance.restorePurchasesWith { /* refresh entitlements */ } }
```

</details>

Real prices must always come from the store. Never hard-code them.

## For AI coding agents

This repo is written to be easy for Claude Code, Cursor, Codex and Copilot to learn from:

- [`llms.txt`](llms.txt): a short map of the project.
- [`docs/PAYWALL_CATALOG.md`](docs/PAYWALL_CATALOG.md): when to use each design, the exact
  signature, the Expressive APIs it uses, and ready-to-paste prompts.

Example prompt: *"Add the trial timeline paywall from github.com/sanikantv/m3-expressive-paywall
to my app and connect `onPurchase` to my existing BillingRepository."*

## Tech stack and versions

| | Version |
|---|---|
| `androidx.compose.material3` | **1.5.0-beta02** (pinned) |
| Compose BOM | 2026.02.01 |
| Kotlin | 2.2.10 |
| Android Gradle Plugin | 9.2.1 |
| compileSdk / targetSdk / minSdk | 37 / 36 / 26 |
| `androidx.graphics:graphics-shapes` | 1.1.0 |

Material 3 Expressive APIs are marked `@ExperimentalMaterial3ExpressiveApi` and can still change
before `1.5.0` stable. If something does not compile, check your material3 version first.

## Project layout

```
paywall/   the library (published on JitPack): 6 public composables + internal/ shared parts
app/       the demo: gallery, settings sheet (theme, dynamic color, motion), fake data
art/       icon, screenshots, hero GIF, social preview
scripts/   capture.sh (screenshots), record_hero.sh (GIF), make_icon.py + render_icon.sh
docs/      PAYWALL_CATALOG.md for humans and AI agents
```

Rebuild the screenshots with `ANDROID_SERIAL=emulator-5554 ./scripts/capture.sh`.

## Related

Part of the [awesome-m3-expressive](https://github.com/sanikantv/awesome-m3-expressive) collection
of Material 3 Expressive components and full-screen templates.

## Built by

**Sanikant**, Android developer shipping apps on Google Play and the App Store.

- Telegram: [@Techno_Sanikant](https://t.me/Techno_Sanikant), where new templates are posted first
- YouTube: [@Sanikant_Kushwaha](https://www.youtube.com/@Sanikant_Kushwaha)
- Website: [sanikantkushwaha.com](https://sanikantkushwaha.com/)

Design patterns were researched on Mobbin. No screenshots or assets from other apps are included.
Icons are Material icons (Apache 2.0).

## License

[MIT](LICENSE) © 2026 Sanikant
