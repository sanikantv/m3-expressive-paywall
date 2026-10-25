#!/usr/bin/env bash
# Renders art/social-preview.png (GitHub social preview, 1280x640) with headless Chrome.
set -euo pipefail
CHROME="${CHROME:-/Applications/Google Chrome.app/Contents/MacOS/Google Chrome}"
"$CHROME" --headless=new --disable-gpu --hide-scrollbars --allow-file-access-from-files \
  --window-size=1280,640 --screenshot="$PWD/art/social-preview.png" "file://$PWD/art/social-preview.html" >/dev/null 2>&1
echo "art/social-preview.png written"
