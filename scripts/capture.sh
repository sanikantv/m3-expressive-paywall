#!/usr/bin/env bash
# Captures README screenshots from a running emulator. Run from the repo root:
#   ANDROID_SERIAL=emulator-5554 ./scripts/capture.sh
# Needs the debug APK built (./gradlew :app:assembleDebug). Uses the baseline purple scheme
# (dynamic color off) so screenshots look the same on every machine.
set -euo pipefail

ADB="${ADB:-$HOME/Library/Android/sdk/platform-tools/adb}"
OUT="art/screenshots"
PKG="dev.sanikant.m3.paywall.demo"
mkdir -p "$OUT"

"$ADB" install -r app/build/outputs/apk/debug/app-debug.apk >/dev/null

# Clean status bar: 12:00, full battery, full signal, no notifications.
"$ADB" shell settings put global sysui_demo_allowed 1
demo() { "$ADB" shell am broadcast -a com.android.systemui.demo -e command "$@" >/dev/null; }
demo enter
demo clock -e hhmm 1200
demo battery -e level 100 -e plugged false
demo network -e wifi show -e level 4
demo network -e mobile hide
demo notifications -e visible false

shot() { # name
  sleep "${WAIT:-4}"
  "$ADB" exec-out screencap -p > "$OUT/$1.png"
  echo "  $OUT/$1.png"
}

open() { # screen key ("" for gallery)
  "$ADB" shell am force-stop "$PKG"
  if [ -n "$1" ]; then
    "$ADB" shell am start -n "$PKG/.MainActivity" --es screen "$1" --ez dynamic false >/dev/null
  else
    "$ADB" shell am start -n "$PKG/.MainActivity" --ez dynamic false >/dev/null
  fi
}

for mode in light dark; do
  if [ "$mode" = dark ]; then "$ADB" shell cmd uimode night yes >/dev/null; else "$ADB" shell cmd uimode night no >/dev/null; fi
  echo "$mode:"
  open "";            shot "00-gallery-$mode"
  open classic;       shot "01-classic-$mode"
  open comparison;    shot "02-comparison-$mode"
  open trial;         shot "03-trial-$mode"
  open picker;        shot "04-picker-$mode"
  open offer;         shot "05-offer-$mode"
  open onboarding;    shot "06-onboarding-$mode"
done

"$ADB" shell cmd uimode night no >/dev/null
demo exit
echo "Done."
