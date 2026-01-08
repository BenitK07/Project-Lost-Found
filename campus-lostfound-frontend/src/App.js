import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import Navigation from './components/Navigation';

// Pages
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import CreateLostItem from './pages/CreateLostItem';
import CreateFoundItem from './pages/CreateFoundItem';
import MyItems from './pages/MyItems';
import Matches from './pages/Matches';

import './App.css';

// Protected Route Component
const ProtectedRoute = ({ children, adminOnly = false }) => {
  const { user, loading, isAdmin } = useAuth();

  if (loading) {
    return (
      <div className="loading-container">
        <div className="spinner"></div>
      </div>
    );
  }

  if (!user) {
    return <Navigate to="/login" />;
  }

  if (adminOnly && !isAdmin()) {
    return <Navigate to="/" />;
  }

  return children;
};

// Public Route (redirect to home if logged in)
const PublicRoute = ({ children }) => {
  const { user, loading } = useAuth();

  if (loading) {
    return (
      <div className="loading-container">
        <div className="spinner"></div>
      </div>
    );
  }

  if (user) {
    return <Navigate to="/" />;
  }

  return children;
};

function AppContent() {
  return (
    <div className="app">
      <Router>
        <Navigation />
        <main className="main-content">
          <Routes>
            {/* Public Routes */}
            <Route path="/" element={<Home />} />
            <Route
              path="/login"
              element={
                <PublicRoute>
                  <Login />
                </PublicRoute>
              }
            />
            <Route
              path="/register"
              element={
                <PublicRoute>
                  <Register />
                </PublicRoute>
              }
            />

            {/* Protected Student Routes */}
            <Route
              path="/lost-items/create"
              element={
                <ProtectedRoute>
                  <CreateLostItem />
                </ProtectedRoute>
              }
            />
            <Route
              path="/found-items/create"
              element={
                <ProtectedRoute>
                  <CreateFoundItem />
                </ProtectedRoute>
              }
            />
            <Route
              path="/my-items"
              element={
                <ProtectedRoute>
                  <MyItems />
                </ProtectedRoute>
              }
            />
            <Route
              path="/matches"
              element={
                <ProtectedRoute>
                  <Matches />
                </ProtectedRoute>
              }
            />
            <Route
              path="/matches/:lostItemId"
              element={
                <ProtectedRoute>
                  <Matches />
                </ProtectedRoute>
              }
            />

            {/* Placeholder routes - implement these later */}
            <Route
              path="/messages"
              element={
                <ProtectedRoute>
                  <div className="container" style={{ padding: '40px' }}>
                    <h1>Messages</h1>
                    <p>Message functionality coming soon...</p>
                  </div>
                </ProtectedRoute>
              }
            />
            <Route
              path="/notifications"
              element={
                <ProtectedRoute>
                  <div className="container" style={{ padding: '40px' }}>
                    <h1>Notifications</h1>
                    <p>Notification functionality coming soon...</p>
                  </div>
                </ProtectedRoute>
              }
            />
            <Route
              path="/profile"
              element={
                <ProtectedRoute>
                  <div className="container" style={{ padding: '40px' }}>
                    <h1>Profile</h1>
                    <p>Profile page coming soon...</p>
                  </div>
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin/dashboard"
              element={
                <ProtectedRoute adminOnly>
                  <div className="container" style={{ padding: '40px' }}>
                    <h1>Admin Dashboard</h1>
                    <p>Admin dashboard coming soon...</p>
                  </div>
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin/moderate"
              element={
                <ProtectedRoute adminOnly>
                  <div className="container" style={{ padding: '40px' }}>
                    <h1>Moderate Items</h1>
                    <p>Moderation page coming soon...</p>
                  </div>
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin/reports"
              element={
                <ProtectedRoute adminOnly>
                  <div className="container" style={{ padding: '40px' }}>
                    <h1>Abuse Reports</h1>
                    <p>Reports page coming soon...</p>
                  </div>
                </ProtectedRoute>
              }
            />

            {/* 404 */}
            <Route path="*" element={<Navigate to="/" />} />
          </Routes>
        </main>
      </Router>
    </div>
  );
}

function App() {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
}

export default App;
