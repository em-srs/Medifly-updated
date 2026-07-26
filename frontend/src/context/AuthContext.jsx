import { createContext, useContext, useState, useEffect } from 'react';
import { useUser, useClerk } from '@clerk/clerk-react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Clerk hook (safely handles if Clerk is active or not)
  let clerkUser = null;
  let clerkObj = null;
  try {
    const u = useUser();
    clerkUser = u?.user;
    clerkObj = useClerk();
  } catch (e) {}

  useEffect(() => {
    if (clerkUser) {
      // Sync Clerk user with AuthContext
      const clerkAuthData = {
        id: clerkUser.id,
        name: clerkUser.fullName || clerkUser.primaryEmailAddress?.emailAddress?.split('@')[0] || 'Patient',
        email: clerkUser.primaryEmailAddress?.emailAddress || '',
        phone: clerkUser.primaryPhoneNumber?.phoneNumber || '',
        role: 'user',
        isSubscriber: false,
        subscribedAt: null,
        subscriptionPlan: null,
        subscriptionExpiry: null,
        joinedAt: clerkUser.createdAt ? new Date(clerkUser.createdAt).toISOString() : new Date().toISOString()
      };
      setUser(clerkAuthData);
      localStorage.setItem('medifly_user', JSON.stringify(clerkAuthData));
      setLoading(false);
      return;
    }

    const saved = localStorage.getItem('medifly_user');
    if (saved) {
      setUser(JSON.parse(saved));
    } else {
      setUser(null);
    }
    setLoading(false);
  }, [clerkUser]);

  const login = (phone, role = 'user') => {
    const userData = {
      id: 'USR-' + Date.now(),
      phone,
      name: role === 'admin' ? 'Admin User' : role === 'pharmacy' ? 'PharmaCare' : role === 'rider' ? 'Rider' : 'User',
      role,
      isSubscriber: false,
      subscribedAt: null,
      subscriptionPlan: null,
      subscriptionExpiry: null,
      joinedAt: new Date().toISOString()
    };
    setUser(userData);
    localStorage.setItem('medifly_user', JSON.stringify(userData));
    return userData;
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('medifly_user');
    if (clerkObj && clerkObj.signOut) {
      clerkObj.signOut();
    }
  };

  const subscribe = (plan) => {
    const expiry = new Date();
    if (plan === 'monthly') {
      expiry.setMonth(expiry.getMonth() + 1);
    } else {
      expiry.setFullYear(expiry.getFullYear() + 1);
    }
    const updated = {
      ...user,
      isSubscriber: true,
      subscribedAt: new Date().toISOString(),
      subscriptionPlan: plan,
      subscriptionExpiry: expiry.toISOString()
    };
    setUser(updated);
    localStorage.setItem('medifly_user', JSON.stringify(updated));
  };

  const updateUser = (data) => {
    const updated = { ...user, ...data };
    setUser(updated);
    localStorage.setItem('medifly_user', JSON.stringify(updated));
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, logout, subscribe, updateUser }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
