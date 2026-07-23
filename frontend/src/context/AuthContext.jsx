import { createContext, useContext, useState, useEffect } from 'react';
import { useUser, useClerk, useAuth as useClerkAuth } from '@clerk/clerk-react';
import { setAuthTokenGetter } from '@/services/api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const { user: clerkUser, isLoaded, isSignedIn } = useUser();
  const { signOut, openSignIn } = useClerk();
  const { getToken } = useClerkAuth();

  const [localUser, setLocalUser] = useState(null);

  // Bind Clerk JWT token getter to API service
  useEffect(() => {
    setAuthTokenGetter(() => getToken());
  }, [getToken]);

  useEffect(() => {
    if (isLoaded && isSignedIn && clerkUser) {
      const activeUser = {
        id: clerkUser.id,
        name: clerkUser.fullName || clerkUser.firstName || clerkUser.primaryEmailAddress?.emailAddress?.split('@')[0] || 'User',
        email: clerkUser.primaryEmailAddress?.emailAddress || '',
        phone: clerkUser.primaryPhoneNumber?.phoneNumber || '',
        role: clerkUser.publicMetadata?.role || 'user',
        imageUrl: clerkUser.imageUrl,
        isSubscriber: false,
      };
      setLocalUser(activeUser);
      localStorage.setItem('medifly_user', JSON.stringify(activeUser));
    } else if (isLoaded && !isSignedIn) {
      const saved = localStorage.getItem('medifly_user');
      if (saved) {
        setLocalUser(JSON.parse(saved));
      } else {
        setLocalUser(null);
      }
    }
  }, [isLoaded, isSignedIn, clerkUser]);

  const login = (phone, role = 'user') => {
    const userData = {
      id: 'USR-' + Date.now(),
      phone,
      name: role === 'admin' ? 'Admin User' : role === 'pharmacy' ? 'PharmaCare' : role === 'rider' ? 'Rider' : 'User',
      role,
      isSubscriber: false,
    };
    setLocalUser(userData);
    localStorage.setItem('medifly_user', JSON.stringify(userData));
    return userData;
  };

  const logout = () => {
    if (isSignedIn) {
      signOut();
    }
    setLocalUser(null);
    localStorage.removeItem('medifly_user');
    localStorage.removeItem('medifly_jwt_token');
  };

  const subscribe = (plan) => {
    const updated = {
      ...localUser,
      isSubscriber: true,
      subscriptionPlan: plan,
    };
    setLocalUser(updated);
    localStorage.setItem('medifly_user', JSON.stringify(updated));
  };

  const updateUser = (data) => {
    const updated = { ...localUser, ...data };
    setLocalUser(updated);
    localStorage.setItem('medifly_user', JSON.stringify(updated));
  };

  return (
    <AuthContext.Provider value={{
      user: localUser,
      loading: !isLoaded,
      isSignedIn,
      clerkUser,
      login,
      logout,
      openSignIn,
      subscribe,
      updateUser
    }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
