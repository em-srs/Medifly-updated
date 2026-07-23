# MediFly — design correction pass (round 2)

**Why this document exists:** the navbar and search bar from round 1 came out correctly. Everything below it drifted — the agent filled gaps in the original spec by defaulting to generic SaaS-landing-page and dashboard patterns instead of MediFly's actual design system. This document is a strict, exhaustive correction. It is self-contained — read this whole file before touching any code, and treat every "Remove" and "Replace" instruction as mandatory, not a suggestion.

Pair with `medifly-design-system-handoff.md` for the base tokens (colors, fonts, radius, etc.) — this document overrides that one wherever the two conflict, since this reflects what actually shipped and what's wrong with it.

---

## 1. The core problem, named directly

Every one of these is currently present on the live site and every one of them is a template default, not a MediFly-specific choice:

1. **Repeated card-grid rhythm.** The homepage currently has three back-to-back sections that are all the same shape: pastel-icon-square + bold heading + gray paragraph, repeated in a 3-card row, then a 4-card row, then a badge row. Three variations of the identical layout in a row is the single biggest tell that a page was generated rather than designed. A real page varies its rhythm — text-only section, then a visual section, then a grid, then something asymmetric.
2. **Two competing icon languages.** The navbar uses clean Tabler outline icons (thin line, monochrome, inherits color). The homepage cards use flat, colorful icon illustrations (a cartoon alarm clock, a colored shield, a snowflake with a gradient). These do not belong in the same product. Pick one icon language for the entire site and never mix in the other.
3. **The "bold black text + one word in bright accent color" headline.** "Medicines Delivered in **1-6** **Hours**" with "1-6" and "Hours" in teal is the most common pattern generative tools default to for hero headlines. It reads as templated specifically because it's the default, not because the technique is bad in the abstract.
4. **Generic stock photography.** The pharmacy-lab photo in the hero doesn't depict anything specific to MediFly — it's the kind of image that could illustrate literally any pharmacy or healthcare company's homepage. It adds no brand-specific information.
5. **Auto Refill and Prescription Vault look like a different, unrelated product.** Dark navy upsell box with a checklist, pricing-tier cards with a "Best value" ribbon, and a dashboard sidebar with a hamburger-style nav — this is boilerplate B2B SaaS pricing-page and admin-dashboard language. It has nothing to do with the quick-commerce-meets-pharmacy visual system built for the rest of the site (teal/coral, pill shapes, Sora/Inter type).
6. **Section titles that are marketing-generic rather than product-specific.** "Why Traditional Delivery Fails," "Built for Better Health," "Seamless Delivery in 4 Steps" are the kind of headline you'd get from any generic prompt like "write hero section copy for a delivery startup." None of them mention medicine, prescriptions, salts, or anything MediFly-specific.

None of this is a "taste" nitpick — each one is fixable with a specific, mechanical instruction below.

---

## 2. Banned patterns — check every page against this list

Do not use any of the following, anywhere on the site, under any circumstances:

- ❌ A headline where one or two words are set in a bright accent color while the rest is black/dark — this includes any variation like colored numbers, colored key phrases, etc.
- ❌ Flat/colorful "illustration-style" icons (rounded gradient squares behind a cute pictorial icon). Use **only** Tabler outline icons (`ti ti-*` classes), monochrome, inheriting `currentColor` or a single token color.
- ❌ Three or more consecutive sections that use the identical "icon + heading + paragraph, repeated in a grid" layout. If you need to communicate 3+ related points, vary the container: one as a horizontal scroller, one as a numbered list with a supporting visual, one as a single wide card — not three identical grids stacked vertically.
- ❌ Generic stock photography of unrelated pharmacies, labs, or generic "healthcare" imagery that isn't MediFly-specific. See §5 for what to use instead.
- ❌ Pricing-tier cards with a "Best value" or "Most popular" ribbon badge, unless the page is genuinely a pricing/plans page (Auto Refill's Plans & Pricing tab is fine to have tiers — see §7 — but keep the visual language matching the rest of the site, not a stock SaaS pricing template).
- ❌ A dark navy "upsell" box with a white checklist of benefits — this is boilerplate SaaS marketing chrome. Rebuild any such block using the site's actual card style (`var(--card)` background, `var(--border)` hairline, `var(--radius-md)`).
- ❌ Generic section headlines that could apply to any startup in any industry. Every headline on the site must reference something MediFly-specific: a medicine, a salt/composition, a prescription, a delivery window, a real Indian city, or a named feature (Auto Refill, Salt Comparison, Prescription Vault).
- ❌ A hamburger-menu-plus-sidebar admin-dashboard layout for consumer-facing pages (Auto Refill, Prescription Vault). Reserve sidebars for genuinely list-heavy management screens, and even then, restyle to match tokens — see §8.

---

## 3. Homepage — exact section list, nothing added beyond this

The homepage must contain **exactly these sections, in this order, and no others**:

1. **Navbar** (already correct — leave as is)
2. **Hero** — see §4 for exact copy and layout
3. **"Shop by health need"** — horizontal scroll of category tiles (already correct in the reference prototype — keep this)
4. **Trust row** — 4-card grid: licensed pharmacy / cold-chain packed / pharmacist checked / easy returns (already correct — keep this, it's the *one* grid the homepage is allowed to have)
5. **"Trending near you"** — product grid

**Do not add:** "Why Traditional Delivery Fails," "Built for Better Health," "Seamless Delivery in 4 Steps," a certification-badge row, testimonials, an app-download banner, or any other section not listed above. If there's a genuine business reason to communicate "why we're better than 2-day delivery," fold that single idea into the hero subheading instead of giving it its own full section — see §4.

If this list feels too sparse compared to what's currently built: it isn't. Zomato, Blinkit, and Swiggy's actual homepages are exactly this sparse — hero, categories, trust signals, products. The extra sections are the tell, not the fix.

---

## 4. Hero — exact copy and layout replacement

**Replace the current headline entirely.** Do not use any "bold text + colored word" construction. Use this instead:

> **Headline:** "Your medicine cabinet, restocked before you run out."
>
> **Subheading:** "Licensed pharmacies, cold-chain packaging, and a pharmacist check on every order — delivered across India in 1–6 hours."

Rationale: this headline is specific to the *behavior* MediFly solves (running out of medicine) rather than a generic speed claim, and it doesn't rely on the colored-word trick. All text in the headline is a single color/weight — differentiate emphasis through type size and line breaks, not color.

**Layout:**
- Keep the existing delivery-pill badge ("1–6 hour delivery" per the honesty correction from round 1) beneath the subheading.
- Keep "Order medicines" (primary, teal pill) and "Upload prescription" (secondary, outline pill) as the two CTAs.
- **Remove the stock photo entirely.** Replace the right-hand visual with one of these, in order of preference:
  1. A simple custom illustration built from the existing icon system — e.g. a large single Tabler icon (`ti-vaccine-bottle` or `ti-package` styled at 120px+) inside a soft `var(--teal-50)` rounded panel, with the "Live tracking — rider is 5 mins away" card overlapping the bottom-left corner exactly as currently designed (that card is good, keep it).
  2. A simple abstract composition using the existing design tokens only — overlapping soft-edged rounded rectangles in `var(--teal-100)`/`var(--coral-50)` suggesting a package/medicine strip shape, no photography.
  - Either option should feel like it was built *for MediFly specifically*, not licensed from a stock library.

---

## 5. Icon system — single source of truth

**Every icon on every page, with zero exceptions, must be a Tabler outline icon** (`<i class="ti ti-name"></i>`), monochrome, sized via `font-size`, colored via `color` or a CSS variable. This includes:
- Trust-row icons (already correct — confirm they weren't accidentally reverted)
- "Why traditional delivery fails" style cards — if any content from these survives into the trust row or elsewhere, convert their icons to Tabler
- Auto Refill's "Why Auto-Refill" checklist icons → `ti-check` in a small teal circle, not colorful checkmark graphics
- Prescription Vault's status icons → `ti-circle-check` (verified), `ti-clock` (pending) in teal/amber respectively
- Salt Comparison's numbered steps and "why compare" checklist → `ti-circle-check`, teal

If a design tool or the agent's default component library inserts a different icon set (Heroicons, Feather, Lucide, emoji, or flat colored SVG illustrations), remove it and replace with the Tabler equivalent. Search the codebase for any icon import that isn't `@tabler/icons` (or the Tabler webfont link) and eliminate it.

---

## 6. Rewrite every generic headline

Replace these specific headlines (found on the live site) since they're industry-generic rather than MediFly-specific:

| Current (generic) | Replace with |
|---|---|
| "Why Traditional Delivery Fails" | Delete this section entirely (see §3) |
| "Built for Better Health" | Delete this section entirely (see §3) |
| "Seamless Delivery in 4 Steps" | Delete this section entirely (see §3) |
| "Compare Two Medicines Side by Side" (Salt Comparison) | Keep the concept, tighten the copy: "Same salt, better price" as the eyebrow-style label already present is good — keep it; just ensure the H1 below it references salts/composition explicitly rather than generic "compare" language. Current H1 is acceptable, keep as is. |

General rule going forward: before finalizing any headline, check whether it would make sense on a page for an unrelated product (a shoe delivery app, a grocery app, a generic SaaS tool). If yes, it's too generic — rewrite it to reference medicines, salts, prescriptions, or a real delivery mechanic.

---

## 7. Auto Refill page — full restyle to match the design system

**Remove entirely:**
- The dark navy "Why Auto-Refill?" box with white checklist
- The pricing-tier cards with "Your plan" / "Best value" ribbon badges styled as a generic SaaS pricing table

**Rebuild using the existing tokens:**
- Page header: "Auto refill" (Sora, 24px) + one-line description in `--ink-soft`, exactly as currently built — this part is fine.
- Tab row (Active Subscriptions / Refill History / Plans & Pricing): keep the underline-tab pattern already used elsewhere on the site (product-detail tabs use this same pattern — reuse that exact component, don't build a new tab style).
- **Active subscription card:** keep the white card with checkmark + plan name + status badge — this part is already close to on-brand, just confirm the "ACTIVE" badge uses `var(--teal-50)`/`var(--teal-800)` text per the badge spec in the base handoff doc, not a generic green.
- **"Why Auto-Refill" content:** instead of a dark upsell box, use a plain white card (`var(--card)`, `var(--border)`, `var(--radius-md)`) with a `var(--teal-50)` icon circle + `ti-check` per line item, in the same visual language as the homepage trust row. No dark background, no white-on-navy text.
- **Plans & Pricing tab:** if you keep tiered pricing, style each tier as a plain white card with `1.5px solid var(--border)` — the currently-selected/current plan gets `2px solid var(--border-accent)`-equivalent (use `var(--teal-700)` border) plus a small badge using `background: var(--teal-50); color: var(--teal-800)` reading "Current plan" — not a black "YOUR PLAN" ribbon or green "BEST VALUE" ribbon in a different color system than the rest of the site.
- **Medicines in Auto-Refill list:** the current card-row style (icon, name, qty/frequency, next refill date, edit/delete icon buttons) is already on-brand — keep it as is.

---

## 8. Prescription Vault — full restyle to match the design system

**Remove:**
- The generic dashboard sidebar (Dashboard / Home / Vault / Orders / Profile / Settings) styled with a plain hamburger-adjacent look unrelated to the rest of the site's nav language.

**Rebuild:**
- If a left-hand patient list is genuinely useful here (multiple family members' prescriptions), keep the concept but restyle: white card container (`var(--card)`, `var(--border)`, `var(--radius-md)`), patient rows with avatar circles matching the `.avatar` style already used in the navbar/account pages (teal circle, white initials) — not the yellow/orange emoji-style avatars currently shown.
- Patient list should sit inside the same page shell as every other page (navbar + search capsule + page nav already at the top — confirm those aren't being suppressed on this page, since the screenshot shows them present, which is correct — just the sidebar below needs restyling).
- **Prescription cards:** current structure (doctor name, hospital, date issued, medicine count, Order Now / view / delete) is good — keep the layout. Just confirm:
  - "VERIFIED" badge → `var(--teal-50)`/`var(--teal-800)` text with a `ti-circle-check` icon
  - "PENDING" badge → `var(--amber-100)`/`#7a4e05` text with a `ti-clock` icon
  - "Order Now" button → teal pill per the shared button spec, not a plain rectangular button
  - Icon buttons (view/delete) → the shared `.icon-btn` circular style used elsewhere on the site, not plain unstyled icons

---

## 9. Salt Comparison — minor correction only

This page is the closest to on-brand already. Only fix:
- The numbered step circles (currently teal — good, keep them)
- The "Why compare salts?" green card and "Need help?" card are close to correct — confirm the green card uses `var(--teal-50)` background with `var(--teal-800)` text (not a generic light-green that doesn't match the token system), and the checkmarks are `ti-check` in teal, not a different icon set.
- No structural changes needed here beyond a token/icon audit.

---

## 10. Mandatory self-check before calling this done

Before reporting this task complete, go through every page and confirm:

- [ ] Every icon on every page is a Tabler outline icon (`ti ti-*`), no exceptions, no colored illustration-style icons anywhere
- [ ] The homepage has exactly the 5 sections listed in §3, nothing more
- [ ] No headline anywhere uses the "black text + one colored word" pattern
- [ ] No stock photography remains in the hero
- [ ] Auto Refill has no dark navy upsell box and no generic pricing-ribbon styling
- [ ] Prescription Vault's sidebar and avatars match the site's actual design tokens, not a generic dashboard template
- [ ] Every section headline references something MediFly-specific (a medicine, salt, prescription, delivery window, or named feature) rather than generic startup marketing language
- [ ] No page has 3+ consecutive sections using the identical icon-grid layout

If any box can't be checked, that page isn't done — fix it before moving to the next.
