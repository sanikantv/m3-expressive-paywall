# Contributing

Thanks for helping. New designs, fixes and translations are all welcome.

1. Open an issue first for a new design so we can agree on the layout.
2. Keep billing out of the library: new paywalls take `List<PaywallPlan>` (or one plan) plus
   `onPurchase`, `onRestore` and `onClose`, and end with `ComplianceFooter`.
3. Use `MaterialTheme.colorScheme` and `MaterialTheme.motionScheme` only. No hard-coded colors.
4. Put user-facing text in `paywall/src/main/res/values/strings.xml` with a `pw_` prefix.
5. Add light and dark `@Preview`s and a gallery entry in the demo app.
6. Run `./gradlew :paywall:assembleRelease :app:assembleDebug :app:lintDebug` before opening a PR.
7. Update the screenshots with `./scripts/capture.sh` if a screen changed.
