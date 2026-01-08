# 🔍 Campus Lost & Found

A full-stack platform for students to report and find lost items on campus. Features smart matching, real-time chat, and admin moderation.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.2.0-blue.svg)](https://reactjs.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-orange.svg)](https://www.mysql.com/)

## ✨ Features

- 🔐 JWT Authentication with role-based access
- 📝 Report lost/found items with image uploads
- 🎯 Smart matching algorithm (category, location, description)
- 💬 Real-time chat using WebSocket/STOMP
- 🛡️ Admin moderation dashboard
- 📊 User profiles and activity tracking

## 🚀 Tech Stack

**Frontend:** React, React Router, Axios, WebSocket  
**Backend:** Spring Boot, Spring Data JPA, JWT, BCrypt  
**Database:** MySQL 8.0

## ⚡ Quick Start

### Prerequisites
- Java 21+, Node.js 14+, MySQL 8.0+

### Database Setup
```bash
mysql -u root -p
```
```sql
CREATE DATABASE campus_lost_found;
CREATE USER 'campus_user'@'localhost' IDENTIFIED BY 'Password@123';
GRANT ALL PRIVILEGES ON campus_lost_found.* TO 'campus_user'@'localhost';
FLUSH PRIVILEGES;
```

### Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
Runs on `http://localhost:8080`

### Frontend
```bash
cd frontend
npm install
npm start
```
Runs on `http://localhost:3000`

### Default Admin Login
- Email: `****@campus.edu`
- Password: `****`

## 📁 Project Structure
```
├── backend/          # Spring Boot application
│   ├── controller/   # REST endpoints
│   ├── entity/       # JPA entities
│   ├── repository/   # Data access layer
│   └── security/     # JWT & authentication
└── frontend/         # React application
    ├── components/   # Reusable components
    ├── pages/        # Page components
    └── services/     # API calls
```

## 🔌 Key Endpoints

```
POST   /api/auth/register          # Register user
POST   /api/auth/login             # Login
POST   /api/lost-items             # Report lost item
POST   /api/found-items            # Report found item
GET    /api/match/lost/{id}        # Get matches
POST   /api/matches/{id}/confirm   # Confirm match
WS     /ws                          # WebSocket chat
```
---

**Built with ❤️ for campus communities**
