# Paywall catalog

Full docs with screenshots, guides and compiled examples: https://paywall.sanikant.dev

A reference for developers and AI coding agents. Each entry says when to use the design, its
signature, which Material 3 Expressive APIs it uses, and a prompt you can paste into an agent.

Package: `dev.sanikant.m3.paywall` · material3 `1.5.0-beta02` · minSdk 26

## Shared contract

```kotlin
data class PaywallPlan(
    val id: String,              // your product / offer / package id, returned in onPurchase
    val title: String,           // "Yearly"
    val price: String,           // formatted store price, "$29.99"
    val period: String,          // "per year", "per month", "one-time"
    val trialText: String? = null,   // "7-day free trial"
    val badge: String? = null,       // "Best value"
    val priceDetail: String? = null, // "$2.49/month, billed yearly"
    val isSubscription: Boolean = true,
)
data class PaywallLegal(val termsUrl: String, val privacyUrl: String)

// Every paywall:
onPurchase: (PaywallPlan) -> Unit   // start your billing flow
onRestore: () -> Unit               // restore purchases
onClose: () -> Unit                 // dismiss
```

Rules every design follows:

- Colors come only from `MaterialTheme.colorScheme`, so dynamic color and dark mode work.
- Motion comes from `MaterialTheme.motionScheme`, so `MotionScheme.expressive()` gives springy
  animations and `MotionScheme.standard()` calmer ones.
- The bottom bar always shows the CTA plus `ComplianceFooter`: disclosure sentence built from the
  selected plan, then Restore · Terms · Privacy.
- The preselected plan is `initialPlanId`, else the first plan with a `badge`, else the first plan.
- Plan cards use `Modifier.selectable(role = Role.RadioButton)` for TalkBack.

---

## 1. ClassicPlansPaywall

**Use when** you sell two or three plans (monthly, yearly, lifetime) and want a familiar layout.

```kotlin
@Composable
fun ClassicPlansPaywall(
    plans: List<PaywallPlan>,
    features: List<PaywallFeature>,
    legal: PaywallLegal,
    onPurchase: (PaywallPlan) -> Unit,
    onRestore: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Unlock everything",
    subtitle: String? = "One upgrade. Every feature. No ads, ever.",
    initialPlanId: String? = null,
    heroIcon: ImageVector = PaywallIcons.Sparkle,
)
```

**Expressive APIs:** `MaterialShapes` (Cookie12Sided, Sunny, Cookie4Sided, Flower) in the hero;
`Morph` between `Circle` and `Cookie9Sided` for the selection radio; `ButtonDefaults.shapes()` for
the squishy CTA; spring corner radius through `motionScheme.fastSpatialSpec()`.

**Prompt:** *"Using ClassicPlansPaywall from m3-expressive-paywall, add a paywall screen to my
Compose navigation graph. Map my BillingRepository's products to PaywallPlan, mark the yearly
plan with badge = "Best value", and pop the back stack in onClose."*

## 2. ComparisonPaywall

**Use when** the value is easiest to explain as "what Free has vs what Pro adds".

```kotlin
@Composable
fun ComparisonPaywall(
    plans: List<PaywallPlan>,          // 2 or 3 fit side by side
    rows: List<ComparisonRow>,         // ComparisonRow(label, free, pro = true, freeText, proText)
    legal: PaywallLegal,
    onPurchase: (PaywallPlan) -> Unit,
    onRestore: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Free vs Pro",
    subtitle: String? = "See exactly what you get when you upgrade.",
    freeLabel: String = "Free",
    proLabel: String = "Pro",
    initialPlanId: String? = null,
)
```

**Expressive APIs:** emphasized headline type; a tall `primaryContainer` pill behind the Pro
column; `PlanTile` with morphing radio. Text values (`freeText = "50"`, `proText = "Unlimited"`)
replace icons.

**Prompt:** *"Add ComparisonPaywall with rows for my free limits (50 notes, no sync, ads) vs Pro.
Use the monthly and yearly plans only."*

## 3. TrialTimelinePaywall

**Use when** you offer a free trial. Showing when the reminder and the charge happen raises
trial starts and lowers refund requests.

```kotlin
@Composable
fun TrialTimelinePaywall(
    plans: List<PaywallPlan>,
    legal: PaywallLegal,
    onPurchase: (PaywallPlan) -> Unit,
    onRestore: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "How your free trial works",
    subtitle: String? = "No surprises. We remind you before you pay.",
    steps: List<TrialStep> = defaultTrialSteps(trialPlan),  // Today, Day 5, Day 7
    initialPlanId: String? = null,
)

@Composable
fun defaultTrialSteps(plan: PaywallPlan, trialDays: Int = 7, reminderDay: Int = trialDays - 2): List<TrialStep>
```

**Expressive APIs:** timeline nodes shaped with `MaterialShapes` (Cookie4Sided, Cookie6Sided,
Sunny); gradient rail; the timeline fades through `motionScheme.defaultEffectsSpec()` when a plan
without a trial is selected. Remember to actually send the reminder you promise.

**Prompt:** *"Add TrialTimelinePaywall for my 14-day trial: pass defaultTrialSteps(plan,
trialDays = 14, reminderDay = 12) and schedule a WorkManager notification on day 12."*

## 4. PlanPickerPaywall

**Use when** you want the most "Expressive" look, with a strong interaction and few plans.

```kotlin
@Composable
fun PlanPickerPaywall(
    plans: List<PaywallPlan>,          // 2 to 4, short titles
    features: List<PaywallFeature>,
    legal: PaywallLegal,
    onPurchase: (PaywallPlan) -> Unit,
    onRestore: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Choose your plan",
    subtitle: String? = "Switch anytime. Your progress stays with you.",
    initialPlanId: String? = null,
)
```

**Expressive APIs:** connected button group (`ToggleButton` with
`ButtonGroupDefaults.connectedLeading/Middle/TrailingButtonShapes()` and
`ButtonGroupDefaults.ConnectedSpaceBetween`); spotlight card morphs between `MaterialShapes`
via `Morph` + `motionScheme.slowSpatialSpec()`; price rolls with `AnimatedContent`.

**Prompt:** *"Replace my current upgrade screen with PlanPickerPaywall and keep my existing
features list."*

## 5. LimitedOfferSheet

**Use when** you show a win-back or launch discount over any screen.

```kotlin
@Composable
fun LimitedOfferSheet(
    plan: PaywallPlan,                 // price = offer price
    originalPrice: String,             // shown struck through
    discountLabel: String,             // "-50%"
    offerEndsAtMillis: Long,           // System.currentTimeMillis() based
    legal: PaywallLegal,
    onPurchase: (PaywallPlan) -> Unit,
    onRestore: () -> Unit,
    onClose: () -> Unit,               // also called on swipe-down
    modifier: Modifier = Modifier,
    title: String = "Limited-time offer",
    subtitle: String = "A special price, just for you.",
    offerDurationMillis: Long = 15 * 60_000L,
    sheetState: SheetState = rememberBottomSheetState(SheetValue.Hidden, setOf(SheetValue.Hidden, SheetValue.Expanded)),
)
```

**Expressive APIs:** `LinearWavyProgressIndicator` draining with the remaining time;
`MaterialShapes.SoftBurst` behind the discount; `ModalBottomSheet`. The CTA turns into
"Offer ended" at zero. Put "First year only, then $X per year" in `priceDetail` for intro prices.
Only run real deadlines: fake countdowns break consumer protection rules.

**Prompt:** *"Show LimitedOfferSheet when a lapsed subscriber opens the app, with the offer end
time stored in DataStore so it survives restarts."*

## 6. OnboardingPaywall

**Use when** the paywall is the last step of onboarding.

```kotlin
@Composable
fun OnboardingPaywall(
    pages: List<BenefitPage>,          // BenefitPage(title, description, icon), 3 or 4 work best
    plans: List<PaywallPlan>,
    legal: PaywallLegal,
    onPurchase: (PaywallPlan) -> Unit,
    onRestore: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    plansTitle: String = "Ready to go Pro?",
    plansSubtitle: String? = "Pick a plan to keep everything you just saw.",
    initialPlanId: String? = null,
    pagerState: PagerState = rememberPagerState { pages.size + 1 },
)
```

**Expressive APIs:** `HorizontalPager` with scale and alpha by page offset; pages illustrated
with `MaterialShapes`; stretching page indicator; emphasized display type. The legal footer
appears on the plan page, where buying becomes possible.

**Prompt:** *"Use OnboardingPaywall as the final onboarding step; when onClose is called, mark
onboarding complete and go to Home."*

---

## Theming

Wrap your app in `MaterialExpressiveTheme` to get the expressive motion and type. The paywalls
read `MaterialTheme.typography`, so your font applies automatically. The demo uses Google Sans Flex:

```kotlin
MaterialExpressiveTheme(
    colorScheme = if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context),
    motionScheme = MotionScheme.expressive(),
    typography = Typography(GoogleSansFlex), // FontFamily built from res/font/google_sans_flex.ttf
) { AppContent() }
```

The demo's [Theme.kt](../app/src/main/java/dev/sanikant/m3/paywall/demo/ui/theme/Theme.kt) shows
the full version with a fallback scheme for Android 11 and below.
