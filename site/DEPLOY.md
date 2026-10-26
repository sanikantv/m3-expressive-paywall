# Deploying paywall.sanikant.dev

The site is static (Astro + Starlight) and runs on **Cloudflare Workers static assets**.
`wrangler.jsonc` maps the custom domain `paywall.sanikant.dev`; Cloudflare creates the DNS record
and the HTTPS certificate on the first deploy.

## Build locally

Needs Node 22.23+, JDK 17+ and the Android SDK (for the snippet compile and Dokka).

```bash
cd site
npm ci
npm run build:all   # compile :docs-snippets, regenerate Dokka into public/api, astro build
npm run check       # one H1, unique titles/descriptions, JSON-LD, sitemap, links, media
npm run preview     # http://localhost:4321
```

`npm run build` alone rebuilds the site without Gradle (uses the existing `public/api`).

## Deploy by hand

```bash
cd site
npx wrangler login     # once
npm run deploy         # wrangler deploy
```

## Deploy automatically from GitHub Actions

`.github/workflows/docs.yml` builds and checks the site on every push and pull request. It
deploys on pushes to `main` once these two repository secrets exist:

1. Cloudflare dashboard → **My Profile → API Tokens → Create Token** → template
   **Edit Cloudflare Workers**. Limit it to your account and the `sanikant.dev` zone.
2. GitHub → repo **Settings → Secrets and variables → Actions**:
   - `CLOUDFLARE_API_TOKEN`: the token from step 1
   - `CLOUDFLARE_ACCOUNT_ID`: shown on the Workers & Pages overview page

## Media and Open Graph images

- `node scripts/prepare-media.mjs`: copies the hero video, GIF and screenshots from `../art`
  into `public/media` (WebP/WebM). Needs ffmpeg.
- `./scripts/render-og.sh`: renders the 1200×630 cards in `public/og/` from `og-src/og.html`
  with headless Chrome.
- `python3 scripts/gen_components.py`: regenerates the component pages from their data.
