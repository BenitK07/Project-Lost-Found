# Campus Lost & Found - Frontend

A modern, responsive React frontend for the Campus Lost & Found application with a clean neutral theme and excellent UI/UX.

## Features

- **Authentication**: Login and registration with JWT
- **Report Lost Items**: Students can report lost items with photos
- **Report Found Items**: Students can report found items with photos
- **Smart Matching**: View potential matches with scoring
- **Match Management**: Confirm/reject matches
- **Messaging System**: Request to message potential matches
- **Admin Dashboard**: Comprehensive admin controls
- **Responsive Design**: Works on all devices

## Tech Stack

- React 18
- React Router v6
- Axios for API calls
- React Icons
- WebSocket support (STOMP)

## Color Theme

The application uses a professional neutral color palette:
- Primary: Slate gray (#4A5568)
- Secondary: Sky blue (#63B3ED)
- Accent: Green (#48BB78)
- Background: Light grays (#F7FAFC, #EDF2F7)

## Setup Instructions

### Prerequisites

- Node.js (v14 or higher)
- npm or yarn
- Backend API running on http://localhost:8080

### Installation

1. Install dependencies:
```bash
npm install
```

2. Start the development server:
```bash
npm start
```

The app will open at http://localhost:3000

### Building for Production

```bash
npm run build
```

This creates an optimized production build in the `build` folder.

## Project Structure

```
src/
├── components/         # Reusable components
│   └── Navigation.js   # Main navigation bar
├── contexts/          # React contexts
│   └── AuthContext.js # Authentication state
├── pages/             # Page components
│   ├── Home.js
│   ├── Login.js
│   ├── Register.js
│   ├── CreateLostItem.js
│   ├── CreateFoundItem.js
│   ├── MyItems.js
│   └── Matches.js
├── services/          # API services
│   └── api.js         # Axios configuration
├── App.js             # Main app component
├── index.js           # Entry point
└── index.css          # Global styles
```

## Default Admin Credentials

- Email: admin@campus.edu
- Password: admin@123

## API Configuration

The app is configured to connect to the backend API at `http://localhost:8080/api`.

To change this, update the `baseURL` in `src/services/api.js`:

```javascript
const api = axios.create({
  baseURL: 'http://your-backend-url/api',
  // ...
});
```

## Features Overview

### For Students

1. **Report Lost Items**
   - Fill in details (title, category, location, description)
   - Upload multiple images
   - Items go through admin approval

2. **Report Found Items**
   - Similar process to lost items
   - Help reunite items with owners

3. **View My Items**
   - See all reported lost and found items
   - Check approval status
   - View potential matches for approved items

4. **Matches**
   - Smart matching algorithm scores potential matches
   - Confirm or reject matches
   - Request to message confirmed matches

### For Admins

1. **Dashboard**
   - View system statistics
   - Monitor activity

2. **Moderation**
   - Approve or reject submitted items
   - Maintain quality control

3. **Reports**
   - Handle abuse reports
   - Take appropriate actions

## Design Principles

1. **Clean & Modern**: Minimalist design with focus on usability
2. **Neutral Theme**: Professional gray and blue color scheme
3. **Responsive**: Works seamlessly on mobile, tablet, and desktop
4. **Accessible**: Good color contrast and readable fonts
5. **Intuitive**: Clear navigation and user flows

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Troubleshooting

### Cannot connect to backend

- Ensure the backend is running on http://localhost:8080
- Check CORS configuration in the backend
- Verify the API baseURL in `src/services/api.js`

### Images not uploading

- Check file size limits
- Verify the backend image storage service is configured
- Ensure the uploads directory exists and has write permissions

### WebSocket connection issues

- Verify WebSocket endpoint is accessible
- Check for proxy configuration issues
- Ensure STOMP is properly configured in the backend

## Future Enhancements

- Real-time notifications
- Advanced search and filtering
- Image recognition for better matching
- Email notifications
- Mobile app version
- Dark mode

## License

This project is part of the Campus Lost & Found system.
