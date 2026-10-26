// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';

export default defineConfig({
  site: 'https://paywall.sanikant.dev',
  trailingSlash: 'always',
  integrations: [
    starlight({
      title: 'M3 Expressive Paywall',
      description:
        'Six Material 3 Expressive paywall screens for Jetpack Compose. Play-ready disclosures, light and dark, bring your own billing.',
      logo: { src: './src/assets/logo.svg', alt: 'M3 Expressive Paywall' },
      favicon: '/favicon.svg',
      social: [
        { icon: 'github', label: 'GitHub', href: 'https://github.com/sanikantv/m3-expressive-paywall' },
      ],
      editLink: { baseUrl: 'https://github.com/sanikantv/m3-expressive-paywall/edit/main/site/' },
      lastUpdated: true,
      disable404Route: true,
      customCss: ['./src/styles/custom.css'],
      routeMiddleware: './src/route-data.ts',
      components: {
        Footer: './src/components/Footer.astro',
        PageTitle: './src/components/PageTitle.astro',
      },
      head: [
        // Google Search Console ownership (keep, even after verification).
        {
          tag: 'meta',
          attrs: { name: 'google-site-verification', content: 'umeNRLlQKuAWDZaTioJXcfOPPrNz9d72Y4BkCPLYxlc' },
        },
        // Self-hosted Google Sans Flex (see src/styles/custom.css); preload the Latin file.
        {
          tag: 'link',
          attrs: {
            rel: 'preload',
            href: '/fonts/google-sans-flex-latin.woff2',
            as: 'font',
            type: 'font/woff2',
            crossorigin: '',
          },
        },
        // Demo videos: the poster <img> paints first; after load, visible ones are swapped for a
        // muted looping video. With reduced motion they stay on the poster.
        {
          tag: 'script',
          content: `addEventListener('load',()=>{if(matchMedia('(prefers-reduced-motion: reduce)').matches)return;const io=new IntersectionObserver(es=>es.forEach(e=>{if(!e.isIntersecting)return;io.unobserve(e.target);const f=e.target,img=f.querySelector('img'),b=f.dataset.video,v=document.createElement('video');v.muted=v.loop=v.playsInline=v.autoplay=true;v.width=270;v.height=600;v.poster=img.src;v.setAttribute('aria-label',img.alt);v.innerHTML='<source src="'+b+'.webm" type="video/webm"><source src="'+b+'.mp4" type="video/mp4">';v.addEventListener('playing',()=>img.replaceWith(v),{once:true});v.style.position='absolute';v.style.opacity='0';f.append(v);v.play().catch(()=>{});v.addEventListener('playing',()=>{v.style.position='';v.style.opacity=''},{once:true})}));document.querySelectorAll('[data-video]').forEach(f=>io.observe(f))})`,
        },
        { tag: 'meta', attrs: { name: 'theme-color', content: '#6750A4' } },
      ],
      expressiveCode: {
        themes: ['github-dark-default', 'github-light-default'],
        styleOverrides: { borderRadius: '0.75rem' },
      },
      sidebar: [
        { label: 'Start here', items: ['getting-started', 'comparison', 'faq', 'changelog'] },
        {
          label: 'Paywalls',
          items: [
            'components',
            'components/classic-plans-paywall',
            'components/comparison-paywall',
            'components/trial-timeline-paywall',
            'components/plan-picker-paywall',
            'components/limited-offer-sheet',
            'components/onboarding-paywall',
            'components/paywall-plan',
          ],
        },
        { label: 'Guides', items: [{ autogenerate: { directory: 'guides' } }] },
        { label: 'API reference', link: '/api/', attrs: { target: '_self' } },
      ],
    }),
  ],
});
