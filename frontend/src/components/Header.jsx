import { useState, useRef, useEffect, useCallback } from 'react';
import { Link, useNavigate, useLocation, useSearchParams } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext';
import { useCart } from '@/context/CartContext';
import styles from './Header.module.css';

/* ── Mode tabs (top row, centered) ─────────────────────── */
const MODE_TABS = [
  { href: '/medicines',    icon: 'ti-shopping-bag', label: 'Shop' },
  { href: '/subscription', icon: 'ti-refresh',      label: 'Auto refill',       badge: 'New' },
  { href: '/prescriptions',icon: 'ti-folder',       label: 'Prescription vault', badge: 'New' },
  { href: '/salt-compare', icon: 'ti-flask-2',      label: 'Salt comparison' },
  { href: '/about',        icon: 'ti-info-circle',  label: 'About' },
];

/* ── Sample locations ───────────────────────────────────── */
const POPULAR_LOCATIONS = [
  { city: 'Mohali, Punjab', pincode: '160055' },
  { city: 'Chandigarh', pincode: '160017' },
  { city: 'Panchkula, Haryana', pincode: '134109' },
  { city: 'New Delhi', pincode: '110001' },
  { city: 'Mumbai', pincode: '400050' },
  { city: 'Bengaluru', pincode: '560038' },
];

/* ── Delivery slots ─────────────────────────────────────── */
const DELIVERY_SLOTS = [
  { id: 'express', label: 'As soon as possible', sub: 'Quick ASAP delivery under 30 mins', icon: 'ti-bolt' },
  { id: 'evening', label: 'Today Evening', sub: 'Between 5:00 PM – 8:00 PM', icon: 'ti-sunset' },
  { id: 'tomorrow', label: 'Tomorrow Morning', sub: 'Between 9:00 AM – 12:00 PM', icon: 'ti-sun' },
  { id: 'scheduled', label: 'Scheduled Refill', sub: 'Auto-recurring delivery date', icon: 'ti-calendar-event' },
];

/* ── Trending search terms ───────────────────────────────── */
const TRENDING_SEARCHES = ['Dolo 650', 'Paracetamol', 'Pan 40', 'Azithral', 'Shelcal 500', 'Crocin Advance', 'Metformin'];

export default function Header() {
  const { pathname } = useLocation();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const { totalItems, setIsOpen } = useCart();

  const [userMenuOpen, setUserMenuOpen] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);
  const [activeSearch, setActiveSearch] = useState(null); // null | 0 | 1 | 2

  // ── Search State ──
  const [searchQuery, setSearchQuery] = useState(searchParams.get('q') || '');
  const [location, setLocation] = useState(searchParams.get('location') || 'Mohali, Punjab');
  const [deliverySlot, setDeliverySlot] = useState(searchParams.get('slot') || 'As soon as possible');
  const [customPincode, setCustomPincode] = useState('');
  const [dbMedicines, setDbMedicines] = useState([]);

  useEffect(() => {
    fetch('/medicines.json')
      .then(res => res.json())
      .then(data => setDbMedicines(data))
      .catch(() => {});
  }, []);

  const liveSuggestions = searchQuery.trim().length >= 1
    ? dbMedicines.filter(m => {
        const q = searchQuery.toLowerCase().trim();
        const bName = (m.brandName || m.name || '').toLowerCase();
        const gName = (m.genericName || m.salt || '').toLowerCase();
        return bName.includes(q) || gName.includes(q);
      }).slice(0, 6)
    : [];

  const menuRef = useRef(null);
  const searchCapsuleRef = useRef(null);
  const segIndicatorRef = useRef(null);
  const searchInputRef = useRef(null);

  // ── Close dropdowns on outside click ──
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (menuRef.current && !menuRef.current.contains(e.target)) {
        setUserMenuOpen(false);
      }
      if (searchCapsuleRef.current && !searchCapsuleRef.current.contains(e.target)) {
        setActiveSearch(null);
        const ind = segIndicatorRef.current;
        if (ind) ind.classList.remove(styles.segShow);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  // ── Close mobile nav on route change ──
  useEffect(() => { setMobileOpen(false); }, [pathname]);

  // ── Sync URL query param to search state ──
  useEffect(() => {
    const q = searchParams.get('q');
    if (q !== null) setSearchQuery(q);
  }, [searchParams]);

  // ── Prevent body scroll when mobile nav is open ──
  useEffect(() => {
    document.body.style.overflow = mobileOpen ? 'hidden' : '';
    return () => { document.body.style.overflow = ''; };
  }, [mobileOpen]);

  // ── Search segment indicator animation ──
  const moveSegIndicator = useCallback((segEl) => {
    const ind = segIndicatorRef.current;
    if (!ind || !segEl) return;
    ind.style.left = segEl.offsetLeft + 'px';
    ind.style.top = segEl.offsetTop + 'px';
    ind.style.width = segEl.offsetWidth + 'px';
    ind.style.height = segEl.offsetHeight + 'px';
    ind.classList.add(styles.segShow);
    ind.classList.add(styles.segBlurring);
    clearTimeout(ind._t);
    ind._t = setTimeout(() => ind.classList.remove(styles.segBlurring), 380);
  }, []);

  const handleSegClick = (idx, e) => {
    setActiveSearch(idx);
    moveSegIndicator(e.currentTarget);
    if (idx === 0) {
      setTimeout(() => searchInputRef.current?.focus(), 50);
    }
  };

  const handleExecuteSearch = (queryToUse) => {
    const q = queryToUse !== undefined ? queryToUse : searchQuery;
    setActiveSearch(null);
    const params = new URLSearchParams();
    if (q.trim()) params.set('q', q.trim());
    if (location) params.set('location', location);
    if (deliverySlot) params.set('slot', deliverySlot);
    navigate(`/medicines?${params.toString()}`);
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      handleExecuteSearch();
    }
  };

  const handleSelectLocation = (locName) => {
    setLocation(locName);
    setActiveSearch(null);
  };

  const handleCustomPincodeSubmit = (e) => {
    e.preventDefault();
    if (customPincode.trim()) {
      setLocation(`PIN ${customPincode.trim()}`);
      setCustomPincode('');
      setActiveSearch(null);
    }
  };

  const handleSelectSlot = (slotLabel) => {
    setDeliverySlot(slotLabel);
    setActiveSearch(null);
  };

  const handleLogout = () => {
    logout();
    setUserMenuOpen(false);
    navigate('/');
  };

  const activeMode = MODE_TABS.find(t => pathname === t.href || pathname.startsWith(t.href + '/'));

  const initials = user?.name
    ? user.name.split(' ').map(w => w[0]).join('').slice(0, 2).toUpperCase()
    : 'SK';

  return (
    <>
      <header className={styles.navbar}>
        {/* ─── Row 1: Logo + mode tabs + right actions ─── */}
        <div className={styles.navRow1}>
          <Link to="/" className={styles.logo}>
            <i className="ti ti-vaccine-bottle" />
            <span>MediFly</span>
          </Link>

          <div className={styles.navTabs}>
            {MODE_TABS.map(({ href, icon, label, badge }) => (
              <Link
                key={href}
                to={href}
                className={`${styles.navTab} ${activeMode?.href === href ? styles.navTabActive : ''}`}
              >
                {badge && <span className={styles.tabBadge}>{badge}</span>}
                <i className={`ti ${icon}`} />
                <span>{label}</span>
              </Link>
            ))}
          </div>

          <div className={styles.navRight}>
            {/* Cart button */}
            <button className={styles.iconBtn} onClick={() => setIsOpen(true)} aria-label="Open cart">
              <i className="ti ti-shopping-cart" />
              {totalItems > 0 && <span className={styles.cartCount}>{totalItems}</span>}
            </button>

            {/* Account pill */}
            <div className={styles.accountPill} ref={menuRef}>
              <button
                className={styles.accountPillBtn}
                onClick={() => setUserMenuOpen(!userMenuOpen)}
                aria-label="User menu"
              >
                <i className="ti ti-menu-2" />
                <div className={styles.avatar}>{initials}</div>
              </button>

              {userMenuOpen && (
                <div className={styles.userDropdown}>
                  {user ? (
                    <>
                      <div className={styles.dropdownHeader}>
                        <strong>{user.name}</strong>
                        <span className={styles.dropdownRole}>{user.role}</span>
                      </div>
                      <div className={styles.dropdownDivider} />
                      <Link to="/profile"   className={styles.dropdownItem} onClick={() => setUserMenuOpen(false)}><i className="ti ti-user" /> My profile</Link>
                      <Link to="/dashboard" className={styles.dropdownItem} onClick={() => setUserMenuOpen(false)}><i className="ti ti-chart-bar" /> Dashboard</Link>
                      <Link to="/orders"    className={styles.dropdownItem} onClick={() => setUserMenuOpen(false)}><i className="ti ti-package" /> My orders</Link>
                      <Link to="/settings"  className={styles.dropdownItem} onClick={() => setUserMenuOpen(false)}><i className="ti ti-settings" /> Settings</Link>
                      <div className={styles.dropdownDivider} />
                      <button className={styles.dropdownLogout} onClick={handleLogout}><i className="ti ti-logout" /> Logout</button>
                    </>
                  ) : (
                    <>
                      <div className={styles.dropdownHeader}>
                        <strong>Welcome to MediFly</strong>
                        <span className={styles.dropdownRole}>Sign in to continue</span>
                      </div>
                      <div className={styles.dropdownDivider} />
                      <Link to="/login" className={styles.dropdownItem} onClick={() => setUserMenuOpen(false)}><i className="ti ti-key" /> Login / Register</Link>
                    </>
                  )}
                </div>
              )}
            </div>

            {/* Hamburger — mobile only */}
            <button
              className={styles.hamburger}
              onClick={() => setMobileOpen(!mobileOpen)}
              aria-label={mobileOpen ? 'Close menu' : 'Open menu'}
              aria-expanded={mobileOpen}
            >
              <span className={`${styles.hbar} ${mobileOpen ? styles.hbar1Open : ''}`} />
              <span className={`${styles.hbar} ${mobileOpen ? styles.hbar2Open : ''}`} />
              <span className={`${styles.hbar} ${mobileOpen ? styles.hbar3Open : ''}`} />
            </button>
          </div>
        </div>

        {/* ─── Row 2: Search capsule ─── */}
        <div className={styles.navRow2}>
          <div className={styles.searchCapsule} ref={searchCapsuleRef}>
            <div className={styles.segIndicator} ref={segIndicatorRef} />

            {/* ── Segment 0: Medicine or salt ── */}
            <div
              className={`${styles.searchSeg} ${activeSearch === 0 ? styles.searchSegActive : searchQuery.trim() ? styles.searchSegFilled : ''}`}
              onClick={(e) => handleSegClick(0, e)}
            >
              <span className={styles.segLabel}>
                <i className={`ti ti-search ${styles.segLabelIcon}`} />
                Medicine or salt
              </span>
              <span className={styles.segValue}>
                {searchQuery || 'Search medicine or salt'}
              </span>
              {searchQuery.trim() && (
                <button
                  className={styles.clearSegBtn}
                  title="Clear search"
                  onClick={(e) => {
                    e.stopPropagation();
                    setSearchQuery('');
                  }}
                >
                  <i className="ti ti-x" />
                </button>
              )}
            </div>

            <div className={styles.segDivider} />

            {/* ── Segment 1: Deliver to ── */}
            <div
              className={`${styles.searchSeg} ${activeSearch === 1 ? styles.searchSegActive : location ? styles.searchSegFilled : ''}`}
              onClick={(e) => handleSegClick(1, e)}
            >
              <span className={styles.segLabel}>
                <i className={`ti ti-map-pin ${styles.segLabelIcon}`} />
                Deliver to
              </span>
              <span className={styles.segValue}>{location}</span>
            </div>

            <div className={styles.segDivider} />

            {/* ── Segment 2: Need it by ── */}
            <div
              className={`${styles.searchSeg} ${activeSearch === 2 ? styles.searchSegActive : deliverySlot ? styles.searchSegFilled : ''}`}
              onClick={(e) => handleSegClick(2, e)}
            >
              <span className={styles.segLabel}>
                <i className={`ti ti-clock ${styles.segLabelIcon}`} />
                Need it by
              </span>
              <span className={styles.segValue}>{deliverySlot}</span>
            </div>

            {/* ── Search action button ── */}
            <button
              className={styles.searchBtn}
              aria-label="Search"
              onClick={() => handleExecuteSearch()}
            >
              <i className="ti ti-search" />
            </button>

            {/* ════ Popovers for Active Segment ════ */}
            {activeSearch === 0 && (
              <div className={styles.popover}>
                <div className={styles.popoverSearchWrap}>
                  <i className="ti ti-search" />
                  <input
                    ref={searchInputRef}
                    type="text"
                    className={styles.popoverSearchInput}
                    placeholder="Search medicine, salt, or composition..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    onKeyDown={handleKeyDown}
                    autoFocus
                  />
                  {searchQuery && (
                    <button
                      className={styles.clearSearchBtn}
                      onClick={() => setSearchQuery('')}
                      aria-label="Clear search"
                    >
                      <i className="ti ti-x" />
                    </button>
                  )}
                </div>

                {liveSuggestions.length > 0 && (
                  <div className={styles.liveSuggestionsList}>
                    {liveSuggestions.map((med) => (
                      <button
                        key={med.id}
                        className={styles.liveSuggestionItem}
                        onClick={() => {
                          const name = med.brandName || med.name;
                          setSearchQuery(name);
                          handleExecuteSearch(name);
                        }}
                      >
                        <i className="ti ti-pill" />
                        <div className={styles.liveSugDetails}>
                          <strong>{med.brandName || med.name}</strong>
                          <span>Salt: {med.genericName || med.salt}</span>
                        </div>
                        <span className={styles.liveSugPrice}>₹{med.price}</span>
                      </button>
                    ))}
                  </div>
                )}

                <div className={styles.popoverHeader}>
                  <i className="ti ti-flame" />
                  <span>Trending Searches</span>
                </div>
                <div className={styles.trendingList}>
                  {TRENDING_SEARCHES.map((item) => (
                    <button
                      key={item}
                      className={styles.trendingItem}
                      onClick={() => {
                        setSearchQuery(item);
                        handleExecuteSearch(item);
                      }}
                    >
                      <i className="ti ti-search" />
                      <span>{item}</span>
                    </button>
                  ))}
                </div>
              </div>
            )}

            {activeSearch === 1 && (
              <div className={styles.popover}>
                <div className={styles.popoverHeader}>
                  <i className="ti ti-map-pin" />
                  <span>Select Delivery Location</span>
                </div>
                <button
                  className={styles.geoBtn}
                  onClick={() => handleSelectLocation('Mohali, Punjab')}
                >
                  <i className="ti ti-current-location" />
                  <span>Use current location</span>
                </button>

                <form onSubmit={handleCustomPincodeSubmit} className={styles.pincodeForm}>
                  <input
                    type="text"
                    placeholder="Enter Pincode or City..."
                    value={customPincode}
                    onChange={(e) => setCustomPincode(e.target.value)}
                    className={styles.pincodeInput}
                  />
                  <button type="submit" className={styles.pincodeSubmit}>Apply</button>
                </form>

                <div className={styles.popoverSubhead}>Popular Cities</div>
                <div className={styles.locList}>
                  {POPULAR_LOCATIONS.map((loc) => (
                    <button
                      key={loc.city}
                      className={`${styles.locItem} ${location === loc.city ? styles.locActive : ''}`}
                      onClick={() => handleSelectLocation(loc.city)}
                    >
                      <div className={styles.locInfo}>
                        <strong>{loc.city}</strong>
                        <span>PIN {loc.pincode}</span>
                      </div>
                      {location === loc.city && <i className="ti ti-check" />}
                    </button>
                  ))}
                </div>
              </div>
            )}

            {activeSearch === 2 && (
              <div className={styles.popover}>
                <div className={styles.popoverHeader}>
                  <i className="ti ti-clock" />
                  <span>Select Delivery Time</span>
                </div>
                <div className={styles.slotList}>
                  {DELIVERY_SLOTS.map((slot) => (
                    <button
                      key={slot.id}
                      className={`${styles.slotItem} ${deliverySlot === slot.label ? styles.slotActive : ''}`}
                      onClick={() => handleSelectSlot(slot.label)}
                    >
                      <div className={styles.slotIconWrap}>
                        <i className={`ti ${slot.icon}`} />
                      </div>
                      <div className={styles.slotText}>
                        <strong>{slot.label}</strong>
                        <span>{slot.sub}</span>
                      </div>
                      {deliverySlot === slot.label && <i className={`ti ti-check ${styles.slotCheck}`} />}
                    </button>
                  ))}
                </div>
              </div>
            )}

          </div>
        </div>
      </header>

      {/* ─── Mobile Nav Drawer ─── */}
      <div
        className={`${styles.mobileBackdrop} ${mobileOpen ? styles.mobileBackdropOpen : ''}`}
        onClick={() => setMobileOpen(false)}
        aria-hidden="true"
      />
      <nav className={`${styles.mobileNav} ${mobileOpen ? styles.mobileNavOpen : ''}`} aria-label="Mobile navigation">
        <div className={styles.mobileNavHeader}>
          <Link to="/" className={styles.logo} onClick={() => setMobileOpen(false)}>
            <i className="ti ti-vaccine-bottle" />
            <span>MediFly</span>
          </Link>
          <button className={styles.mobileClose} onClick={() => setMobileOpen(false)} aria-label="Close menu">
            <i className="ti ti-x" />
          </button>
        </div>

        <div className={styles.mobileNavLinks}>
          <Link to="/"              className={`${styles.mobileNavLink} ${pathname === '/' ? styles.mobileNavActive : ''}`} onClick={() => setMobileOpen(false)}><i className="ti ti-home" /> Home</Link>
          <Link to="/medicines"     className={`${styles.mobileNavLink} ${pathname === '/medicines' ? styles.mobileNavActive : ''}`} onClick={() => setMobileOpen(false)}><i className="ti ti-shopping-bag" /> Shop</Link>
          <Link to="/subscription"  className={`${styles.mobileNavLink} ${pathname === '/subscription' ? styles.mobileNavActive : ''}`} onClick={() => setMobileOpen(false)}><i className="ti ti-refresh" /> Auto refill</Link>
          <Link to="/prescriptions" className={`${styles.mobileNavLink} ${pathname === '/prescriptions' ? styles.mobileNavActive : ''}`} onClick={() => setMobileOpen(false)}><i className="ti ti-folder" /> Prescription vault</Link>
          <Link to="/salt-compare"  className={`${styles.mobileNavLink} ${pathname === '/salt-compare' ? styles.mobileNavActive : ''}`} onClick={() => setMobileOpen(false)}><i className="ti ti-flask" /> Salt comparison</Link>
          <Link to="/about"         className={`${styles.mobileNavLink} ${pathname === '/about' ? styles.mobileNavActive : ''}`} onClick={() => setMobileOpen(false)}><i className="ti ti-info-circle" /> About</Link>
          <Link to="/contact"       className={`${styles.mobileNavLink} ${pathname === '/contact' ? styles.mobileNavActive : ''}`} onClick={() => setMobileOpen(false)}><i className="ti ti-phone" /> Contact</Link>
        </div>

        <div className={styles.mobileNavFooter}>
          <button
            className={styles.mobileCartBtn}
            onClick={() => { setIsOpen(true); setMobileOpen(false); }}
          >
            <i className="ti ti-shopping-cart" /> View cart {totalItems > 0 && <span className={styles.mobileCartCount}>{totalItems}</span>}
          </button>
          {!user && (
            <Link to="/login" className={styles.mobileLoginBtn} onClick={() => setMobileOpen(false)}>
              <i className="ti ti-key" /> Login / Register
            </Link>
          )}
        </div>
      </nav>
    </>
  );
}
