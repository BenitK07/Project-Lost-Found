import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import api from '../services/api';
import { FiCheck, FiX, FiMessageSquare } from 'react-icons/fi';
import './Matches.css';

const Matches = () => {
  const { lostItemId } = useParams();
  const [matches, setMatches] = useState([]);
  const [myMatches, setMyMatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(null);

  useEffect(() => {
    if (lostItemId) {
      fetchItemMatches();
    } else {
      fetchMyMatches();
    }
  }, [lostItemId]);

  const fetchItemMatches = async () => {
    try {
      const response = await api.get(`/match/lost/${lostItemId}`);
      setMatches(response.data);
    } catch (err) {
      console.error('Failed to fetch matches', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchMyMatches = async () => {
    try {
      const response = await api.get('/matches/my');
      setMyMatches(response.data);
    } catch (err) {
      console.error('Failed to fetch matches', err);
    } finally {
      setLoading(false);
    }
  };

  const handleConfirm = async (matchId) => {
    setActionLoading(matchId);
    try {
      await api.post(`/matches/${matchId}/confirm`);
      alert('Match confirmed!');
      if (lostItemId) {
        fetchItemMatches();
      } else {
        fetchMyMatches();
      }
    } catch (err) {
      alert('Failed to confirm match');
    } finally {
      setActionLoading(null);
    }
  };

  const handleReject = async (matchId) => {
    setActionLoading(matchId);
    try {
      await api.post(`/matches/${matchId}/reject`);
      alert('Match rejected');
      if (lostItemId) {
        fetchItemMatches();
      } else {
        fetchMyMatches();
      }
    } catch (err) {
      alert('Failed to reject match');
    } finally {
      setActionLoading(null);
    }
  };

  const handleRequestMessage = async (matchId) => {
    const message = prompt('Enter a message to send with your request:');
    if (!message) return;

    try {
      await api.post(`/message-requests/${matchId}`, message, {
        headers: { 'Content-Type': 'text/plain' }
      });
      alert('Message request sent!');
    } catch (err) {
      alert(err.response?.data || 'Failed to send message request');
    }
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="spinner"></div>
        <p>Loading matches...</p>
      </div>
    );
  }

  return (
    <div className="matches-container">
      <div className="container">
        <div className="page-header">
          <h1>{lostItemId ? 'Potential Matches' : 'My Matches'}</h1>
        </div>

        {lostItemId && matches.length === 0 && (
          <div className="empty-state">
            <p>No matches found yet. Check back later!</p>
          </div>
        )}

        {!lostItemId && myMatches.length === 0 && (
          <div className="empty-state">
            <p>You don't have any matches yet.</p>
          </div>
        )}

        <div className="matches-grid">
          {lostItemId ? (
            matches.map((match) => (
              <div key={match.matchId} className="match-card">
                <div className="match-header">
                  <div className="match-score">
                    <span className="score-label">Match Score</span>
                    <span className="score-value">{match.score}%</span>
                  </div>
                  <span className={`match-status ${match.status.toLowerCase()}`}>
                    {match.status}
                  </span>
                </div>

                <div className="match-details">
                  <h3>{match.item.title}</h3>
                  <p><strong>Category:</strong> {match.item.category}</p>
                  <p><strong>Location:</strong> {match.item.location}</p>
                  <p className="match-description">{match.item.description}</p>
                  <p className="match-date">
                    <small>Found on {new Date(match.item.createdAt).toLocaleDateString()}</small>
                  </p>
                </div>

                {match.status === 'PENDING' && (
                  <div className="match-actions">
                    <button
                      onClick={() => handleConfirm(match.matchId)}
                      className="btn btn-success"
                      disabled={actionLoading === match.matchId}
                    >
                      <FiCheck /> Confirm
                    </button>
                    <button
                      onClick={() => handleReject(match.matchId)}
                      className="btn btn-danger"
                      disabled={actionLoading === match.matchId}
                    >
                      <FiX /> Reject
                    </button>
                  </div>
                )}

                {match.status === 'CONFIRMED' && (
                  <button
                    onClick={() => handleRequestMessage(match.matchId)}
                    className="btn btn-primary"
                  >
                    <FiMessageSquare /> Request to Message
                  </button>
                )}
              </div>
            ))
          ) : (
            myMatches.map((match) => (
              <div key={match.matchId} className="match-card">
                <div className="match-header">
                  <div className="match-score">
                    <span className="score-value">{match.score}%</span>
                  </div>
                  <span className={`match-status ${match.status.toLowerCase()}`}>
                    {match.status}
                  </span>
                </div>
                <div className="match-details">
                  <p><strong>Lost:</strong> {match.lostTitle}</p>
                  <p><strong>Found:</strong> {match.foundTitle}</p>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default Matches;
