import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext(null);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const email = localStorage.getItem('userEmail');
    const role = localStorage.getItem('userRole');
    
    if (token && email && role) {
      setUser({ email, role });
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
    setLoading(false);
  }, []);

  const login = (token, email, role) => {
    localStorage.setItem('token', token);
    localStorage.setItem('userEmail', email);
    localStorage.setItem('userRole', role);
    api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    setUser({ email, role });
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userRole');
    delete api.defaults.headers.common['Authorization'];
    setUser(null);
  };

  const isAdmin = () => user?.role === 'ADMIN';
  const isStudent = () => user?.role === 'STUDENT';

  return (
    <AuthContext.Provider value={{ user, login, logout, isAdmin, isStudent, loading }}>
      {children}
    </AuthContext.Provider>
  );
};
