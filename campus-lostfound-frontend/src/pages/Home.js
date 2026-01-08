import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { FiSearch, FiAlertCircle, FiCheckCircle, FiMessageSquare } from 'react-icons/fi';
import './Home.css';

const Home = () => {
  const { user, isStudent, isAdmin } = useAuth();

  if (!user) {
    return (
      <div className="home-container">
        <div className="hero-section">
          <div className="hero-content">
            <h1>Campus Lost & Found</h1>
            <p>
              Lost something? Found something? We're here to help reunite items
              with their owners using smart matching technology.
            </p>
            <div className="hero-buttons">
              <Link to="/register" className="btn btn-primary btn-lg">
                Get Started
              </Link>
              <Link to="/login" className="btn btn-secondary btn-lg">
                Sign In
              </Link>
            </div>
          </div>
        </div>

        <div className="features-section">
          <div className="container">
            <h2>How It Works</h2>
            <div className="features-grid">
              <div className="feature-card">
                <div className="feature-icon">
                  <FiAlertCircle />
                </div>
                <h3>Report Lost Items</h3>
                <p>
                  Describe what you've lost with details like category, location,
                  and upload photos to help identify your item.
                </p>
              </div>

              <div className="feature-card">
                <div className="feature-icon">
                  <FiCheckCircle />
                </div>
                <h3>Report Found Items</h3>
                <p>
                  Found something? Report it and help someone reunite with their
                  lost belongings.
                </p>
              </div>

              <div className="feature-card">
                <div className="feature-icon">
                  <FiSearch />
                </div>
                <h3>Smart Matching</h3>
                <p>
                  Our system automatically matches lost and found items based on
                  category, location, and description.
                </p>
              </div>

              <div className="feature-card">
                <div className="feature-icon">
                  <FiMessageSquare />
                </div>
                <h3>Secure Messaging</h3>
                <p>
                  Connect with potential matches through our secure messaging
                  system to verify and arrange returns.
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard-container">
      <div className="container">
        <div className="dashboard-header">
          <h1>
            Welcome back, {user.email.split('@')[0]}!
          </h1>
          <p>What would you like to do today?</p>
        </div>

        {isStudent() && (
          <div className="quick-actions">
            <Link to="/lost-items/create" className="action-card action-lost">
              <FiAlertCircle className="action-icon" />
              <h3>Report Lost Item</h3>
              <p>Lost something? Let us help you find it.</p>
            </Link>

            <Link to="/found-items/create" className="action-card action-found">
              <FiCheckCircle className="action-icon" />
              <h3>Report Found Item</h3>
              <p>Found something? Help return it to its owner.</p>
            </Link>

            <Link to="/matches" className="action-card action-matches">
              <FiSearch className="action-icon" />
              <h3>View Matches</h3>
              <p>Check potential matches for your items.</p>
            </Link>

            <Link to="/messages" className="action-card action-messages">
              <FiMessageSquare className="action-icon" />
              <h3>Messages</h3>
              <p>Chat with potential matches.</p>
            </Link>
          </div>
        )}

        {isAdmin() && (
          <div className="quick-actions">
            <Link to="/admin/dashboard" className="action-card">
              <h3>Dashboard</h3>
              <p>View system statistics</p>
            </Link>

            <Link to="/admin/moderate" className="action-card">
              <h3>Moderate Items</h3>
              <p>Review pending submissions</p>
            </Link>

            <Link to="/admin/reports" className="action-card">
              <h3>Abuse Reports</h3>
              <p>Handle user reports</p>
            </Link>
          </div>
        )}
      </div>
    </div>
  );
};

export default Home;
