"""Generates src/content/docs/components/*.mdx from the data below.

Parameter names, types and defaults are copied from paywall/src/main/java/dev/sanikant/m3/paywall/.
Run: python3 scripts/gen_components.py
"""
import json
import pathlib

OUT = pathlib.Path(__file__).resolve().parent.parent / "src/content/docs/components"
API = "/api/m3-expressive-paywall/dev.sanikant.m3.paywall"

SHARED = [
    ("legal", "PaywallLegal", "required", "Terms and Privacy URLs for the footer links."),
    ("onPurchase", "(PaywallPlan) -> Unit", "required", "Called with the selected plan when the button is tapped. Start your billing flow here."),
    ("onRestore", "() -> Unit", "required", "Called from the Restore link."),
    ("onClose", "() -> Unit", "required", "Called from the close button."),
    ("modifier", "Modifier", "`Modifier`", "Applied to the paywall's root."),
]

PAGES = [
    dict(
        slug="classic-plans-paywall", name="ClassicPlansPaywall", label="Classic plans",
        title="Classic plans paywall for Jetpack Compose",
        description="ClassicPlansPaywall: a Jetpack Compose paywall with a Material 3 Expressive hero, monthly, yearly and lifetime plan cards, a Best value badge and a features list.",
        screen="01-classic", alt="Classic plans paywall",
        intro="A familiar layout that works for most apps: an animated hero built from `MaterialShapes`, the plan cards right under the title so prices are visible without scrolling, then your feature list.",
        when=["You sell two or three plans, such as monthly, yearly and lifetime.", "You want the safest, most familiar paywall to start with."],
        params=[
            ("plans", "List<PaywallPlan>", "required", "Plans to offer, in display order. Must not be empty."),
            ("features", "List<PaywallFeature>", "required", "Benefits listed under the plans."),
            *SHARED,
            ("title", "String", '"Unlock everything"', "Headline."),
            ("subtitle", "String?", '"One upgrade. Every feature. No ads, ever."', "Line under the headline. `null` hides it."),
            ("initialPlanId", "String?", "`null`", "Plan to preselect. Defaults to the first plan with a `badge`, else the first plan."),
            ("heroIcon", "ImageVector", "`PaywallIcons.Sparkle`", "Icon in the center of the hero art."),
        ],
        snippets=[("Components.kt", "classic-usage", "Basic usage"), ("Components.kt", "classic-custom", "Custom copy, preselected plan and icon")],
        custom="Colors come from `MaterialTheme.colorScheme`, so dynamic color and dark mode just work. Selection animations follow `MaterialTheme.motionScheme`: `MotionScheme.expressive()` gives the springy corner and radio morph shown above, `MotionScheme.standard()` a calmer version.",
        a11y=["Each plan card is `selectable` with `Role.RadioButton`, so TalkBack reads it as a radio option with its selected state.", "The close button and the footer links are at least 48dp tall.", "Text uses the theme's type scale and wraps at large font sizes; the body scrolls while the button stays fixed."],
        mistakes=["Hard-coding prices. Always pass the localized `formattedPrice` from the store.", "Badging a plan and then passing `initialPlanId` for another one: the badge no longer marks the preselected plan, which confuses users.", "Showing the yearly plan only as a monthly figure. Put the real yearly price in `price` and use `priceDetail` for \"$2.49/month\"."],
        related=[("Trial timeline paywall", "/components/trial-timeline-paywall/"), ("PaywallPlan and models", "/components/paywall-plan/"), ("How to build a paywall in Jetpack Compose", "/guides/how-to-build-a-paywall-jetpack-compose/")],
    ),
    dict(
        slug="comparison-paywall", name="ComparisonPaywall", label="Free vs Pro table",
        title="Free vs Pro comparison paywall for Jetpack Compose",
        description="ComparisonPaywall: a Jetpack Compose subscription screen with a Free vs Pro feature table, a highlighted Pro column, check and cross icons and side-by-side plan tiles.",
        screen="02-comparison", alt="Free vs Pro comparison paywall",
        intro="Shows what the free tier includes and what Pro adds, as a table with a highlighted Pro column. Text values such as \"50\" vs \"Unlimited\" replace the icons where a limit is easier to read than a check mark.",
        when=["Your free tier is generous and users ask \"what do I actually get?\"", "You want to show what is available without a subscription, which Google Play's policy expects."],
        params=[
            ("plans", "List<PaywallPlan>", "required", "Plans shown as tiles side by side. Two or three fit best."),
            ("rows", "List<ComparisonRow>", "required", "Table rows. See [ComparisonRow](/components/paywall-plan/#comparisonrow)."),
            *SHARED,
            ("title", "String", '"Free vs Pro"', "Headline."),
            ("subtitle", "String?", '"See exactly what you get when you upgrade."', "Line under the headline."),
            ("freeLabel", "String", '"Free"', "Header of the free column."),
            ("proLabel", "String", '"Pro"', "Header of the highlighted column."),
            ("initialPlanId", "String?", "`null`", "Plan to preselect."),
        ],
        snippets=[("SampleData.kt", "comparison-rows", "Rows"), ("Components.kt", "comparison-usage", "Usage with your own tier names")],
        custom="Rename the columns with `freeLabel` and `proLabel` (\"Basic\" and \"Plus\" above). The Pro column uses `primaryContainer`, so it follows your theme.",
        a11y=["Check and cross icons have content descriptions (\"Included\", \"Not included\"), so the table reads correctly with TalkBack.", "Plan tiles are radio options, like the plan cards in the other designs."],
        mistakes=["Too many rows. Six to eight rows keep the plans above the fold on most phones.", "More than three plans: the tiles get narrow. Use the classic design for four or more."],
        related=[("Classic plans paywall", "/components/classic-plans-paywall/"), ("Google Play subscription paywall requirements", "/guides/google-play-subscription-paywall-requirements/")],
    ),
    dict(
        slug="trial-timeline-paywall", name="TrialTimelinePaywall", label="Trial timeline",
        title="Free trial timeline paywall for Jetpack Compose",
        description="TrialTimelinePaywall: a Jetpack Compose free trial paywall that shows Today, a reminder day and the billing day as a timeline, with plan cards and Play disclosures.",
        screen="03-trial", alt="Trial timeline paywall",
        intro="Explains exactly what happens during the trial: full access today, a reminder before it ends, and the day billing starts. The timeline describes the first plan that has a `trialText`, and fades when the user selects a plan without a trial.",
        when=["You offer a free trial and want more people to start it with confidence.", "You want to reduce \"I forgot to cancel\" refund requests."],
        params=[
            ("plans", "List<PaywallPlan>", "required", "Plans to offer. The first plan with `trialText` drives the timeline."),
            *SHARED,
            ("title", "String", '"How your free trial works"', "Headline."),
            ("subtitle", "String?", '"No surprises. We remind you before you pay."', "Line under the headline."),
            ("steps", "List<TrialStep>", "`defaultTrialSteps(trialPlan)`", "Timeline steps. The default is Today, Day 5 and Day 7."),
            ("initialPlanId", "String?", "`null`", "Plan to preselect. Defaults to the first plan with a trial."),
        ],
        extra="""### defaultTrialSteps

`defaultTrialSteps(plan: PaywallPlan, trialDays: Int = 7, reminderDay: Int = trialDays - 2): List<TrialStep>`
builds the three default steps from the plan's price and period. It is `@Composable` because it reads
the translated strings.
""",
        snippets=[("Components.kt", "trial-usage", "Basic usage"), ("Components.kt", "trial-14-days", "A 14-day trial"), ("Components.kt", "trial-own-steps", "Your own steps")],
        custom="Pass your own `TrialStep` list to change the copy or icons. Keep the last step's price identical to the plan's price; the footer repeats it.",
        a11y=["Timeline icons are decorative; the step title and description carry the meaning for TalkBack.", "The selected plan is announced as a radio option."],
        mistakes=["Promising a reminder you never send. If the timeline says Day 5, schedule that notification (for example with WorkManager).", "A `trialText` that disagrees with the store's real trial length."],
        related=[("Free trial paywall guide", "/guides/free-trial-paywall-jetpack-compose/"), ("Google Play subscription paywall requirements", "/guides/google-play-subscription-paywall-requirements/")],
    ),
    dict(
        slug="plan-picker-paywall", name="PlanPickerPaywall", label="Plan picker",
        title="Plan picker paywall with a button group for Jetpack Compose",
        description="PlanPickerPaywall: a Material 3 Expressive paywall where a connected ButtonGroup of ToggleButtons switches plans and the spotlight card morphs its shape and rolls the price.",
        screen="04-picker", alt="Plan picker paywall",
        intro="The most \"Expressive\" of the six. A connected button group switches plans; the spotlight card below morphs between `MaterialShapes`, changes color and rolls the price with `AnimatedContent`.",
        when=["You have two to four plans with short names.", "You want the paywall itself to feel like a showcase of Material 3 Expressive."],
        params=[
            ("plans", "List<PaywallPlan>", "required", "Two to four plans. Titles appear in the buttons, so keep them short."),
            ("features", "List<PaywallFeature>", "required", "Benefits under the card."),
            *SHARED,
            ("title", "String", '"Choose your plan"', "Headline."),
            ("subtitle", "String?", '"Switch anytime. Your progress stays with you."', "Line under the headline."),
            ("initialPlanId", "String?", "`null`", "Plan to preselect."),
        ],
        snippets=[("Components.kt", "picker-usage", "Usage")],
        custom="The button group uses `ButtonGroupDefaults.connectedLeading/Middle/TrailingButtonShapes()`. To build the same control elsewhere, read [Connected button group in Jetpack Compose](/guides/connected-button-group-jetpack-compose/). The card morph is explained in [Shape morphing with MaterialShapes](/guides/shape-morphing-material-shapes-compose/).",
        a11y=["Each toggle button has `Role.RadioButton` semantics, because only one plan can be chosen.", "Buttons are 56dp tall."],
        mistakes=["Long plan titles like \"Annual subscription\" get cut off in the buttons. Use \"Yearly\".", "Five or more plans. Use the classic design instead."],
        related=[("Connected button group guide", "/guides/connected-button-group-jetpack-compose/"), ("Shape morphing guide", "/guides/shape-morphing-material-shapes-compose/")],
    ),
    dict(
        slug="limited-offer-sheet", name="LimitedOfferSheet", label="Limited-time offer",
        title="Limited-time offer bottom sheet for Jetpack Compose",
        description="LimitedOfferSheet: a Material 3 modal bottom sheet paywall with a discount badge, struck-through price, live countdown and LinearWavyProgressIndicator for Jetpack Compose.",
        screen="05-offer", alt="Limited-time offer bottom sheet",
        intro="A `ModalBottomSheet` you can show over any screen: a big discount, the original price struck through, a countdown and a wavy progress bar that drains as time runs out. When the time is up, the button switches to \"Offer ended\" and is disabled.",
        when=["Win-back offers for lapsed subscribers.", "A launch or seasonal discount with a real deadline."],
        params=[
            ("plan", "PaywallPlan", "required", "The discounted plan. Its `price` is the offer price."),
            ("originalPrice", "String", "required", "Regular price, shown struck through."),
            ("discountLabel", "String", "required", "Short text in the badge, such as \"-50%\"."),
            ("offerEndsAtMillis", "Long", "required", "When the offer ends, as `System.currentTimeMillis()` time."),
            *SHARED[:4],
            ("modifier", "Modifier", "`Modifier`", "Applied to the sheet."),
            ("title", "String", '"Limited-time offer"', "Headline."),
            ("subtitle", "String", '"A special price, just for you."', "Line under the headline."),
            ("offerDurationMillis", "Long", "`15 * 60_000L`", "Full length of the offer. Scales the progress bar."),
            ("sheetState", "SheetState", "Hidden, skips the half-expanded state", "Sheet state, if you need to control it."),
        ],
        snippets=[("Components.kt", "offer-usage", "Usage")],
        callout="""<Aside type="note" title="Opt-in needed">
  `LimitedOfferSheet` exposes material3's `SheetState`, which material3 marks
  `@ExperimentalMaterial3Api`. Add `@OptIn(ExperimentalMaterial3Api::class)` where you call it,
  as in the example.
</Aside>
""",
        custom="Intro prices need the full story: put \"First year only, then $29.99 per year\" in `priceDetail`. `onClose` is called both by the close button and by swiping the sheet down.",
        a11y=["The countdown is announced as one phrase (\"Offer ends in 12 min 30 sec\") instead of separate digits.", "The sheet has a close button in addition to swipe-to-dismiss."],
        mistakes=["Resetting the deadline on every launch. Fake urgency breaks consumer protection rules; store the real end time.", "Leaving out the regular price after the intro period."],
        related=[("Countdown timer guide", "/guides/countdown-timer-jetpack-compose/"), ("Classic plans paywall", "/components/classic-plans-paywall/")],
    ),
    dict(
        slug="onboarding-paywall", name="OnboardingPaywall", label="Onboarding end",
        title="Onboarding paywall carousel for Jetpack Compose",
        description="OnboardingPaywall: a Jetpack Compose onboarding carousel of benefit pages that ends in Material 3 Expressive plan selection, with skip, page indicator and Play disclosures.",
        screen="06-onboarding", alt="Onboarding paywall",
        intro="A short `HorizontalPager` of benefit pages that ends in the plan selection. A Skip button jumps to the plans, and the legal footer appears on the plan page, where buying becomes possible.",
        when=["The paywall is the last step of your onboarding.", "You want to explain value before you ask for money."],
        params=[
            ("pages", "List<BenefitPage>", "required", "Benefit pages shown before the plans. Three or four work best."),
            ("plans", "List<PaywallPlan>", "required", "Plans on the last page."),
            *SHARED,
            ("plansTitle", "String", '"Ready to go Pro?"', "Headline of the plan page."),
            ("plansSubtitle", "String?", '"Pick a plan to keep everything you just saw."', "Line under it."),
            ("initialPlanId", "String?", "`null`", "Plan to preselect."),
            ("pagerState", "PagerState", "`rememberPagerState { pages.size + 1 }`", "Pager state, for example to start on the plan page."),
        ],
        snippets=[("SampleData.kt", "pages", "Pages"), ("Components.kt", "onboarding-usage", "Usage"), ("Components.kt", "onboarding-pager", "Open on the plan page")],
        custom="The page count is `pages.size + 1`; the extra page is the plan selection. Pass your own `pagerState` to control it.",
        a11y=["The page indicator announces \"Page 2 of 4\".", "Skip and Next are real buttons, so the carousel works without swiping."],
        mistakes=["Treating `onClose` as \"cancel onboarding\". Usually it should finish onboarding and continue to the free experience.", "Long benefit copy. One line of title and one or two lines of description per page."],
        related=[("Classic plans paywall", "/components/classic-plans-paywall/"), ("How to build a paywall in Jetpack Compose", "/guides/how-to-build-a-paywall-jetpack-compose/")],
    ),
]


def table(rows):
    lines = ["| Parameter | Type | Default | Description |", "|---|---|---|---|"]
    for name, typ, default, desc in rows:
        lines.append(f"| `{name}` | `{typ}` | {default} | {desc} |")
    return "\n".join(lines)


def page(p, order):
    api = f"{API}/-{''.join('-' + c.lower() if c.isupper() else c for c in p['name'])[1:]}.html"
    if len(p["snippets"]) == 1:
        f, r, _ = p["snippets"][0]
        snippets = f"<Snippet file=\"{f}\" region=\"{r}\" />"
    else:
        snippets = "\n\n".join(f"### {t}\n\n<Snippet file=\"{f}\" region=\"{r}\" />" for f, r, t in p["snippets"])
    return f"""---
title: {json.dumps(p['title'])}
description: {json.dumps(p['description'])}
sidebar:
  label: {json.dumps(p['label'])}
  order: {order}
---

{{/* Generated by site/scripts/gen_components.py. Edit the script, not this file. */}}

import {{ Aside, LinkCard, CardGrid }} from '@astrojs/starlight/components';
import Install from '../../../components/Install.astro';
import Screens from '../../../components/Screens.astro';
import Snippet from '../../../components/Snippet.astro';

`{p['name']}` is design {order} of [M3 Expressive Paywall](/). {p['intro']}

<Screens name="{p['screen']}" alt="{p['alt']}" />

<Install only="dependency" />

## When to use it

{chr(10).join('- ' + w for w in p['when'])}

## Usage

{p.get('callout', '')}
{snippets}

## Parameters

{table(p['params'])}

{p.get('extra', '')}
Full signature: [{p['name']} in the API reference]({api}).

## Customization

{p['custom']}

## Accessibility

{chr(10).join('- ' + a for a in p['a11y'])}

## Common mistakes

{chr(10).join('- ' + m for m in p['mistakes'])}

## Related

<CardGrid>
{chr(10).join(f'  <LinkCard title="{t}" href="{h}" />' for t, h in p['related'])}
</CardGrid>
"""


for i, p in enumerate(PAGES, start=1):
    (OUT / f"{p['slug']}.mdx").write_text(page(p, i))
print(f"wrote {len(PAGES)} component pages")
