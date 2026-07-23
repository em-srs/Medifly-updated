import { useState, useEffect, useRef } from 'react';
import styles from './MedicineAutocomplete.module.css';

// Shared module-level cache for medicines.json so we only fetch once
let cachedMedicines = null;
let fetchPromise = null;

function loadMedicines() {
  if (cachedMedicines) return Promise.resolve(cachedMedicines);
  if (!fetchPromise) {
    fetchPromise = fetch('/medicines.json')
      .then(res => res.json())
      .then(data => {
        cachedMedicines = data;
        return data;
      })
      .catch(err => {
        console.error('Failed to load medicines:', err);
        fetchPromise = null;
        return [];
      });
  }
  return fetchPromise;
}

export default function MedicineAutocomplete({
  value,
  onChange,
  onSelect,
  placeholder = 'Search medicine, salt, or brand...',
  className = '',
  autoFocus = false,
}) {
  const [medicines, setMedicines] = useState([]);
  const [suggestions, setSuggestions] = useState([]);
  const [isOpen, setIsOpen] = useState(false);
  const containerRef = useRef(null);

  useEffect(() => {
    loadMedicines().then(data => setMedicines(data));
  }, []);

  useEffect(() => {
    const handleOutsideClick = (e) => {
      if (containerRef.current && !containerRef.current.contains(e.target)) {
        setIsOpen(false);
      }
    };
    document.addEventListener('mousedown', handleOutsideClick);
    return () => document.removeEventListener('mousedown', handleOutsideClick);
  }, []);

  const handleInputChange = (e) => {
    const val = e.target.value;
    onChange(val);

    if (!val || val.trim().length < 1) {
      setSuggestions([]);
      setIsOpen(false);
      return;
    }

    const q = val.toLowerCase().trim();
    const matches = medicines.filter(med => {
      const bName = (med.brandName || med.name || '').toLowerCase();
      const gName = (med.genericName || med.salt || '').toLowerCase();
      const mName = (med.manufacturer || '').toLowerCase();
      return bName.includes(q) || gName.includes(q) || mName.includes(q);
    }).slice(0, 8);

    setSuggestions(matches);
    setIsOpen(true);
  };

  const handleSelectMed = (med) => {
    const nameToUse = med.brandName || med.name;
    onChange(nameToUse);
    if (onSelect) onSelect(med);
    setIsOpen(false);
  };

  return (
    <div className={styles.autocompleteWrap} ref={containerRef}>
      <div className={styles.inputContainer}>
        <i className={`ti ti-search ${styles.searchIcon}`} />
        <input
          type="text"
          className={`${styles.inputField} ${className}`}
          placeholder={placeholder}
          value={value}
          onChange={handleInputChange}
          onFocus={() => {
            if (value && suggestions.length > 0) setIsOpen(true);
          }}
          autoFocus={autoFocus}
        />
        {value && (
          <button
            type="button"
            className={styles.clearBtn}
            onClick={() => {
              onChange('');
              setSuggestions([]);
              setIsOpen(false);
            }}
          >
            <i className="ti ti-x" />
          </button>
        )}
      </div>

      {isOpen && suggestions.length > 0 && (
        <div className={styles.dropdown}>
          {suggestions.map((med, index) => (
            <div
              key={med.id || index}
              className={styles.suggestionItem}
              onClick={() => handleSelectMed(med)}
            >
              <div className={styles.medIcon}>
                <i className="ti ti-pill" />
              </div>
              <div className={styles.medDetails}>
                <div className={styles.medName}>
                  <strong>{med.brandName || med.name}</strong>
                  {med.strength && <span className={styles.strength}>{med.strength}</span>}
                </div>
                <div className={styles.medSub}>
                  <span>Salt: {med.genericName || med.salt}</span>
                  {med.manufacturer && <span> · {med.manufacturer}</span>}
                </div>
              </div>
              {med.price && (
                <div className={styles.medPrice}>
                  ₹{med.price}
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
