#!/usr/bin/env bash
# Renders the icon PNGs from the SVGs with headless Chrome. Run from the repo root.
set -euo pipefail
CHROME="${CHROME:-/Applications/Google Chrome.app/Contents/MacOS/Google Chrome}"
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT
render() { # size[:rounded] out
  python3 scripts/make_icon.py --svg "$1" "$TMP/icon.svg"
  "$CHROME" --headless=new --disable-gpu --hide-scrollbars --default-background-color=00000000 \
    --window-size="${1%%:*},${1%%:*}" --screenshot="$PWD/$2" "file://$TMP/icon.svg" >/dev/null 2>&1
}
python3 scripts/make_icon.py
render 512 art/icon/icon-512.png                  # Play Store icon (full bleed, Play adds the mask)
render 512:rounded art/icon/icon-rounded-512.png
render 1024:rounded art/icon/icon-rounded-1024.png
echo "PNG icons written to art/icon/"
