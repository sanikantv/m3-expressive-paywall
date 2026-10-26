# Library Information

Single source of truth for https://paywall.sanikant.dev. Every fact below was checked against
this repository on 2026-10-26.

## Library
Name: M3 Expressive Paywall
Tagline: 6 ready-to-use Material 3 Expressive paywalls for Jetpack Compose. Bring your own billing.
Repository: https://github.com/sanikantv/m3-expressive-paywall
Latest version: v1.0.0 (tag `v1.0.0`, released 2026-10-25)
License: MIT
Author: Sanikant (sanikant.dev)

## Install
Repository setup: `maven("https://jitpack.io")` in `dependencyResolutionManagement.repositories`
Dependency line: `implementation("com.github.sanikantv:m3-expressive-paywall:v1.0.0")`
Requirements: minSdk 26 / compileSdk 37 / material3 1.5.0-beta02 (transitive, `api`) / Kotlin 2.2.10 (built with) / Compose BOM 2026.02.01 / AGP 9.2.1

## Positioning
What it does: Six full paywall screens (Compose) that take `List<PaywallPlan>` + `onPurchase` / `onRestore` / `onClose`, with the price, period, trial and auto-renew disclosure built from the selected plan.
What material3 does NOT provide that this library does: complete paywall screens; plan selection with radio semantics; disclosure text generation; trial timeline; offer countdown sheet; working examples of ButtonGroup/ToggleButton, MaterialShapes + Morph and LinearWavyProgressIndicator together.
Target developers: Android developers using Jetpack Compose who sell subscriptions or one-time unlocks, with Play Billing, RevenueCat or another backend.
Primary use cases: upgrade screen, end of onboarding, free-trial start, win-back/launch discount.

## Public API
| Component / function | Purpose | Key parameters | Docs page |
|---|---|---|---|
| `ClassicPlansPaywall` | Hero + plan cards + features | plans, features, legal, callbacks, title, subtitle, initialPlanId, heroIcon | /components/classic-plans-paywall/ |
| `ComparisonPaywall` | Free vs Pro table + plan tiles | plans, rows, freeLabel, proLabel | /components/comparison-paywall/ |
| `TrialTimelinePaywall` | Today / reminder / billing timeline | plans, steps | /components/trial-timeline-paywall/ |
| `defaultTrialSteps` | Default timeline steps | plan, trialDays = 7, reminderDay = trialDays - 2 | /components/trial-timeline-paywall/ |
| `PlanPickerPaywall` | Connected toggle group + morphing card | plans, features | /components/plan-picker-paywall/ |
| `LimitedOfferSheet` | Discount bottom sheet, countdown, wavy progress | plan, originalPrice, discountLabel, offerEndsAtMillis, offerDurationMillis, sheetState | /components/limited-offer-sheet/ |
| `OnboardingPaywall` | Benefits pager ending in plans | pages, plans, plansTitle, pagerState | /components/onboarding-paywall/ |
| `PaywallPlan`, `PaywallFeature`, `PaywallLegal`, `ComparisonRow`, `TrialStep`, `BenefitPage` | Data classes | see page | /components/paywall-plan/ |
| `PaywallIcons` | 16 bundled Material icons | — | /components/paywall-plan/#paywallicons |

Known limitation: `LimitedOfferSheet` exposes material3 `SheetState` (`@ExperimentalMaterial3Api`), so callers need `@OptIn(ExperimentalMaterial3Api::class)`.

## Demo Media
- `art/hero.mp4`, `art/hero.gif`: plan picker morph, predictive back, offer sheet (17 s) → `site/public/media/hero-demo.{mp4,webm,gif}`, poster `hero-poster.webp`
- `art/screenshots/0[0-6]-*-{light,dark}.png` (1080×2400, emulator) → `site/public/media/screens/*.webp` (540×1200)
- `art/icon/*`: app icon → `site/public/favicon.svg`
- `art/social-preview.png`: GitHub social card; site OG cards in `site/public/og/`

## SEO Research
Data: Google autocomplete + web-search check on 2026-10-26. No paid volume data.

### Primary Keywords
| Keyword | Intent | Priority | Page |
|---|---|---|---|
| jetpack compose paywall | code/library | 1 | / |
| material 3 expressive paywall | Expressive example | 1 | / |
| compose subscription screen | build UI | 2 | / |

### Secondary Keywords
revenuecat jetpack compose · google play billing jetpack compose · free trial paywall · paywall template (vendor-dominated) · revenuecat paywall alternative

### Long-Tail / How-To Keywords
how to build a paywall in jetpack compose · jetpack compose countdown timer · linear wavy progress indicator · material shapes compose · compose shape morph · material 3 expressive shapes compose · jetpack compose connected button group · jetpack compose toggle button group · jetpack compose bottom sheet material 3 · jetpack compose onboarding screen

### Question Keywords
does material 3 expressive work with stable material3 · how to use material 3 expressive in compose · what does google play require on a subscription screen · how to restore purchases google play billing

### Alternative / Comparison Keywords
revenuecat paywall alternative · superwall alternative open source · adapty paywall templates · revenuecat paywall templates

### Rejected
android paywall, paywall android, how to make paywall android (bypass intent)

### Page Mapping
| Keyword | URL | Intent | CTA |
|---|---|---|---|
| jetpack compose paywall | / | library | install line, Get started |
| how to build a paywall in jetpack compose | /guides/how-to-build-a-paywall-jetpack-compose/ | tutorial | install |
| google play subscription requirements | /guides/google-play-subscription-paywall-requirements/ | checklist | install |
| google play billing jetpack compose | /guides/connect-paywall-google-play-billing/ | integration | install |
| revenuecat jetpack compose | /guides/connect-paywall-revenuecat/ | integration | install |
| free trial paywall | /guides/free-trial-paywall-jetpack-compose/ | design | install |
| jetpack compose countdown timer | /guides/countdown-timer-jetpack-compose/ | snippet | install |
| material shapes compose / shape morph | /guides/shape-morphing-material-shapes-compose/ | snippet | install |
| jetpack compose connected button group | /guides/connected-button-group-jetpack-compose/ | snippet | install |
| revenuecat paywall alternative | /comparison/ | evaluation | GitHub |

Full competitor notes and rankings: site/seo-report.md.
