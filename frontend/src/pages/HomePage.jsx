import { Link } from 'react-router-dom';
import { useCart } from '@/context/CartContext';
import styles from './HomePage.module.css';
import useScrollReveal from '@/hooks/useScrollReveal';

/* ── Category data for "Shop by health need" tiles ─── */
const CATEGORIES = [
  { icon: 'ti-thermometer',   label: 'Fever & cold' },
  { icon: 'ti-lungs',         label: 'Cough' },
  { icon: 'ti-droplet',       label: 'Diabetes' },
  { icon: 'ti-heart',         label: 'Heart care' },
  { icon: 'ti-baby-bottle',   label: 'Baby care' },
  { icon: 'ti-flask',         label: 'Lab tests' },
  { icon: 'ti-friction',      label: 'Skin care' },
  { icon: 'ti-device-watch',  label: 'Devices' },
];

/* ── Trust row data ──────────────────────────────────── */
const TRUST_ITEMS = [
  { icon: 'ti-shield-check', title: 'Licensed pharmacy',   desc: 'Every seller verified' },
  { icon: 'ti-snowflake',    title: 'Cold-chain packed',   desc: 'For temperature-sensitive meds' },
  { icon: 'ti-user-check',   title: 'Pharmacist checked',  desc: 'Every order reviewed' },
  { icon: 'ti-rotate',       title: 'Easy returns',        desc: 'On wrong or damaged items' },
];

/* ── Trending products (placeholder) ─────────────────── */
const TRENDING = [
  { id: 1, name: 'Dolo 650mg',          sub: 'Micro Labs · 15 tablets',   price: 32,  mrp: 35,  rx: false },
  { id: 2, name: 'Pan 40mg',            sub: 'Alkem · 15 tablets',        price: 68,  mrp: 79,  rx: false },
  { id: 3, name: 'Azithral 500mg',      sub: 'Alembic · 3 tablets',       price: 95,  mrp: 109, rx: true },
  { id: 4, name: 'Shelcal 500mg',       sub: 'Torrent · 15 tablets',      price: 117, mrp: 132, rx: false },
  { id: 5, name: 'Crocin Advance 500mg', sub: 'GSK · 20 tablets',         price: 28,  mrp: 30,  rx: false },
  { id: 6, name: 'Metformin 500mg',     sub: 'USV · 20 tablets',          price: 22,  mrp: 28,  rx: true },
  { id: 7, name: 'Combiflam',           sub: 'Sanofi · 20 tablets',       price: 42,  mrp: 47,  rx: false },
  { id: 8, name: 'Augmentin 625mg',     sub: 'GSK · 10 tablets',          price: 195, mrp: 218, rx: true },
];


export default function HomePage() {
  const heroRef  = useScrollReveal(0.05);
  const catRef   = useScrollReveal();
  const trustRef = useScrollReveal();
  const prodRef  = useScrollReveal();

  return (
    <div className={styles.pageWrap}>

      {/* ─── 1. HERO ─────────────────────────────────────── */}
      <section className={styles.hero} ref={heroRef}>
        <div className={styles.heroInner}>
          <div className={styles.heroContent}>
            <h1 className={styles.heroTitle}>
              Your medicine cabinet, restocked before you run out.
            </h1>
            <p className={styles.heroSub}>
              Licensed pharmacies, cold-chain packaging, and a pharmacist check
              on every order — delivered under 30 mins or on your schedule.
            </p>
            <div className={styles.deliveryPill}>
              <span className={styles.pulseDot} />
              <i className="ti ti-bolt" />
              Quick delivery under 30 mins
            </div>
            <div className={styles.heroBtns}>
              <Link to="/medicines" className={styles.btnPrimary}>
                <i className="ti ti-shopping-cart" /> Order medicines
              </Link>
              <Link to="/prescriptions" className={styles.btnOutline}>
                <i className="ti ti-cloud-upload" /> Upload prescription
              </Link>
            </div>
          </div>

          <div className={styles.heroVisual}>
            <div className={styles.heroIllustration}>
              <div className={styles.illustBg1} />
              <div className={styles.illustBg2} />
              <i className="ti ti-vaccine-bottle" />
            </div>
            <div className={styles.floatingCard}>
              <div className={styles.floatingDot} />
              <div className={styles.floatingText}>
                <strong>Live tracking</strong>
                <span>Rider is 5 mins away · Bandra West</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* ─── 2. SHOP BY HEALTH NEED ─────────────────────── */}
      <section className={styles.section} ref={catRef}>
        <div className={styles.container}>
          <div className={styles.sectionHead}>
            <h2>Shop by health need</h2>
          </div>
          <div className={styles.catScroll}>
            {CATEGORIES.map(({ icon, label }) => (
              <Link to="/medicines" key={label} className={styles.catCard}>
                <div className={styles.catIcon}>
                  <i className={`ti ${icon}`} />
                </div>
                <span>{label}</span>
              </Link>
            ))}
          </div>
        </div>
      </section>

      {/* ─── 3. TRUST ROW ───────────────────────────────── */}
      <section className={styles.section} ref={trustRef}>
        <div className={styles.container}>
          <div className={styles.trustRow}>
            {TRUST_ITEMS.map(({ icon, title, desc }) => (
              <div className={styles.trustCard} key={title}>
                <div className={styles.trustIcon}>
                  <i className={`ti ${icon}`} />
                </div>
                <div>
                  <b>{title}</b>
                  <p>{desc}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ─── 4. TRENDING NEAR YOU ────────────────────────── */}
      <section className={styles.section} ref={prodRef}>
        <div className={styles.container}>
          <div className={styles.sectionHead}>
            <h2>Trending near you</h2>
            <Link to="/medicines" className={styles.viewAll}>View all</Link>
          </div>
          <div className={styles.prodGrid}>
            {TRENDING.map((med) => (
              <div className={styles.prodCard} key={med.id}>
                <div className={styles.prodThumb}>
                  <i className="ti ti-pill" />
                  {med.rx && <span className={styles.rxBadge}>Rx</span>}
                  <span className={styles.deliveryBadge}><i className="ti ti-bolt" /> &lt;30 min</span>
                </div>
                <div className={styles.prodName}>{med.name}</div>
                <div className={styles.prodSub}>{med.sub}</div>
                <div className={styles.prodPriceRow}>
                  <div>
                    <span className={styles.prodPrice}>₹{med.price}</span>
                    <span className={styles.prodMrp}>₹{med.mrp}</span>
                  </div>
                  <button className={styles.addBtn}>Add</button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

    </div>
  );
}
