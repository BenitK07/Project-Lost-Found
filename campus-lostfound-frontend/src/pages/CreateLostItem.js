import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import { FiAlertCircle, FiCheckCircle, FiUpload } from 'react-icons/fi';
import '../pages/Auth.css';
import './ItemForm.css';

const CreateLostItem = () => {
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    category: '',
    location: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);
  const [itemId, setItemId] = useState(null);
  const [images, setImages] = useState([]);
  const navigate = useNavigate();

  const categories = [
    'Electronics',
    'Keys',
    'Wallet',
    'Phone',
    'Laptop',
    'Bag',
    'Jewelry',
    'Clothing',
    'Books',
    'ID Card',
    'Other'
  ];

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess(false);
    setLoading(true);

    try {
      const response = await api.post('/lost-items', formData);
      
      // Get the item ID from response or fetch the user's items
      const myItemsResponse = await api.get('/lost-items/my');
      const latestItem = myItemsResponse.data[myItemsResponse.data.length - 1];
      
      setItemId(latestItem.id);
      setSuccess(true);
      
      setTimeout(() => {
        navigate('/my-items');
      }, 2000);
    } catch (err) {
      setError(err.response?.data || 'Failed to create item. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleImageUpload = async (e) => {
    const files = Array.from(e.target.files);
    
    if (!itemId) {
      setError('Please create the item first before uploading images');
      return;
    }

    for (const file of files) {
      const formData = new FormData();
      formData.append('file', file);

      try {
        await api.post(`/images/lost/${itemId}`, formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        });
        setImages([...images, file.name]);
      } catch (err) {
        setError('Failed to upload image');
      }
    }
  };

  return (
    <div className="item-form-container">
      <div className="item-form-card">
        <div className="form-header">
          <FiAlertCircle className="header-icon lost-icon" />
          <h1>Report Lost Item</h1>
          <p>Provide details about the item you've lost</p>
        </div>

        {error && (
          <div className="alert alert-error">
            <FiAlertCircle />
            {error}
          </div>
        )}

        {success && (
          <div className="alert alert-success">
            <FiCheckCircle />
            Item reported successfully! You can now upload images. Redirecting...
          </div>
        )}

        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label htmlFor="title">Item Title *</label>
            <input
              type="text"
              id="title"
              name="title"
              value={formData.title}
              onChange={handleChange}
              placeholder="e.g., Black iPhone 13"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="category">Category *</label>
            <select
              id="category"
              name="category"
              value={formData.category}
              onChange={handleChange}
              required
            >
              <option value="">Select a category</option>
              {categories.map(cat => (
                <option key={cat} value={cat}>{cat}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="location">Last Seen Location *</label>
            <input
              type="text"
              id="location"
              name="location"
              value={formData.location}
              onChange={handleChange}
              placeholder="e.g., Library 3rd Floor"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="description">Description *</label>
            <textarea
              id="description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              placeholder="Provide detailed description including color, brand, distinctive features..."
              required
            />
          </div>

          {!success && (
            <button type="submit" className="btn btn-primary" disabled={loading}>
              {loading ? 'Submitting...' : 'Report Lost Item'}
            </button>
          )}
        </form>

        {success && itemId && (
          <div className="image-upload-section">
            <label className="upload-label">
              <FiUpload />
              Upload Images (Optional)
              <input
                type="file"
                multiple
                accept="image/*"
                onChange={handleImageUpload}
                style={{ display: 'none' }}
              />
            </label>
            {images.length > 0 && (
              <div className="uploaded-images">
                {images.map((img, idx) => (
                  <span key={idx} className="image-badge">{img}</span>
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default CreateLostItem;
