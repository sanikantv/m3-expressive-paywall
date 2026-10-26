"""Writes compositions/*.html from the templates below.

Shared pieces (fonts, the phone frame, the MaterialShapes cookie) live here once so the nine scene
files stay consistent. Edit this file and run: python3 tools/build_scenes.py
Scene times are local to each scene; the global anchor word is noted next to each tween.
"""
import math, pathlib

def cookie(cx=50, cy=50, r=44, bumps=8, depth=0.075, steps=96):
    pts = []
    for i in range(steps):
        t = 2 * math.pi * i / steps
        rr = r * (1 + depth * math.cos(bumps * t))
        pts.append((cx + rr * math.cos(t - math.pi / 2), cy + rr * math.sin(t - math.pi / 2)))
    return "M" + " L".join(f"{x:.1f},{y:.1f}" for x, y in pts) + "Z"

COOKIE = cookie()
CHECK = "M37,58.6 L26.6,48.2 l-4.2,4.2 L37,67 66.6,37.4 l-4.2,-4.2z"  # Material check, 100 viewBox

FONTS = """
    @font-face { font-family: 'Google Sans Flex'; src: url('assets/fonts/google-sans-flex-latin.woff2') format('woff2'); font-weight: 1 1000; }
    @font-face { font-family: 'Google Sans Code'; src: url('assets/fonts/GoogleSansCode.ttf') format('truetype'); font-weight: 300 800; }
    #root { position: absolute; inset: 0; overflow: hidden; font-family: 'Google Sans Flex', sans-serif; color: #1d1b20; }
    .phone { position: absolute; width: 420px; height: 900px; border-radius: 66px; background: #1d1b20; padding: 14px;
             box-shadow: 0 30px 60px rgba(29, 27, 32, 0.22); }
    .screen { position: relative; width: 392px; height: 872px; border-radius: 52px; overflow: hidden; background: #fef7ff; }
    .screen img, .screen video { position: absolute; left: 0; top: 0; width: 392px; height: 872px; object-fit: cover; }
    .display { font-weight: 700; font-size: 104px; letter-spacing: -0.02em; line-height: 1.04; }
    .code { font-family: 'Google Sans Code', monospace; font-weight: 500; }
"""

def scene(cid, css, body, script):
    return f"""<template>
  <style>{FONTS}{css}
  </style>
  <div id="root" data-composition-id="{cid}" data-width="1920" data-height="1080">
{body}
  </div>
  <script>
    const tl = gsap.timeline({{ paused: true }});
    const wipeIn = (sel, at, dur = 0.35) =>
      tl.fromTo(sel, {{ clipPath: "inset(0 100% 0 0)" }}, {{ clipPath: "inset(0 0% 0 0)", duration: dur, ease: "power3.out" }}, at);
    const wipeOut = (sel, at, dur = 0.22) =>
      tl.fromTo(sel, {{ clipPath: "inset(0 0% 0 0)" }}, {{ clipPath: "inset(0 0% 0 100%)", duration: dur, ease: "power2.in", immediateRender: false }}, at);
{script}
    window.__timelines["{cid}"] = tl;
  </script>
</template>
"""

S = {}

# 1. Hook (0.0-3.4): "Your app is ready. The paywall isn't."
S["hook"] = scene("hook", """
    #hook-bg { position: absolute; inset: 0; background: #f3edf7; }
    #hook-phone { left: 250px; top: 90px; }
    #hook-empty { position: absolute; inset: 0; background: #fef7ff; display: flex; align-items: center; justify-content: center; }
    #hook-slot { width: 300px; height: 360px; border-radius: 40px; border: 5px dashed #cac4d0; }
    .hook-line { position: absolute; left: 860px; top: 430px; white-space: nowrap; }
    #hook-isnt { color: #6750a4; }
""", """
    <div id="hook-bg"></div>
    <div id="hook-phone" class="phone"><div class="screen">
      <img src="assets/img/00-gallery-light.png" alt="Demo app gallery" />
      <div id="hook-empty"><div id="hook-slot"></div></div>
    </div></div>
    <div id="hook-ready" class="hook-line display">Your app is ready.</div>
    <div id="hook-paywall" class="hook-line display">The paywall <span id="hook-isnt">isn't.</span></div>
""", """
    tl.fromTo("#hook-phone", { y: 40, scale: 0.96 }, { y: 0, scale: 1, duration: 0.6, ease: "back.out(1.7)" }, 0);
    wipeIn("#hook-ready", 0.3, 0.5);                 // "Your" 0.40 … "ready" 1.03
    wipeOut("#hook-ready", 1.85);
    wipeIn("#hook-paywall", 2.05);                    // "paywall" 2.13
    tl.fromTo("#hook-empty", { opacity: 0 }, { opacity: 1, duration: 0.3, ease: "power2.out" }, 2.4);
    tl.fromTo("#hook-slot", { scale: 0.7 }, { scale: 1, duration: 0.45, ease: "back.out(1.7)" }, 2.4);
""")

# 2. Rules (3.4-12.5): Play's four disclosure rules as a checklist on a dark sheet.
rows = [("price", "Price", 3.82), ("period", "Billing period", 4.78), ("trial", "Trial terms", 5.9), ("renew", "That it auto-renews", 7.55)]
row_html = "\n".join(
    f'    <div id="rules-{k}" class="rules-row"><svg class="rules-bullet" viewBox="0 0 100 100"><path d="{COOKIE}" /></svg><span class="rules-text">{t}</span></div>'
    for k, t, _ in rows)
row_js = "\n".join(
    f'    tl.fromTo("#rules-{k} .rules-bullet", {{ scale: 0, rotation: -90 }}, {{ scale: 1, rotation: 0, duration: 0.45, ease: "back.out(2)" }}, {at});\n'
    f'    wipeIn("#rules-{k} .rules-text", {at + 0.05:.2f});'
    for k, _, at in rows)
S["rules"] = scene("rules", """
    #rules-bg { position: absolute; inset: 0; background: #f3edf7; }
    #rules-sheet { position: absolute; left: 0; top: 0; width: 1920px; height: 1080px; background: #1d1b20; border-radius: 48px 48px 0 0; }
    #rules-title { position: absolute; left: 160px; top: 150px; color: #e6e0e9; white-space: nowrap; }
    #rules-title b { color: #d0bcff; font-weight: 700; }
    #rules-source { position: absolute; left: 164px; top: 290px; font-size: 34px; font-weight: 500; color: #938f99; }
    .rules-row { position: absolute; left: 160px; display: flex; align-items: center; gap: 36px; }
    #rules-price { top: 420px; } #rules-period { top: 560px; } #rules-trial { top: 700px; } #rules-renew { top: 840px; }
    .rules-bullet { width: 84px; height: 84px; }
    .rules-bullet path { fill: none; stroke: #d0bcff; stroke-width: 5; }
    .rules-text { font-size: 72px; font-weight: 600; color: #e6e0e9; letter-spacing: -0.01em; white-space: nowrap; }
""", f"""
    <div id="rules-bg"></div>
    <div id="rules-sheet">
    <div id="rules-title" class="display">Google Play has <b>rules</b></div>
    <div id="rules-source">Play Console Help · Subscriptions policy</div>
{row_html}
    </div>
""", f"""
    tl.fromTo("#rules-sheet", {{ y: 1080 }}, {{ y: 0, duration: 0.5, ease: "expo.out" }}, 0);
    wipeIn("#rules-title", 0.32, 0.45);              // "Google" 4.00 … "rules" 6.24
    wipeIn("#rules-source", 1.1);
{row_js}
""")

# 3. Reveal (12.5-15.4): "Meet M3 Expressive Paywall." The mark lands, the wordmark wipes in.
S["reveal"] = scene("reveal", """
    #reveal-bg { position: absolute; inset: 0; background: #eaddff; }
    #reveal-lockup { position: absolute; left: 0; right: 0; top: 0; bottom: 0; display: flex; align-items: center; justify-content: center; gap: 52px; }
    #reveal-icon { width: 250px; height: 250px; border-radius: 58px; }
    #reveal-word { font-size: 112px; font-weight: 700; letter-spacing: -0.025em; color: #21005d; white-space: nowrap; }
""", """
    <div id="reveal-bg"></div>
    <div id="reveal-lockup">
      <img id="reveal-icon" src="assets/img/icon.svg" alt="M3 Expressive Paywall icon" />
      <div id="reveal-word">M3 Expressive Paywall</div>
    </div>
""", """
    tl.fromTo("#reveal-icon", { scale: 0, rotation: -120 }, { scale: 1, rotation: 0, duration: 0.7, ease: "back.out(1.6)" }, 0.18);  // "Meet" 12.74
    wipeIn("#reveal-word", 0.75, 0.5);
""")

# 4. Six designs (15.4-32.7): one carousel, the centre phone is the design being named.
designs = [("01-classic-light", "ClassicPlansPaywall"), ("02-comparison-light", "ComparisonPaywall"),
           ("03-trial-light", "TrialTimelinePaywall"), ("04-picker-light", "PlanPickerPaywall"),
           ("05-offer-light", "LimitedOfferSheet"), ("06-onboarding-light", "OnboardingPaywall")]
GAP = 500  # px between phone centres in the strip
phones = []
for i, (img, api) in enumerate(designs):
    extra = ""
    if api == "PlanPickerPaywall":
        # The real recording: switching plans morphs the spotlight card.
        extra = '<video id="six-picker-video" class="clip" src="assets/video/hero-demo.mp4" data-start="11.15" data-duration="2.2" data-media-start="4.3" data-track-index="2" muted playsinline></video>'
    phones.append(f'      <div id="six-p{i}" class="phone six-phone" style="left:{i * GAP}px"><div class="screen"><img src="assets/img/{img}.png" alt="{api}" />{extra}</div></div>')
labels = "\n".join(f'    <div id="six-l{i}" class="six-label code">{api}</div>' for i, (_, api) in enumerate(designs))
# strip x so phone i is centred: centre x of phone i = i*GAP + 210 -> 960
center_x = lambda i: 960 - (i * GAP + 210)
steps = [(5.85, 0), (7.6, 1), (9.55, 2), (11.15, 3), (13.25, 4), (15.15, 5)]  # Classic 21.48, Free 23.14, trial 25.16, picker 27.07, limited 28.94, onboarding 31.05
six_js = [
    '    tl.fromTo("#six-strip", { x: %.1f, y: 330, scale: 0.42 }, { x: %.1f, y: 330, scale: 0.42, duration: 0.01 }, 0);' % (960 - 0.42 * (2.5 * GAP + 210), 960 - 0.42 * (2.5 * GAP + 210)),
    '    tl.fromTo(".six-phone", { y: 160, opacity: 0 }, { y: 0, opacity: 1, duration: 0.55, ease: "back.out(1.6)", stagger: 0.07 }, 0.15);  // "Six" 15.59',
    '    wipeIn("#six-kicker", 1.0, 0.45);',
    '    tl.to("#six-strip", { x: %.1f, y: 310, scale: 0.47, duration: 4.4, ease: "sine.inOut" }, 0.9);' % (960 - 0.47 * (2.5 * GAP + 210)),
    '    wipeOut("#six-kicker", 5.5);',
]
prev = None
for at, i in steps:
    six_js.append(f'    tl.to("#six-strip", {{ x: {center_x(i)}, y: 90, scale: 1, duration: 0.55, ease: "expo.inOut" }}, {at - 0.3:.2f});')
    if prev is not None:
        six_js.append(f'    wipeOut("#six-l{prev}", {at - 0.3:.2f});')
    six_js.append(f'    wipeIn("#six-l{i}", {at + 0.15:.2f});')
    prev = i
S["six"] = scene("six", """
    #six-bg { position: absolute; inset: 0; background: #fef7ff; }
    #six-strip { position: absolute; left: 0; top: 0; width: 2920px; height: 900px; transform-origin: 0 0; }
    .six-phone { top: 0; }
    #six-kicker { position: absolute; left: 0; right: 0; top: 150px; text-align: center; font-size: 64px; font-weight: 700; letter-spacing: -0.015em; color: #1d1b20; }
    #six-kicker span { color: #6750a4; }
    .six-label { position: absolute; left: 0; right: 0; top: 1004px; text-align: center; font-size: 40px; color: #4a4458; }
""", f"""
    <div id="six-bg"></div>
    <div id="six-kicker">6 paywalls · Jetpack Compose · <span>Material 3 Expressive</span></div>
    <div id="six-strip" data-layout-allow-overflow>
{chr(10).join(phones)}
    </div>
{labels}
""", "\n".join(six_js))

# 5. Code (32.7-37.6): the real snippet from docs-snippets (Components.kt, region classic-usage).
code_lines = [
    ('ClassicPlansPaywall(', ''),
    ('    plans = Sample.plans,', 'plans'),
    ('    features = Sample.features,', ''),
    ('    legal = Sample.legal,', ''),
    ('    onPurchase = { plan -> buy(plan) },', 'cb'),
    ('    onRestore = restore,', 'cb'),
    ('    onClose = close,', 'cb'),
    (')', ''),
]
def hl(s):
    import html, re
    s = html.escape(s)
    s = re.sub(r'^(\s*)(\w+)( =)', r'\1<span class="k">\2</span>\3', s)
    s = s.replace('ClassicPlansPaywall', '<span class="f">ClassicPlansPaywall</span>')
    return s
code_html = "\n".join(f'      <div class="code-line{" hl-" + g if g else ""}">{hl(t) or "&nbsp;"}</div>' for t, g in code_lines)
S["code"] = scene("code", """
    #code-bg { position: absolute; inset: 0; background: #fef7ff; }
    #code-card { position: absolute; left: 300px; top: 170px; width: 1320px; height: 740px; background: #ffffff; border-radius: 32px;
                 box-shadow: 0 24px 60px rgba(29, 27, 32, 0.14); padding: 48px 64px; transform-origin: 50% 50%; }
    #code-file { font-size: 30px; color: #79747e; margin-bottom: 26px; }
    .code-line { font-size: 46px; line-height: 1.5; color: #1d1b20; white-space: pre; position: relative; }
    .code-line .k { color: #6750a4; }
    .code-line .f { color: #7d5260; font-weight: 700; }
    .code-band { position: absolute; left: 40px; width: 1240px; border-radius: 16px; background: #eaddff; }
    #code-band-plans { top: 171px; height: 69px; }
    #code-band-cb { top: 378px; height: 207px; }
    #code-lines { position: relative; }
    #code-phone { left: 750px; top: 90px; }
""", f"""
    <div id="code-bg"></div>
    <div id="code-card">
      <div id="code-file" class="code">UpgradeScreen.kt</div>
      <div id="code-band-plans" class="code-band"></div>
      <div id="code-band-cb" class="code-band"></div>
      <div id="code-lines" class="code">
{code_html}
      </div>
    </div>
    <div id="code-phone" class="phone"><div class="screen"><img src="assets/img/01-classic-light.png" alt="Classic plans paywall" /></div></div>
""", """
    tl.fromTo("#code-card", { y: 80, opacity: 0 }, { y: 0, opacity: 1, duration: 0.5, ease: "back.out(1.4)" }, 0);
    tl.fromTo(".code-line", { clipPath: "inset(0 100% 0 0)" }, { clipPath: "inset(0 0% 0 0)", duration: 0.3, ease: "power2.out", stagger: 0.05 }, 0.15);
    tl.fromTo("#code-band-plans", { scaleX: 0, transformOrigin: "0 50%" }, { scaleX: 1, duration: 0.35, ease: "power3.out" }, 0.75);  // "plans" 33.49
    tl.to("#code-band-plans", { opacity: 0, duration: 0.2 }, 1.3);
    tl.fromTo("#code-band-cb", { scaleX: 0, transformOrigin: "0 50%" }, { scaleX: 1, duration: 0.35, ease: "power3.out" }, 1.35);  // "three" 34.09
    tl.fromTo("#code-card", { scale: 1, x: 0 }, { scale: 0.3, x: 0, opacity: 0, duration: 0.55, ease: "expo.inOut" }, 3.35);  // "whole" 36.10
    tl.fromTo("#code-phone", { scale: 0.4, opacity: 0 }, { scale: 1, opacity: 1, duration: 0.6, ease: "back.out(1.5)" }, 3.5);
""")

# 6. Disclosure (37.6-44.3), the payoff: punch into the real footer; the four rules come back checked.
SC = 2.5                     # punch-in scale
FX, FY = 210, 812            # footer centre in phone coordinates (measured on 01-classic-light.png)
L0, T0 = 1060, 90            # phone position before the punch-in
TX, TY = 1290, 600           # where the footer centre lands on screen
check_rows = [("Price", 1.3), ("Billing period", 1.42), ("Trial terms", 1.54), ("Auto-renew note", 1.66), ("Restore · Terms · Privacy", 3.62)]
crow_html = "\n".join(
    f'    <div id="disc-r{i}" class="disc-row" style="top:{300 + i * 118}px"><svg class="disc-check" viewBox="0 0 100 100"><path class="c" d="{COOKIE}" /><path class="t" d="{CHECK}" /></svg><span class="disc-text">{t}</span></div>'
    for i, (t, _) in enumerate(check_rows))
crow_js = "\n".join(
    f'    tl.fromTo("#disc-r{i} .disc-check", {{ scale: 0, rotation: -90 }}, {{ scale: 1, rotation: 0, duration: 0.45, ease: "back.out(2)" }}, {at});\n'
    f'    wipeIn("#disc-r{i} .disc-text", {at + 0.04:.2f});'
    for i, (_, at) in enumerate(check_rows))
S["disclosure"] = scene("disclosure", f"""
    #disc-bg {{ position: absolute; inset: 0; background: #f3edf7; }}
    #disc-head {{ position: absolute; left: 120px; top: 150px; font-size: 64px; font-weight: 700; letter-spacing: -0.015em; white-space: nowrap; }}
    .disc-row {{ position: absolute; left: 120px; display: flex; align-items: center; gap: 26px; }}
    .disc-check {{ width: 64px; height: 64px; }}
    .disc-check .c {{ fill: #6750a4; }}
    .disc-check .t {{ fill: #ffffff; }}
    .disc-text {{ font-size: 46px; font-weight: 600; white-space: nowrap; }}
    #disc-cam {{ position: absolute; left: {L0}px; top: {T0}px; width: 420px; height: 900px; transform-origin: 0 0; }}
    #disc-phone {{ left: 0; top: 0; }}
    .disc-box {{ position: absolute; border: 3px solid #6750a4; border-radius: 10px; }}
    #disc-box-text {{ left: 48px; top: 777px; width: 324px; height: 41px; }}
    #disc-box-links {{ left: 114px; top: 821px; width: 193px; height: 28px; }}
""", f"""
    <div id="disc-bg"></div>
    <div id="disc-head">On every paywall</div>
{crow_html}
    <div id="disc-cam" data-layout-allow-overflow>
      <div id="disc-phone" class="phone"><div class="screen"><img src="assets/img/01-classic-light.png" alt="Classic plans paywall footer" /></div></div>
      <div id="disc-box-text" class="disc-box"></div>
      <div id="disc-box-links" class="disc-box"></div>
    </div>
""", f"""
    tl.fromTo("#disc-cam", {{ x: 0, y: 0, scale: 1 }}, {{ x: {TX - L0 - FX * SC:.1f}, y: {TY - T0 - FY * SC:.1f}, scale: {SC}, duration: 0.6, ease: "expo.inOut" }}, 0.3);
    wipeIn("#disc-head", 0.4);
    tl.fromTo("#disc-box-text", {{ opacity: 0, scale: 1.25 }}, {{ opacity: 1, scale: 1, duration: 0.35, ease: "back.out(2)" }}, 1.3);   // "disclosure" 38.93
    tl.fromTo("#disc-box-links", {{ opacity: 0, scale: 1.25 }}, {{ opacity: 1, scale: 1, duration: 0.35, ease: "back.out(2)" }}, 3.62); // "Restore" 41.26
{crow_js}
""")

# 7. Billing (44.3-50.5): one lambda, three backends.
bodies = [
    ("b0", '<span class="cm">// your billing here</span>', None, 0.35, 1.7),
    ("b1", 'launchPurchase(activity, billingClient, product, plan)', "Google Play Billing 9", 1.8, 3.32),   # "Google" 46.31
    ("b2", 'purchase(activity, packages, plan) { unlock() }', "RevenueCat 10", 3.37, 4.72),                  # "RevenueCat" 47.71
    ("b3", 'yourBackend.subscribe(plan.id)', "Your own backend", 4.82, None),                               # "backend" 49.39
]
body_html = "\n".join(f'      <div id="bill-{k}" class="bill-body">    {h}</div>' for k, h, _, _, _ in bodies)
cap_html = "\n".join(f'    <div id="bill-c{k}" class="bill-cap">{c}</div>' for k, _, c, _, _ in bodies if c)
bill_js = []
for k, _, c, a, b in bodies:
    bill_js.append(f'    wipeIn("#bill-{k}", {a});')
    if c: bill_js.append(f'    wipeIn("#bill-c{k}", {a});')
    if b:
        bill_js.append(f'    wipeOut("#bill-{k}", {b});')
        if c: bill_js.append(f'    wipeOut("#bill-c{k}", {b});')
S["billing"] = scene("billing", """
    #bill-bg { position: absolute; inset: 0; background: #1d1b20; }
    #bill-card { position: absolute; left: 120px; top: 400px; width: 1680px; height: 420px; border-radius: 40px; background: #2b2930; }
    #bill-block { position: absolute; left: 190px; top: 500px; width: 1600px; height: 240px; font-size: 44px; line-height: 1.6; color: #e6e0e9; }
    #bill-open { position: absolute; left: 0; top: 0; white-space: pre; }
    #bill-block .k { color: #d0bcff; }
    .bill-body { position: absolute; left: 0; top: 70px; white-space: pre; }
    .bill-body .cm { color: #938f99; }
    #bill-close { position: absolute; left: 0; top: 140px; white-space: pre; }
    .bill-cap { position: absolute; left: 120px; top: 190px; font-size: 104px; font-weight: 700; letter-spacing: -0.02em; color: #d0bcff; white-space: nowrap; }
""", f"""
    <div id="bill-bg"></div>
    <div id="bill-card"></div>
{cap_html}
    <div id="bill-block" class="code" data-layout-allow-overlap>
      <div id="bill-open"><span class="k">onPurchase</span> = {{ plan -&gt;</div>
{body_html}
      <div id="bill-close">}}</div>
    </div>
""", """
    tl.fromTo("#bill-card", { y: 120, opacity: 0 }, { y: 0, opacity: 1, duration: 0.5, ease: "back.out(1.4)" }, 0);
    tl.fromTo("#bill-block", { y: 120, opacity: 0 }, { y: 0, opacity: 1, duration: 0.5, ease: "back.out(1.4)" }, 0.04);
""" + "\n".join(bill_js))

# 8. Theme (50.5-54.9): one phone, light → dark → dynamic color.
S["theme"] = scene("theme", """
    .theme-bg { position: absolute; inset: 0; }
    #theme-bg-light { background: #fef7ff; }
    #theme-bg-dark { background: #141218; }
    #theme-bg-dyn { background: #f8f9fa; }
    #theme-phone { left: 1180px; top: 90px; }
    .theme-shot { position: absolute; inset: 0; }
    #theme-words { position: absolute; left: 180px; top: 300px; }
    .theme-word { font-size: 110px; font-weight: 700; letter-spacing: -0.02em; line-height: 1.25; white-space: nowrap; color: #cac4d0; }
""", """
    <div id="theme-bg-light" class="theme-bg"></div>
    <div id="theme-bg-dark" class="theme-bg"></div>
    <div id="theme-bg-dyn" class="theme-bg"></div>
    <div id="theme-words">
      <div id="theme-w-light" class="theme-word">Light</div>
      <div id="theme-w-dark" class="theme-word">Dark</div>
      <div id="theme-w-dyn" class="theme-word">Dynamic color</div>
    </div>
    <div id="theme-phone" class="phone"><div class="screen">
      <img src="assets/img/04-picker-light.png" alt="Plan picker, light theme" />
      <img id="theme-shot-dark" class="theme-shot" src="assets/img/04-picker-dark.png" alt="Plan picker, dark theme" />
      <img id="theme-shot-dyn" class="theme-shot" src="assets/img/04-picker-dynamic.png" alt="Plan picker, dynamic color" />
    </div></div>
""", """
    const reveal = (sel, at) => tl.fromTo(sel, { clipPath: "inset(0 0 0 100%)" }, { clipPath: "inset(0 0 0 0%)", duration: 0.5, ease: "expo.inOut" }, at);
    tl.fromTo("#theme-phone", { y: 50, scale: 0.97 }, { y: 0, scale: 1, duration: 0.5, ease: "back.out(1.6)" }, 0);
    tl.fromTo("#theme-w-light", { color: "#cac4d0" }, { color: "#1d1b20", duration: 0.2 }, 0.15);    // "Light" 50.71
    reveal("#theme-bg-dark", 0.7); reveal("#theme-shot-dark", 0.7);                                   // "dark" 51.26
    tl.to("#theme-w-light", { color: "#49454f", duration: 0.2 }, 0.75);
    tl.to("#theme-w-dark", { color: "#e6e0e9", duration: 0.2 }, 0.8);
    reveal("#theme-bg-dyn", 1.85); reveal("#theme-shot-dyn", 1.85);                                   // "color" 52.40
    tl.to(["#theme-w-light", "#theme-w-dark"], { color: "#c4c6cf", duration: 0.2 }, 1.9);
    tl.to("#theme-w-dyn", { color: "#1a1c1e", duration: 0.2 }, 1.95);
""")

# 9. End card (54.9-63.0): the mark carried in, name, URL and install line; hold 3 s.
S["endcard"] = scene("endcard", """
    #end-bg { position: absolute; inset: 0; background: #eaddff; }
    #end-icon { position: absolute; left: 830px; top: 150px; width: 260px; height: 260px; border-radius: 60px; }
    #end-word { position: absolute; left: 0; right: 0; top: 450px; text-align: center; font-size: 112px; font-weight: 700; letter-spacing: -0.025em; color: #21005d; }
    #end-sub { position: absolute; left: 0; right: 0; top: 600px; text-align: center; font-size: 44px; font-weight: 500; color: #4a4458; }
    #end-url { position: absolute; left: 0; right: 0; top: 700px; text-align: center; font-size: 72px; font-weight: 700; color: #6750a4; letter-spacing: -0.01em; }
    #end-install { position: absolute; left: 0; right: 0; top: 840px; text-align: center; font-size: 32px; color: #1d1b20; }
    #end-install span { background: #ffffff; border-radius: 18px; padding: 16px 28px; }
""", """
    <div id="end-bg"></div>
    <img id="end-icon" src="assets/img/icon.svg" alt="M3 Expressive Paywall icon" />
    <div id="end-word">M3 Expressive Paywall</div>
    <div id="end-sub">Free and open source · MIT · Jetpack Compose</div>
    <div id="end-url">paywall.sanikant.dev</div>
    <div id="end-install" class="code"><span>implementation("com.github.sanikantv:m3-expressive-paywall:v1.0.0")</span></div>
""", """
    tl.fromTo("#end-icon", { y: 500, scale: 0.4, rotation: 90 }, { y: 0, scale: 1, rotation: 0, duration: 0.8, ease: "back.out(1.5)" }, 0.05);
    wipeIn("#end-word", 0.45, 0.5);
    wipeIn("#end-sub", 0.5);                          // "free" 55.30
    tl.fromTo("#end-url", { scale: 0.6, opacity: 0 }, { scale: 1, opacity: 1, duration: 0.5, ease: "back.out(2)" }, 2.1);   // "Get" 57.07
    wipeIn("#end-install", 2.9, 0.5);
""")

for name, html in S.items():
    pathlib.Path(f"compositions/{name}.html").write_text(html)
print("wrote", ", ".join(S))
