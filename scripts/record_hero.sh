#!/usr/bin/env bash
# Records the README hero clip on a 1080x2400 emulator and converts it to art/hero.gif + art/hero.mp4.
#   ANDROID_SERIAL=emulator-5554 ./scripts/record_hero.sh
# Tap coordinates match the Medium Phone AVD (1080x2400, 420 dpi) in light mode.
set -euo pipefail

ADB="${ADB:-$HOME/Library/Android/sdk/platform-tools/adb}"
PKG="dev.sanikant.m3.paywall.demo"
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT

"$ADB" install -r app/build/outputs/apk/debug/app-debug.apk >/dev/null
"$ADB" shell settings put global sysui_demo_allowed 1
demo() { "$ADB" shell am broadcast -a com.android.systemui.demo -e command "$@" >/dev/null; }
demo enter
demo clock -e hhmm 1200
demo battery -e level 100 -e plugged false
demo network -e wifi show -e level 4
demo network -e mobile hide
demo notifications -e visible false
"$ADB" shell cmd uimode night no >/dev/null

"$ADB" shell am force-stop "$PKG"
"$ADB" shell am start -n "$PKG/.MainActivity" --ez dynamic false >/dev/null
sleep 3

"$ADB" shell rm -f /sdcard/hero.mp4
"$ADB" shell screenrecord --time-limit 26 --bit-rate 8000000 /sdcard/hero.mp4 &
REC=$!
sleep 1.5
tap() { "$ADB" shell input tap "$1" "$2"; sleep "$3"; }
"$ADB" shell input swipe 540 1900 540 900 700; sleep 1.2   # scroll the gallery, app bar collapses
tap 540 1363 1.8                                           # open Plan picker
tap 218 546 1.4                                            # Monthly
tap 860 546 1.4                                            # Lifetime
tap 540 546 1.8                                            # Yearly
"$ADB" shell input swipe 2 1200 640 1200 600; sleep 1.6    # predictive back gesture
tap 540 1786 4.0                                           # open Limited-time offer sheet
"$ADB" shell input swipe 540 1000 540 2350 250; sleep 1.4  # swipe the sheet away
tap 540 517 3.0                                            # open Free vs Pro
wait "$REC" || true
"$ADB" pull /sdcard/hero.mp4 "$TMP/raw.mp4" >/dev/null
demo exit

mkdir -p art
ffmpeg -loglevel error -y -ss 1 -i "$TMP/raw.mp4" -vf "scale=540:-2" -c:v libx264 -preset slow -crf 26 -pix_fmt yuv420p -an -movflags +faststart art/hero.mp4
ffmpeg -loglevel error -y -ss 1 -i "$TMP/raw.mp4" -vf "fps=15,scale=320:-1:flags=lanczos,split[a][b];[a]palettegen=max_colors=128:stats_mode=diff[p];[b][p]paletteuse=dither=bayer:bayer_scale=4:diff_mode=rectangle" art/hero.gif
ls -lh art/hero.gif art/hero.mp4
