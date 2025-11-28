# Blog Application - Quick Start Guide

## 🚀 Quick Start (Recommended)

### Option 1: Use the Start Script (Windows)
```powershell
.\start-app.ps1
```
This script will:
- Check for PostgreSQL connection
- Create a sample .env file if missing
- Start the backend in a new window
- Install npm packages if needed
- Start the frontend in a new window
- Open the browser automatically

### Option 2: Manual Start

#### Step 1: Setup Environment Variables
Copy `.env.example` to `.env` and update with your database credentials:
```bash
copy .env.example .env
```

#### Step 2: Start Backend
```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Step 3: Start Frontend (in a new terminal)
```powershell
cd blogWeb
npm install   # First time only
npm start
```

## 📋 Prerequisites Checklist
- [ ] Java 17+ installed
- [ ] Node.js 18+ installed
- [ ] PostgreSQL installed and running
- [ ] Database `blogs` created
- [ ] .env file configured

## 🌐 Application URLs
- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080/api/v1
- **API Docs**: http://localhost:8080/api/v1/blog/hello (test endpoint)

## 📱 Features

### Available Pages
1. **Blog List** (`/posts`) - Home page showing all blog posts
2. **Blog Detail** (`/blog/:id`) - Individual blog post with comments
3. **Create Post** (`/create-post`) - Form to create new blog posts

### Functionality
✅ Create blog posts with images and tags
✅ View all blog posts with pagination
✅ Like blog posts
✅ Add comments to blog posts
✅ View post details with full content
✅ Responsive design for mobile and desktop

## 🎨 Technology Stack

### Backend
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Lombok
- Maven

### Frontend
- Angular 19
- Angular Material
- RxJS
- TypeScript

## 📁 Key Files Created

### Services
- `blogWeb/src/app/services/blog.service.ts` - Blog API integration
- `blogWeb/src/app/services/comment.service.ts` - Comment API integration

### Components
- `blogWeb/src/app/pages/blog-list/` - List all blogs with pagination
- `blogWeb/src/app/pages/blog-detail/` - Blog details with comments
- `blogWeb/src/app/pages/create-post/` - Create new blog post

### Configuration
- `src/main/java/learn/blogfiles/blog/config/CorsConfig.java` - CORS configuration
- `blogWeb/src/app/app-routing.module.ts` - Route configuration

## 🔧 Common Issues

### Backend won't start
- Ensure PostgreSQL is running
- Check .env file has correct credentials
- Verify database `blogs` exists

### Frontend won't start
- Run `npm install` in blogWeb directory
- Clear node_modules and reinstall: `rm -rf node_modules; npm install`

### CORS errors
- Ensure backend is running on port 8080
- Frontend must run on port 4200
- CORS is configured for localhost:4200

### TypeScript/Module errors
- These are development warnings only
- Run `npm install` to ensure all dependencies are installed
- The application will work despite these warnings

## 🎯 Next Steps

1. Start the application using `.\start-app.ps1`
2. Create your first blog post at http://localhost:4200/create-post
3. View all posts at http://localhost:4200/posts
4. Click on a post to view details and add comments

## 📝 API Documentation

### Blog Endpoints
```
POST   /api/v1/blog/post              - Create blog post
GET    /api/v1/blog/all-posts         - Get all posts (paginated)
GET    /api/v1/blog/get-details/{id}  - Get post by ID
PATCH  /api/v1/blog/like/{id}         - Like a post
```

### Comment Endpoints
```
POST   /api/v1/comments/{blogId}      - Add comment to post
```

## 🎉 You're Ready!
Your blog application is now fully set up and ready to use locally!
