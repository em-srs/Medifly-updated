import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { SignIn, SignUp, useUser } from '@clerk/clerk-react';
import { useAuth } from '@/context/AuthContext';
import styles from './LoginPage.module.css';
import useScrollReveal from '@/hooks/useScrollReveal';
import { Shield, Lock, TestTubes, RefreshCw, User, CheckCircle2, Zap, Bike, Hospital } from 'lucide-react';

export default function LoginPage() {
  const [authMode, setAuthMode] = useState('signin'); // 'signin' | 'signup'
  const { isSignedIn } = useUser();
  const { login } = useAuth();
  const navigate = useNavigate();

  const pageRef = useScrollReveal(0.05);

  if (isSignedIn) {
    navigate('/dashboard');
  }

  return (
    <div className={styles.page}>
      <div className={styles.container} ref={pageRef}>
        <div className={styles.left} data-reveal="left" data-delay="0">
          <div className={styles.branding}>
            <span className={styles.brandIcon}>⚕️</span>
            <h1>Welcome to <span className="text-gradient">MediFly</span></h1>
            <p>Medicines delivered under 30 minutes or scheduled to your doorstep. Sign in to access your dashboard, prescriptions, and auto-refill orders.</p>
          </div>
          <div className={styles.features}>
            <div className={styles.feature}><span><Zap size={18} style={{ display: 'inline-block', verticalAlign: 'middle', marginRight: '4px' }} /></span> Quick ASAP delivery under 30 mins</div>
            <div className={styles.feature}><span><TestTubes size={18} style={{ display: 'inline-block', verticalAlign: 'middle', marginRight: '4px' }} /></span> Bioequivalent salt comparison tool</div>
            <div className={styles.feature}><span><RefreshCw size={18} style={{ display: 'inline-block', verticalAlign: 'middle', marginRight: '4px' }} /></span> Auto-refill engine for chronic meds</div>
            <div className={styles.feature}><span><Lock size={18} style={{ display: 'inline-block', verticalAlign: 'middle', marginRight: '4px' }} /></span> Secure prescription vault</div>
          </div>
        </div>

        <div className={styles.right} data-reveal="right" data-delay="100">
          <div className={styles.card} style={{ padding: '20px', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <div style={{ marginBottom: '16px', display: 'flex', gap: '8px' }}>
              <button
                className={`btn ${authMode === 'signin' ? 'btn-primary' : 'btn-outline'}`}
                onClick={() => setAuthMode('signin')}
              >
                Sign In
              </button>
              <button
                className={`btn ${authMode === 'signup' ? 'btn-primary' : 'btn-outline'}`}
                onClick={() => setAuthMode('signup')}
              >
                Sign Up
              </button>
            </div>

            {authMode === 'signin' ? (
              <SignIn routing="hash" redirectUrl="/dashboard" />
            ) : (
              <SignUp routing="hash" redirectUrl="/dashboard" />
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
