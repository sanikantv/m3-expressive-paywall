#!/usr/bin/env bash
# Renders the 1200x630 Open Graph cards into public/og/ with headless Chrome. Run from site/.
set -euo pipefail
CHROME="${CHROME:-/Applications/Google Chrome.app/Contents/MacOS/Google Chrome}"
mkdir -p public/og
for s in home components guides docs; do
  "$CHROME" --headless=new --disable-gpu --hide-scrollbars --allow-file-access-from-files \
    --virtual-time-budget=3000 --window-size=1200,630 \
    --screenshot="$PWD/public/og/$s.png" "file://$PWD/og-src/og.html?section=$s" >/dev/null 2>&1
done
ls -la public/og
