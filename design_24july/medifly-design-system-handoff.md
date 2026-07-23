# MediFly — visual redesign handoff

**Purpose of this document:** apply the new visual design to the existing MediFly codebase (React frontend, Java/Spring Boot backend) without changing functionality, routes, or data flow — only markup structure where needed to support the new components, plus CSS/styling and copy tone. Pair this doc with `medifly-redesign.html` (static reference prototype, all screens) — treat that file as the visual source of truth and this doc as the implementation instructions.

Positioning correction baked into this spec: MediFly delivers in **1–6 hours** (hyperlocal, not instant quick-commerce). All copy below reflects that — do not use "10-15 min" style claims anywhere in the real app.

---

## 1. Design tokens

Add these as CSS custom properties at the root (e.g. `:root` in your global stylesheet or `index.css`):

```css
:root {
  /* Brand */
  --teal-900:#04342C; --teal-800:#085041; --teal-700:#0F6E56; --teal-500:#1D9E75; --teal-300:#5DCAA5; --teal-100:#CDEFE1; --teal-50:#EAF8F2;
  --coral-700:#B84B24; --coral-600:#D85A30; --coral-500:#E2703A; --coral-100:#FBE1D4; --coral-50:#FDF0E8;
  --amber-500:#E2A233; --amber-100:#FBEBCF;
  --red-500:#D64545;

  /* Neutrals */
  --ink:#12211D; --ink-soft:#4B5C57; --ink-mute:#89968F;
  --bg:#F6FAF8; --card:#FFFFFF; --border:#E3ECE7;

  /* Shape & elevation */
  --radius-sm:10px; --radius-md:16px; --radius-lg:22px; --radius-pill:999px;
  --shadow: 0 2px 10px rgba(8,80,65,0.06), 0 1px 2px rgba(8,80,65,0.05);
}
```

**Usage rules:**
- `--teal-700` = primary brand color: nav accents, primary buttons, links, active states.
- `--coral-600` = single accent reserved for urgency/CTA moments (add-to-cart, delivery badges, offers). Don't let it compete with teal — one accent color visible per screen at a time is the ceiling.
- `--ink` for headings/primary text, `--ink-soft` for body copy, `--ink-mute` for placeholders/captions.
- `--bg` is the page canvas; `--card` is every raised surface (cards, modals, nav).

## 2. Typography

Add to `index.html` `<head>` or your font-loading setup:

```html
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Sora:wght@400;600;700;800&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
```

- **Sora** — all headings (`h1`–`h3`), the logo wordmark, prices, big numbers. Weights used: 600 (subheads), 700 (headings), 800 (hero only).
- **Inter** — everything else: body text, labels, buttons, form inputs, nav.
- Never mix a third typeface in. No default browser serif anywhere.

## 3. Icons

Replace any existing icon set with **Tabler Icons** (outline style only):

```html
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@2.44.0/tabler-icons.min.css">
```

Usage: `<i class="ti ti-pill"></i>`. Standard inline size 16–20px, decorative icons in category tiles 24–26px. Category icon key already used in the prototype (reuse for consistency):

| Section | Icon class |
|---|---|
| Fever & cold | `ti-thermometer` |
| Cough | `ti-lungs` |
| Diabetes | `ti-droplet` |
| Heart care | `ti-heart` |
| Baby care | `ti-baby-bottle` |
| Lab tests | `ti-flask` |
| Skin care | `ti-friction` |
| Devices | `ti-device-watch` |
| Prescription upload | `ti-prescription` / `ti-cloud-upload` |
| Cart | `ti-shopping-cart` |
| Delivery / speed | `ti-bolt` |
| Trust / verified | `ti-shield-check` |

## 4. Signature motif — capsule shape

The recurring visual signature across the whole product: **pill/capsule shapes** (search bars, nav, buttons, badges) — a literal nod to medicine strips. Apply `border-radius: var(--radius-pill)` to:
- The search input
- All primary/secondary buttons
- Category filter chips
- Delivery-time and Rx badges
- The bottom-sheet/sticky add-to-cart bar's CTA

Cards and content containers stay at `var(--radius-md)` (16px) — reserve the full pill shape for interactive controls and badges so it keeps its meaning instead of becoming decoration everywhere.

## 5. Page-by-page instructions

For each page, locate the corresponding component/route in the codebase and restyle per the notes below. Structural HTML changes should be minimal — mostly class/style additions — unless a note explicitly says to add a new element.

### Home / landing
- Hero band: `linear-gradient(120deg, var(--teal-800), var(--teal-700) 60%, var(--teal-500))`, white text, Sora headline ≤ 34px.
- Replace any "instant delivery" copy with a delivery-promise pill: *"Delivered in 1–6 hours"* with `<i class="ti ti-bolt">`, pill shape, `background: var(--teal-800)`/white text on the hero, or `var(--teal-50)`/`var(--teal-800)` text elsewhere.
- Add (or restyle existing) "shop by health need" horizontal scroll of category tiles — 104px wide cards, icon in a 48px teal-tinted rounded square, label below.
- Trust row: 4-up grid — licensed pharmacy / cold-chain packed / pharmacist checked / easy returns. Use `ti-shield-check`, `ti-snowflake`, `ti-user-check`, `ti-rotate`.
- Product grid below: see "Product card" spec in section 6.

### Medicine search / listing
- Top: category name + result count, horizontal filter-pill row (category, form, price, prescription-only, in-stock).
- Left rail (desktop) or a filter drawer (mobile): checkbox groups for brand / price / availability, `var(--card)` background, `var(--radius-md)`.
- Main area: product grid, same card spec as home.

### Product detail
- Image area in a `var(--radius-lg)` container, `var(--teal-50)` background if no product photography exists yet.
- Title in Sora 24px, manufacturer line in `--ink-mute`.
- Badge row: stock status (`var(--teal-50)`/`var(--teal-800)` text) and, where relevant, an amber "Prescription required" badge (`var(--amber-100)`/`#7a4e05` text).
- Price block: current price large (Sora 700, 24-28px), MRP struck through in `--ink-mute`, discount % in `--coral-600`.
- Tabs: Description / Uses / Side effects / Substitutes — underline-style active state in `--teal-700`.
- Sticky bottom bar (mobile) or fixed action panel (desktop): total price + "Add to cart" pill button, `var(--teal-700)` background, white text.

### Cart
- Address strip at top: teal-tinted, address + "Change" link right-aligned.
- Line items: thumbnail, name + pack size, quantity stepper (pill-shaped, teal fill), price right-aligned.
- Coupon strip: coral-dashed border, ticket icon, applied-coupon copy.
- Bill summary panel (sticky on desktop, bottom sheet on mobile): itemized rows, dashed divider before total, full-width "Proceed to checkout" pill button.

### Checkout
- Stack of cards: delivery address → payment method → order summary. One card per concern, `var(--card)` background, `var(--radius-md)`, 20px padding.
- Payment method as a row of selectable tiles (UPI / card / COD / net banking), selected state = teal border + `var(--teal-50)` fill.
- Replace any "instant" ETA claims with an ETA pill: *"Arriving in 1–6 hours"* or a specific time estimate once available from the backend.

### Order tracking
- Horizontal stepper: Confirmed → Packed → Out for delivery → Delivered, connected by a thin line, completed steps filled teal, pending steps gray.
- Delivery partner card: avatar, name, distance/ETA, call + message icon buttons.
- If no live map integration exists yet, use a placeholder panel (`var(--teal-50)` background, map-pin icon) rather than blocking the redesign on that integration.

### Account / profile
- Left column: profile summary card + vertical menu list (orders, addresses, saved prescriptions, payment methods, help, log out), each row with a leading icon and trailing chevron.
- Right column: recent orders as horizontal cards with a "Reorder" pill button.

## 6. Shared component specs

**Product card**
```
white card, var(--radius-md), 1px var(--border)
├─ thumbnail (var(--teal-50) bg, centered icon or product image)
│   ├─ top-left: "Rx" badge if prescription required (amber)
│   └─ top-right: delivery-time badge (teal-800 bg, white text, bolt icon)
├─ name (Inter 600, 13.5px, 2-line clamp)
├─ pack size / brand (Inter 400, 11.5px, --ink-mute)
├─ price row: current price (Sora 700) + struck-through MRP (--ink-mute)
└─ Add button, right-aligned: outlined pill → fills teal on hover/tap;
   becomes a qty stepper (− / count / +) once added to cart
```

**Badges**
- Rx required: `background: var(--amber-100); color: #7a4e05`
- In stock / verified: `background: var(--teal-50); color: var(--teal-800)`
- Delivery/urgency: `background: var(--teal-800); color: #fff`

**Buttons**
- Primary: `background: var(--teal-700); color: #fff; border-radius: var(--radius-pill)`, hover darkens to `--teal-800`.
- Secondary/outline: transparent bg, `1.5px solid var(--teal-700)` border, teal text; fills teal + white text on hover.
- Never more than one primary (solid) button visible per screen — everything else is secondary/outline or a plain icon button.

## 7. Navbar — Airbnb-style, two rows

Replace the existing single-row navbar with a two-row layout modeled on Airbnb's: a top row with logo + centered mode-tabs + account/cart on the right, and a second row with a pill-shaped mega search bar.

**Mapping logic:** Airbnb's Homes / Experiences / Services become MediFly's **Shop / Auto refill / Prescription vault** — the three distinct product modes, with "New" badges on the two newer ones. Salt comparison and About move to plain text links since they're secondary, not modes. Airbnb's Where / When / Who search fields become **Medicine or salt / Deliver to / Need it by**.

```html
<div class="navbar">
  <div class="nav-row1">
    <div class="logo"><i class="ti ti-vaccine-bottle"></i>MediFly</div>
    <div class="nav-tabs">
      <button class="nav-tab active"><i class="ti ti-shopping-bag"></i><span>Shop</span></button>
      <button class="nav-tab"><span class="tab-badge">New</span><i class="ti ti-refresh"></i><span>Auto refill</span></button>
      <button class="nav-tab"><span class="tab-badge">New</span><i class="ti ti-folder"></i><span>Prescription vault</span></button>
    </div>
    <div class="nav-right">
      <a class="nav-link" href="/salt-comparison">Salt comparison</a>
      <a class="nav-link" href="/about">About</a>
      <button class="icon-btn"><i class="ti ti-shopping-cart"></i><span class="cart-count">0</span></button>
      <div class="account-pill"><i class="ti ti-menu-2"></i><div class="avatar">SK</div></div>
    </div>
  </div>
  <div class="nav-row2">
    <div class="search-capsule">
      <div class="search-seg">
        <span class="seg-label">Medicine or salt</span>
        <span class="seg-value">Search medicines, salts, brands</span>
      </div>
      <div class="seg-divider"></div>
      <div class="search-seg">
        <span class="seg-label">Deliver to</span>
        <span class="seg-value">Mohali, Punjab</span>
      </div>
      <div class="seg-divider"></div>
      <div class="search-seg">
        <span class="seg-label">Need it by</span>
        <span class="seg-value">As soon as possible</span>
      </div>
      <button class="search-btn"><i class="ti ti-search"></i></button>
    </div>
  </div>
</div>
```

```css
.navbar{ position:sticky; top:0; z-index:50; background:var(--card); border-bottom:1px solid var(--border); }

.nav-row1{ max-width:1180px; margin:0 auto; padding:16px 24px 0; display:flex; align-items:center; gap:24px; }
.nav-tabs{ flex:1; display:flex; justify-content:center; gap:40px; }
.nav-tab{
  display:flex; flex-direction:column; align-items:center; gap:6px; font-size:13px; font-weight:600; color:var(--ink-mute);
  padding-bottom:14px; border-bottom:2px solid transparent; position:relative; background:none; border-top:none; border-left:none; border-right:none;
}
.nav-tab .ti{ font-size:22px; }
.nav-tab.active{ color:var(--ink); border-bottom-color:var(--ink); }
.nav-tab:not(.active):hover{ color:var(--ink-soft); }
.tab-badge{
  position:absolute; top:-4px; right:-16px; background:var(--teal-700); color:#fff; font-size:9px; font-weight:700;
  padding:2px 6px; border-radius:var(--radius-pill); letter-spacing:.02em;
}
.nav-right{ display:flex; align-items:center; gap:18px; flex-shrink:0; padding-bottom:14px; }
.nav-link{ font-size:13.5px; font-weight:600; color:var(--ink-soft); text-decoration:none; white-space:nowrap; }
.nav-link:hover{ color:var(--ink); }
.account-pill{
  display:flex; align-items:center; gap:10px; border:1px solid var(--border); border-radius:var(--radius-pill);
  padding:6px 6px 6px 14px; cursor:pointer;
}
.account-pill .ti{ font-size:16px; color:var(--ink-soft); }

.nav-row2{ max-width:660px; margin:0 auto; padding:14px 24px 18px; }
.search-capsule{
  display:flex; align-items:center; background:var(--card); border:1px solid var(--border);
  border-radius:var(--radius-pill); box-shadow:var(--shadow); padding:6px;
}
.search-seg{ flex:1; padding:8px 22px; border-radius:var(--radius-pill); cursor:pointer; min-width:0; }
.search-seg:hover{ background:var(--bg); }
.seg-label{ display:block; font-size:12px; font-weight:700; color:var(--ink); }
.seg-value{ display:block; font-size:12.5px; color:var(--ink-mute); margin-top:1px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }
.seg-divider{ width:1px; height:28px; background:var(--border); flex-shrink:0; }
.search-btn{
  width:40px; height:40px; border-radius:50%; background:var(--coral-600); color:#fff; font-size:16px; border:none;
  display:flex; align-items:center; justify-content:center; flex-shrink:0; cursor:pointer;
}
@media(max-width:820px){
  .nav-link{ display:none; }
  .nav-tabs{ gap:22px; }
  .nav-tab span:last-child{ display:none; } /* icon-only tabs on small screens */
}
```

**Behavior notes for Antigravity:**
- Clicking a mode tab sets `.active` on itself and removes it from the others (swap `border-bottom-color` + text color, same as the existing route-active styling in the app already does for `Home`).
- Each `.search-seg` should open the relevant control on click: medicine/salt autocomplete, an address picker, and a "now vs scheduled" toggle respectively — reuse whatever input components already back the current search bar, just restyle them into these three segments.
- On mobile, collapse `nav-row2` into a single tappable pill that opens a full-screen search sheet (standard Airbnb mobile pattern) rather than trying to fit three segments in one row — this is a bigger change, flag it separately if you want it built out.

## 8. Animated pill indicator (page nav + search segments)

Both the page/tab navigation and the search-bar segments use a shared pattern: an absolutely positioned pill `<div>` sits behind the buttons/segments, and JS slides it to the active one's position on click, briefly blurring mid-motion so it reads as a soft morph rather than a hard jump.

```css
.pill-indicator, .seg-indicator{
  position:absolute; border-radius:var(--radius-pill); z-index:0;
  transition:left .4s cubic-bezier(.3,0,.1,1), top .4s cubic-bezier(.3,0,.1,1),
             width .4s cubic-bezier(.3,0,.1,1), height .4s cubic-bezier(.3,0,.1,1), filter .35s ease-out;
}
.pill-indicator{ background:var(--teal-700); }
.seg-indicator{ background:var(--teal-50); opacity:0; }
.seg-indicator.show{ opacity:1; }
.pill-indicator.blurring, .seg-indicator.blurring{ filter:blur(6px); }
```

```js
function moveIndicator(indicatorEl, targetEl, blurMs){
  indicatorEl.style.left = targetEl.offsetLeft + 'px';
  indicatorEl.style.top = targetEl.offsetTop + 'px';
  indicatorEl.style.width = targetEl.offsetWidth + 'px';
  indicatorEl.style.height = targetEl.offsetHeight + 'px';
  indicatorEl.classList.add('blurring');
  clearTimeout(indicatorEl._t);
  indicatorEl._t = setTimeout(() => indicatorEl.classList.remove('blurring'), blurMs);
}
```

Call `moveIndicator(pillEl, activeButton, 420)` whenever the active nav tab changes, and `moveIndicator(segIndicatorEl, clickedSegment, 380)` whenever a search segment is clicked. The container (`.pagenav` / `.search-capsule`) must have `position: relative` so `offsetLeft`/`offsetTop` resolve correctly. On first render, set the indicator's position with `transition: none`, apply it, then clear the inline `transition` on the next animation frame — otherwise it visibly slides in from the top-left corner on page load.



## 9. Copy tone

- Sentence case everywhere — no Title Case, no ALL CAPS.
- Say what happened, not how the system works: "Order confirmed", not "Order status: CONFIRMED".
- Delivery promises stay honest to the 1–6 hour model — avoid "instant", "in minutes", or countdown-style urgency copy borrowed from food-delivery apps.
- Prescription/medical copy stays plain and reassuring, not salesy: "Pharmacist checked", "Licensed pharmacy", "Cold-chain packed".

## 10. What not to change

- No changes to routing, API calls, state management, or business logic — this is a visual/CSS/markup pass only.
- Don't introduce new dependencies beyond the two Google Fonts and the Tabler icon webfont linked above.
- Keep existing form validation and prescription-upload logic intact; only restyle the surrounding UI.

---

**Reference file:** `medifly-redesign.html` — open it in a browser and click through the page-switcher pills at the top (Home / Browse / Product / Cart / Checkout / Track order / Account) to see every screen fully built out in this system. Antigravity can inspect that file's CSS directly for exact values if anything above is ambiguous.
