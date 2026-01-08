import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { FiMenu, FiX, FiSearch, FiMessageSquare, FiBell, FiUser, FiLogOut } from 'react-icons/fi';
import './Navigation.css';

const Navigation = () => {
  const { user, logout, isAdmin, isStudent } = useAuth();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-brand">
          <FiSearch className="brand-icon" />
          Campus Lost & Found
        </Link>

        <button 
          className="navbar-toggle"
          onClick={() => setMenuOpen(!menuOpen)}
        >
          {menuOpen ? <FiX /> : <FiMenu />}
        </button>

        <div className={`navbar-menu ${menuOpen ? 'active' : ''}`}>
          {user ? (
            <>
              {isStudent() && (
                <>
                  <Link to="/lost-items/create" className="nav-link">
                    Report Lost
                  </Link>
                  <Link to="/found-items/create" className="nav-link">
                    Report Found
                  </Link>
                  <Link to="/my-items" className="nav-link">
                    My Items
                  </Link>
                  <Link to="/matches" className="nav-link">
                    Matches
                  </Link>
                  <Link to="/messages" className="nav-link">
                    <FiMessageSquare />
                  </Link>
                  <Link to="/notifications" className="nav-link">
                    <FiBell />
                  </Link>
                </>
              )}

              {isAdmin() && (
                <>
                  <Link to="/admin/dashboard" className="nav-link">
                    Dashboard
                  </Link>
                  <Link to="/admin/moderate" className="nav-link">
                    Moderate
                  </Link>
                  <Link to="/admin/reports" className="nav-link">
                    Reports
                  </Link>
                </>
              )}

              <Link to="/profile" className="nav-link">
                <FiUser />
              </Link>

              <button onClick={handleLogout} className="nav-link logout-btn">
                <FiLogOut />
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="nav-link">
                Login
              </Link>
              <Link to="/register" className="btn-primary-nav">
                Register
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navigation;
