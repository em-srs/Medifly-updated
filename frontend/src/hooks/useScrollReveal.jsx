import { useEffect, useRef } from 'react';

/**
 * Attaches an IntersectionObserver to a container element.
 * Any child with data-reveal will be animated when it
 * enters the viewport.
 *
 * Uses a standard useRef + polling approach so it works
 * even when the container mounts after the initial render
 * (e.g. conditional sections).
 *
 * Usage:
 *   const sectionRef = useScrollReveal();
 *   <section ref={sectionRef}>
 *     <div data-reveal="true" data-delay="0">...</div>
 *   </section>
 */
export default function useScrollReveal(threshold = 0.05) {
  const ref = useRef(null);

  useEffect(() => {
    const root = ref.current;
    if (!root) return;

    const revealElement = (el) => {
      if (!el) return;
      const delay = el.dataset.delay || '0';
      el.style.transitionDelay = `${delay}ms`;
      el.classList.add('revealed');
    };

    const io = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            revealElement(entry.target);
            io.unobserve(entry.target);
          }
        });
      },
      { threshold: 0 }
    );

    const observeAll = () => {
      root.querySelectorAll('[data-reveal]').forEach((el) => {
        if (!el.classList.contains('revealed')) {
          const rect = el.getBoundingClientRect();
          if (rect.top < window.innerHeight && rect.bottom >= 0) {
            revealElement(el);
          } else {
            io.observe(el);
          }
        }
      });
    };

    observeAll();

    // Safety fallback so content is never trapped at opacity: 0
    const timer = setTimeout(() => {
      if (root) {
        root.querySelectorAll('[data-reveal]').forEach(revealElement);
      }
    }, 300);

    const mo = new MutationObserver(() => observeAll());
    mo.observe(root, { childList: true, subtree: true });

    return () => {
      clearTimeout(timer);
      io.disconnect();
      mo.disconnect();
    };
  }, [threshold]);

  return ref;
}
