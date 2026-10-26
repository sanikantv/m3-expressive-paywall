# SEO report: paywall.sanikant.dev

Built 2026-10-26. Research details and raw autocomplete data: [../library.md](../library.md).

## Method and limits

- Keywords come from Google autocomplete (firefox and chrome clients) for 30+ seed queries,
  plus a web-search check of what ranks for about 10 candidates.
- **No paid search-volume data was used.** Keywords are prioritized by intent, fit with
  the library and visible competition, not by measured volume. Re-check in Google Search
  Console after 4–8 weeks of impressions.
- "android paywall" and "paywall android" were rejected: every suggestion is about bypassing
  paywalls (consumer intent).

## Primary keywords (home page)

| Keyword | Intent | Why |
|---|---|---|
| jetpack compose paywall | Find code or a library | Exact intent. Only vendor SDK docs rank (RevenueCat, Superwall, Adapty); no neutral open-source answer. Low volume (no autocomplete). |
| material 3 expressive paywall | Find an Expressive example | The library's unique angle. Rides the growing "material 3 expressive …" autocomplete family. |
| compose subscription screen | Build a subscription UI | Common phrasing for the same need. |

## Secondary and long-tail keywords → page

| Keyword | Page | Competition |
|---|---|---|
| how to build a paywall in jetpack compose | /guides/how-to-build-a-paywall-jetpack-compose/ | Low–medium (vendor blogs) |
| google play subscription requirements / google play billing policy paywall | /guides/google-play-subscription-paywall-requirements/ | Low for the developer angle |
| google play billing jetpack compose | /guides/connect-paywall-google-play-billing/ | Low–medium |
| revenuecat jetpack compose, revenuecat paywall alternative | /guides/connect-paywall-revenuecat/, /comparison/ | Medium (vendor marketing) |
| free trial paywall | /guides/free-trial-paywall-jetpack-compose/ | Medium |
| jetpack compose countdown timer, linear wavy progress indicator | /guides/countdown-timer-jetpack-compose/ | Medium (old tutorials) / low |
| material shapes compose, compose shape morph, material 3 expressive shapes compose | /guides/shape-morphing-material-shapes-compose/ | Low (API pages only) |
| jetpack compose connected button group, toggle button group | /guides/connected-button-group-jetpack-compose/ | Low (API pages only) |
| compose paywall plan cards, best value badge | /components/classic-plans-paywall/ | Low |
| free vs pro comparison table compose | /components/comparison-paywall/ | Low |
| jetpack compose bottom sheet material 3 offer | /components/limited-offer-sheet/ | Medium |
| jetpack compose onboarding screen paywall | /components/onboarding-paywall/ | Medium |
| m3 expressive paywall install / jitpack | /getting-started/ | None (branded) |

## Titles and descriptions (from the built pages)

| URL | Title | Description |
|---|---|---|
| `/changelog/` | Changelog | M3 Expressive Paywall | Release history of M3 Expressive Paywall, the Material 3 Expressive paywall templates for Jetpack Compose. |
| `/comparison/` | M3 Expressive Paywall vs RevenueCat Paywalls and other options | M3 Expressive Paywall | An honest comparison of M3 Expressive Paywall with building a Compose paywall yourself, RevenueCat Paywalls, Superwall, Adapty and Qonversion, and when each one is the better choice. |
| `/components/classic-plans-paywall/` | Classic plans paywall for Jetpack Compose | M3 Expressive Paywall | ClassicPlansPaywall: a Jetpack Compose paywall with a Material 3 Expressive hero, monthly, yearly and lifetime plan cards, a Best value badge and a features list. |
| `/components/comparison-paywall/` | Free vs Pro comparison paywall for Jetpack Compose | M3 Expressive Paywall | ComparisonPaywall: a Jetpack Compose subscription screen with a Free vs Pro feature table, a highlighted Pro column, check and cross icons and side-by-side plan tiles. |
| `/components/` | Jetpack Compose paywall designs | M3 Expressive Paywall | All six M3 Expressive Paywall designs for Jetpack Compose - classic plans, Free vs Pro table, trial timeline, plan picker, limited-time offer and onboarding - with screenshots. |
| `/components/limited-offer-sheet/` | Limited-time offer bottom sheet for Jetpack Compose | M3 Expressive Paywall | LimitedOfferSheet: a Material 3 modal bottom sheet paywall with a discount badge, struck-through price, live countdown and LinearWavyProgressIndicator for Jetpack Compose. |
| `/components/onboarding-paywall/` | Onboarding paywall carousel for Jetpack Compose | M3 Expressive Paywall | OnboardingPaywall: a Jetpack Compose onboarding carousel of benefit pages that ends in Material 3 Expressive plan selection, with skip, page indicator and Play disclosures. |
| `/components/paywall-plan/` | PaywallPlan, PaywallFeature and other paywall models | M3 Expressive Paywall | The data classes every M3 Expressive Paywall screen takes - PaywallPlan, PaywallFeature, PaywallLegal, ComparisonRow, TrialStep, BenefitPage - plus the bundled PaywallIcons. |
| `/components/plan-picker-paywall/` | Plan picker paywall with a button group for Jetpack Compose | M3 Expressive Paywall | PlanPickerPaywall: a Material 3 Expressive paywall where a connected ButtonGroup of ToggleButtons switches plans and the spotlight card morphs its shape and rolls the price. |
| `/components/trial-timeline-paywall/` | Free trial timeline paywall for Jetpack Compose | M3 Expressive Paywall | TrialTimelinePaywall: a Jetpack Compose free trial paywall that shows Today, a reminder day and the billing day as a timeline, with plan cards and Play disclosures. |
| `/faq/` | M3 Expressive Paywall FAQ | M3 Expressive Paywall | Answers about M3 Expressive Paywall - material3 version, minimum SDK, dark mode and dynamic color, billing providers, Compose Multiplatform, animation speed and license. |
| `/getting-started/` | Getting started | M3 Expressive Paywall | Add M3 Expressive Paywall to a Jetpack Compose app with JitPack, set compileSdk 37 and show your first Material 3 Expressive paywall in a few lines. |
| `/guides/connect-paywall-google-play-billing/` | Connect a Compose paywall to Google Play Billing | M3 Expressive Paywall | Use Google Play Billing Library 9 with a Jetpack Compose paywall - query ProductDetails, map subscription offers to plans, launch the billing flow and restore purchases. |
| `/guides/connect-paywall-revenuecat/` | Connect a Compose paywall to RevenueCat | M3 Expressive Paywall | Use RevenueCat Offerings and Packages with your own Jetpack Compose paywall UI - map packages to plans, purchase, restore and check entitlements with purchases-android 10. |
| `/guides/connected-button-group-jetpack-compose/` | Connected button group in Jetpack Compose | M3 Expressive Paywall | Build a Material 3 Expressive connected button group in Jetpack Compose with ToggleButton and ButtonGroupDefaults connected shapes, for single-choice options like plan selection. |
| `/guides/countdown-timer-jetpack-compose/` | Countdown timer in Jetpack Compose | M3 Expressive Paywall | Make a countdown timer in Jetpack Compose with produceState that ticks on the second, survives recomposition and drives a Material 3 LinearWavyProgressIndicator. |
| `/guides/free-trial-paywall-jetpack-compose/` | Free trial paywall in Jetpack Compose | M3 Expressive Paywall | Build a free trial paywall in Jetpack Compose that shows what happens today, when the reminder comes and when billing starts, with the trial terms Google Play requires. |
| `/guides/google-play-subscription-paywall-requirements/` | Google Play subscription paywall requirements | M3 Expressive Paywall | What Google Play's subscription policy requires on an Android paywall - price, billing period, trial terms, auto-renewal and cancellation - as a checklist with Jetpack Compose examples. |
| `/guides/how-to-build-a-paywall-jetpack-compose/` | How to build a paywall in Jetpack Compose | M3 Expressive Paywall | Build a subscription paywall screen in Jetpack Compose step by step - plan selection, purchase button and the Google Play disclosures - with plain material3 or a ready-made template. |
| `/guides/` | Jetpack Compose paywall guides | M3 Expressive Paywall | Step-by-step Jetpack Compose guides for subscription paywalls - Google Play rules, Play Billing, RevenueCat, free trials, countdowns, shape morphing and button groups. |
| `/guides/shape-morphing-material-shapes-compose/` | Shape morphing with MaterialShapes in Jetpack Compose | M3 Expressive Paywall | Use Material 3 Expressive MaterialShapes in Jetpack Compose as backgrounds and clips, and animate between them with Morph from androidx.graphics.shapes and the motion scheme. |
| `/` | Material 3 Expressive paywalls for Jetpack Compose | M3 Expressive Paywall | Six ready-to-use Jetpack Compose paywall screens built with Material 3 Expressive. Google Play subscription disclosures built in, light and dark, works with Play Billing or RevenueCat. |

## Technical SEO in place

- Canonical, Open Graph (title, description, url, type, image 1200×630) and Twitter card tags on
  every page. Four OG cards: home, components, guides, docs.
- One H1 per page; checked by `npm run check`.
- `sitemap-index.xml` (22 URLs, 404 excluded) referenced from `robots.txt`; `llms.txt` at the root.
- JSON-LD: `SoftwareSourceCode` with a `Person` author on home, `BreadcrumbList` on inner pages,
  `TechArticle` on guides, `FAQPage` on the FAQ and on every guide (from the page's FAQ section).
  No ratings, download counts or invented numbers.
- Visible breadcrumbs that match the JSON-LD.
- Clean URLs with trailing slashes; requests without the slash get a redirect.

## Lighthouse (mobile, local preview, 2026-10-26)

| Page | Performance | Accessibility | Best practices | SEO |
|---|:-:|:-:|:-:|:-:|
| / | 100 | 100 | 100 | 100 |
| /components/ | 100 | 100 | 100 | 100 |
| /getting-started/ | 100 | 100 | 100 | 100 |
| /components/limited-offer-sheet/ | 100 | 100 | 100 | 100 |
| /guides/countdown-timer-jetpack-compose/ | 100 | 100 | 100 | 100 |
| /guides/connect-paywall-google-play-billing/ | 100 | 100 | 100 | 100 |

What got it there: demo videos start after load only when visible (the poster image is the LCP),
Google Sans Flex loads from Google Fonts with `display=optional` (no layout shift), screenshots are
540px WebP with fixed dimensions.

## Internal linking

Home → Getting started, all six designs, guides, FAQ answers. Each design → related designs and
guides + its Dokka page. Each guide → related guides and designs + install CTA + "star on GitHub".
FAQ → the page with the full answer. Footer → hub (sanikant.dev), GitHub, changelog, Telegram.

## Recommended next guides

1. Jetpack Compose onboarding screen (autocomplete: "jetpack compose onboarding screen")
2. Material 3 Expressive in Compose: dependency and setup ("material 3 expressive compose dependency/version")
3. LoadingIndicator in Compose ("compose loading indicator", "material 3 expressive loading indicator")
4. Paywall A/B testing without a paywall SDK
5. Localizing subscription prices and disclosure text

## Could not be verified from the repo

- Search volumes (no keyword tool). Watch Search Console.
- Whether Adapty's Paywall Builder is being replaced by its newer Flow Builder on Android; the
  comparison page only links Adapty's Paywall Builder docs.
- Restore/Terms/Privacy are presented as good practice, not Google Play requirements, because
  Google's subscription policy page doesn't name them.
