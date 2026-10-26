// Adds per-page Open Graph images and JSON-LD. Starlight already writes title, description,
// canonical, og:title/description/url/type and twitter:card from each page's frontmatter.
import { defineRouteMiddleware } from '@astrojs/starlight/route-data';
import { AUTHOR, AUTHOR_NAME, HUB, MATERIAL3, REPO, SITE_URL, VERSION } from './lib/site';

const SECTIONS: Record<string, string> = { components: 'Paywalls', guides: 'Guides' };
const DEFAULT_KEYWORDS = ['jetpack compose paywall', 'material 3 expressive', 'compose subscription screen'];

export const onRequest = defineRouteMiddleware((context) => {
  const route = context.locals.starlightRoute;
  const { entry, head, id } = route;
  const data = entry.data as typeof entry.data & {
    faq?: { q: string; a: string }[];
    keywords?: string[];
    ogImage?: string;
  };
  const url = new URL(context.url.pathname, SITE_URL).href;
  const section = id.split('/')[0];

  const ogImage = new URL(
    data.ogImage ?? (id === '' || id === 'index' ? '/og/home.png' : SECTIONS[section] ? `/og/${section}.png` : '/og/docs.png'),
    SITE_URL,
  ).href;
  head.push(
    { tag: 'meta', attrs: { name: 'author', content: AUTHOR_NAME }, content: '' },
    { tag: 'link', attrs: { rel: 'author', href: HUB }, content: '' },
    { tag: 'meta', attrs: { name: 'keywords', content: (data.keywords ?? DEFAULT_KEYWORDS).join(', ') }, content: '' },
    { tag: 'meta', attrs: { property: 'og:image', content: ogImage }, content: '' },
    { tag: 'meta', attrs: { property: 'og:image:width', content: '1200' }, content: '' },
    { tag: 'meta', attrs: { property: 'og:image:height', content: '630' }, content: '' },
    { tag: 'meta', attrs: { name: 'twitter:image', content: ogImage }, content: '' },
  );

  const graph: object[] = [];
  if (id === '' || id === 'index') {
    graph.push({
      '@type': 'SoftwareSourceCode',
      name: 'M3 Expressive Paywall',
      description: data.description,
      url: SITE_URL,
      codeRepository: REPO,
      programmingLanguage: 'Kotlin',
      runtimePlatform: 'Android',
      version: VERSION,
      license: 'https://opensource.org/licenses/MIT',
      keywords: ['Jetpack Compose', 'paywall', 'Material 3 Expressive', `material3 ${MATERIAL3}`, 'subscription'],
      author: AUTHOR,
    });
    graph.push({
      '@type': 'VideoObject',
      name: 'M3 Expressive Paywall: intro',
      description:
        'A 63-second narrated tour of M3 Expressive Paywall: six Material 3 Expressive paywall screens for Jetpack Compose, the Google Play disclosure footer, and the plans-plus-three-callbacks API.',
      thumbnailUrl: `${SITE_URL}/media/intro-poster.webp`,
      contentUrl: `${SITE_URL}/media/intro.mp4`,
      uploadDate: '2026-10-26',
      duration: 'PT1M3S',
      inLanguage: 'en',
      author: AUTHOR,
    });
  } else {
    const crumbs = [{ name: 'Home', url: `${SITE_URL}/` }];
    if (SECTIONS[section]) crumbs.push({ name: SECTIONS[section], url: `${SITE_URL}/${section}/` });
    crumbs.push({ name: data.title, url });
    graph.push({
      '@type': 'BreadcrumbList',
      itemListElement: crumbs.map((c, i) => ({ '@type': 'ListItem', position: i + 1, name: c.name, item: c.url })),
    });
  }
  if (section === 'guides') {
    head.push({ tag: 'meta', attrs: { property: 'article:author', content: HUB }, content: '' });
    graph.push({
      '@type': 'TechArticle',
      headline: data.title,
      description: data.description,
      url,
      author: AUTHOR,
      proficiencyLevel: 'Beginner',
      dependencies: `Jetpack Compose, androidx.compose.material3 ${MATERIAL3}`,
      keywords: data.keywords,
    });
  }
  if (data.faq?.length) {
    graph.push({
      '@type': 'FAQPage',
      mainEntity: data.faq.map(({ q, a }) => ({ '@type': 'Question', name: q, acceptedAnswer: { '@type': 'Answer', text: a } })),
    });
  }
  head.push({
    tag: 'script',
    attrs: { type: 'application/ld+json' },
    content: JSON.stringify({ '@context': 'https://schema.org', '@graph': graph }),
  });
});
