GOAL:    An Android developer leaves knowing they can add a Play-ready Material 3 Expressive paywall
         by passing plans and three callbacks, then visits paywall.sanikant.dev.
OUTPUT:  HyperFrames, 1920 x 1080, 30 fps
TIME:    63 s x 30 = 1890 frames. Voice (Liam, eleven_multilingual_v2) starts at 0.4 s and is the clock.
LOOK:    grounds are the app's own Material 3 surfaces: #FEF7FF surface, #F3EDF7 surfaceContainer,
         #EADDFF primaryContainer, #1D1B20 dark surface (with full-frame content, never a void).
         Ink #1D1B20 / #E6E0E9 on dark. Accent #6750A4 only on: the highlighted word, checks, the mark.
         Display: Google Sans Flex 700, 104 px, tracking -2%. Body: Google Sans Flex 500, 46 px.
         Code: Google Sans Code 500, 31 px, line-height 1.45.
         Medium: real app screenshots inside a phone: bezel #1D1B20 14 px, radius 66 px, 900 px tall
         (83% of frame height); shadow 0 30px 60px rgba(29,27,32,.22). Shapes are MaterialShapes cookies.
         No grain, no vignette, no gradients, no glow.
OBJECTS: phone (the demo app on the Medium Phone emulator), the Play checklist (4 rows, cookie bullets),
         the quick-start code card (white sheet, 32 px radius), the app icon (purple square, lavender
         cookie, sparkle).
MOTION:  enter: back.out(1.7) on scale/position (M3 Expressive spring feel), 0.5 s;
         move/camera: expo.inOut 0.6 s; text: clip-path wipe left→right 0.35 s, never fade-slide;
         exit: power2.in 0.25 s. Stagger 4–6 frames.
SHOTS:   0.0–3.4 hook: phone with the gallery fills left half; "Your app is ready." wipes in on "ready",
         "The paywall isn't." replaces it on "paywall"; the phone screen dims to an empty card.
         3.4–12.5 rules: dark sheet slides up; title "Google Play's paywall rules" on "rules";
         rows Price / Billing period / Trial terms / Auto-renew note pop on their spoken words.
         12.5–15.4 reveal: lavender world; icon springs in on "Meet", wordmark wipes in. Bass impact.
         15.4–32.7 six: phone centre with neighbours peeking; advances on each design's name; label
         under the phone is the composable's name in Google Sans Code; picker beat plays the real
         recording.
         32.7–37.6 code: the real QuickStart.kt snippet; plans line lights on "plans", the three
         callbacks on "three callbacks"; on "whole" the card shrinks into the phone.
         37.6–44.3 disclosure (payoff): punch-in at 1.7x on the real footer of the classic paywall;
         disclosure line highlighted on "disclosure", links on "Restore"; the four checklist rows
         return checked.
         44.3–50.5 billing: one lambda `onPurchase = { plan -> … }`, body swaps on "Google",
         "RevenueCat", "backend".
         50.5–54.9 theme: one phone wipes light → dark → dynamic on the spoken words.
         54.9–63.0 end card: icon carried in to centre, wordmark wipes, URL + install line, hold 3 s.
BANNED:  dark void with floating elements; glow; purple gradients; glass cards; three cards in a row;
         fading-and-sliding-up everything; text moving while read; docked logo; invented UI;
         the logo before the reveal; Telegram.
QA:      frames 0.5, 2.8, 8.0, 13.8, 22.0, 27.5, 34.5, 40.0, 42.5, 47.0, 52.5, 61.0.
