"""Builds captions (WebVTT) for the intro from the ElevenLabs word timings.

The voiceover starts 0.4 s into the film (see index.html), so every cue is shifted by that much.
Run: python3 tools/make_vtt.py > renders/intro.en.vtt
"""
import json

OFFSET = 0.4
MAX_WORDS = 9
words = json.load(open("assets/audio/voiceover.words.json"))

def ts(t):
    h, rem = divmod(t, 3600)
    m, s = divmod(rem, 60)
    return f"{int(h):02d}:{int(m):02d}:{s:06.3f}"

cues, cur = [], []
for w in words:
    cur.append(w)
    if w["w"].endswith((".", ",", ":", "?")) and len(cur) >= 3 or len(cur) >= MAX_WORDS or w["w"].endswith((".", ":")):
        cues.append(cur)
        cur = []
if cur:
    cues.append(cur)

print("WEBVTT\n")
for i, c in enumerate(cues, 1):
    text = " ".join(x["w"] for x in c).replace("paywall dot sanikant dot dev", "paywall.sanikant.dev")
    print(f"{i}\n{ts(c[0]['start'] + OFFSET)} --> {ts(c[-1]['end'] + OFFSET + 0.15)}\n{text}\n")
