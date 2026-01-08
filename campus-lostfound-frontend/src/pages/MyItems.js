import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../services/api';
import { FiAlertCircle, FiCheckCircle, FiClock, FiXCircle, FiSearch } from 'react-icons/fi';
import './MyItems.css';

const MyItems = () => {
  const [lostItems, setLostItems] = useState([]);
  const [foundItems, setFoundItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('lost');

  useEffect(() => {
    fetchItems();
  }, []);

  const fetchItems = async () => {
    try {
      const [lostRes, foundRes] = await Promise.all([
        api.get('/lost-items/my'),
        api.get('/found-items/my')
      ]);
      setLostItems(lostRes.data);
      setFoundItems(foundRes.data);
    } catch (err) {
      console.error('Failed to fetch items', err);
    } finally {
      setLoading(false);
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case 'PENDING':
        return <FiClock className="status-icon pending" />;
      case 'APPROVED':
        return <FiCheckCircle className="status-icon approved" />;
      case 'REJECTED':
        return <FiXCircle className="status-icon rejected" />;
      default:
        return null;
    }
  };

  const getStatusClass = (status) => {
    return status?.toLowerCase() || 'pending';
  };

  const ItemCard = ({ item, type }) => (
    <div className="item-card">
      <div className="item-header">
        <h3>{item.title}</h3>
        <span className={`status-badge ${getStatusClass(item.status)}`}>
          {getStatusIcon(item.status)}
          {item.status}
        </span>
      </div>
      <div className="item-details">
        <p className="item-category">
          <strong>Category:</strong> {item.category}
        </p>
        <p className="item-location">
          <strong>Location:</strong> {item.location}
        </p>
        <p className="item-description">{item.description}</p>
        <p className="item-date">
          <small>
            {type === 'lost' ? 'Lost on' : 'Found on'}{' '}
            {new Date(item.createdAt).toLocaleDateString()}
          </small>
        </p>
      </div>
      {item.status === 'APPROVED' && type === 'lost' && (
        <Link to={`/matches/${item.id}`} className="btn btn-secondary btn-sm">
          <FiSearch /> View Matches
        </Link>
      )}
    </div>
  );

  if (loading) {
    return (
      <div className="loading-container">
        <div className="spinner"></div>
        <p>Loading your items...</p>
      </div>
    );
  }

  return (
    <div className="my-items-container">
      <div className="container">
        <div className="page-header">
          <h1>My Items</h1>
          <div className="header-actions">
            <Link to="/lost-items/create" className="btn btn-primary">
              <FiAlertCircle /> Report Lost
            </Link>
            <Link to="/found-items/create" className="btn btn-accent">
              <FiCheckCircle /> Report Found
            </Link>
          </div>
        </div>

        <div className="tabs">
          <button
            className={`tab ${activeTab === 'lost' ? 'active' : ''}`}
            onClick={() => setActiveTab('lost')}
          >
            <FiAlertCircle />
            Lost Items ({lostItems.length})
          </button>
          <button
            className={`tab ${activeTab === 'found' ? 'active' : ''}`}
            onClick={() => setActiveTab('found')}
          >
            <FiCheckCircle />
            Found Items ({foundItems.length})
          </button>
        </div>

        <div className="items-grid">
          {activeTab === 'lost' ? (
            lostItems.length > 0 ? (
              lostItems.map(item => (
                <ItemCard key={item.id} item={item} type="lost" />
              ))
            ) : (
              <div className="empty-state">
                <FiAlertCircle />
                <p>You haven't reported any lost items yet.</p>
                <Link to="/lost-items/create" className="btn btn-primary">
                  Report Lost Item
                </Link>
              </div>
            )
          ) : (
            foundItems.length > 0 ? (
              foundItems.map(item => (
                <ItemCard key={item.id} item={item} type="found" />
              ))
            ) : (
              <div className="empty-state">
                <FiCheckCircle />
                <p>You haven't reported any found items yet.</p>
                <Link to="/found-items/create" className="btn btn-accent">
                  Report Found Item
                </Link>
              </div>
            )
          )}
        </div>
      </div>
    </div>
  );
};

export default MyItems;
