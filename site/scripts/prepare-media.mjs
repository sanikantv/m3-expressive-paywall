// Copies demo media from ../art into public/media in web-friendly formats.
// Run: node scripts/prepare-media.mjs   (needs ffmpeg on PATH for the video formats)
import { execFileSync } from 'node:child_process';
import { copyFileSync, mkdirSync, readdirSync, rmSync } from 'node:fs';
import { basename, join } from 'node:path';
import { fileURLToPath } from 'node:url';
import sharp from 'sharp';

const art = fileURLToPath(new URL('../../art/', import.meta.url));
const media = fileURLToPath(new URL('../public/media/', import.meta.url));
mkdirSync(join(media, 'screens'), { recursive: true });

// Hero video: MP4 + WebM for <video>, GIF as the no-video fallback, a poster frame.
copyFileSync(join(art, 'hero.mp4'), join(media, 'hero-demo.mp4'));
copyFileSync(join(art, 'hero.gif'), join(media, 'hero-demo.gif'));
const ffmpeg = (...args) => execFileSync('ffmpeg', ['-loglevel', 'error', '-y', ...args]);
ffmpeg('-i', join(art, 'hero.mp4'), '-c:v', 'libvpx-vp9', '-b:v', '0', '-crf', '40', '-row-mt', '1', '-an', join(media, 'hero-demo.webm'));
ffmpeg('-ss', '4', '-i', join(art, 'hero.mp4'), '-frames:v', '1', join(media, 'hero-poster.png'));
await sharp(join(media, 'hero-poster.png')).webp({ quality: 80 }).toFile(join(media, 'hero-poster.webp'));
rmSync(join(media, 'hero-poster.png'));

// Screenshots: 1080x2400 PNG -> 540x1200 WebP.
for (const file of readdirSync(join(art, 'screenshots')).filter((f) => f.endsWith('.png'))) {
  await sharp(join(art, 'screenshots', file))
    .resize({ width: 540 })
    .webp({ quality: 82 })
    .toFile(join(media, 'screens', `${basename(file, '.png')}.webp`));
}

copyFileSync(join(art, 'icon', 'icon.svg'), fileURLToPath(new URL('../public/favicon.svg', import.meta.url)));
copyFileSync(join(art, 'icon', 'icon-rounded-512.png'), join(media, 'icon-512.png'));
console.log('media ready');
