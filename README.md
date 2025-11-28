# Blog Application - Setup Instructions

## Overview
This is a full-stack blog application with a Spring Boot backend and Angular frontend.

## Prerequisites
- Java 17 or higher
- Node.js 18+ and npm
- PostgreSQL database
- Maven (or use the included mvnw)

## Backend Setup (Spring Boot)

### 1. Configure Database
Create a `.env` file in the root directory with the following properties:

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/blogs
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
```

### 2. Create Database
```sql
CREATE DATABASE blogs;
```

### 3. Start the Backend
```bash
# Using Maven wrapper (Windows)
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev

# Or using Maven wrapper (Unix/Mac)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Or if you have Maven installed
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The backend will start on `http://localhost:8080`

API Base URL: `http://localhost:8080/api/v1`

## Frontend Setup (Angular)

### 1. Navigate to Frontend Directory
```bash
cd blogWeb
```

### 2. Install Dependencies
```bash
npm install
```

### 3. Start the Development Server
```bash
npm start
```

Or:
```bash
ng serve
```

The frontend will start on `http://localhost:4200`

## Using the Application

### Access the Application
Open your browser and navigate to `http://localhost:4200`

### Features Available:
1. **View All Posts** - The home page displays all blog posts with pagination
2. **Create Post** - Click "Create Post" button in the toolbar to create a new blog post
3. **View Post Details** - Click on any blog post card to view full details
4. **Like Posts** - Like posts from the list view or detail view
5. **Add Comments** - Add comments on individual blog posts
6. **Tags** - Add and view tags on blog posts

## API Endpoints

### Blog Endpoints
- `POST /api/v1/blog/post` - Create a new blog post
- `GET /api/v1/blog/all-posts` - Get all blog posts (paginated)
- `GET /api/v1/blog/get-details/{blogId}` - Get blog details by ID
- `GET /api/v1/blog/find-name?name={name}` - Search blog by name
- `PUT /api/v1/blog/update-blog/{blogId}` - Update a blog post
- `PATCH /api/v1/blog/like/{postId}` - Like a post

### Comment Endpoints
- `POST /api/v1/comments/{blogId}` - Add a comment to a blog post

## Project Structure

```
BlogAndAngular/
├── src/main/java/                    # Spring Boot backend
│   └── learn/blogfiles/blog/
│       ├── controller/               # REST controllers
│       ├── service/                  # Business logic
│       ├── repository/               # Data access
│       ├── model/                    # Entity models
│       └── dtos/                     # Data transfer objects
├── blogWeb/                          # Angular frontend
│   └── src/app/
│       ├── services/                 # API services
│       ├── pages/                    # Page components
│       │   ├── blog-list/           # List all blogs
│       │   ├── blog-detail/         # Blog detail with comments
│       │   └── create-post/         # Create new post
│       └── app-routing.module.ts    # Route configuration
└── pom.xml                          # Maven configuration
```

## Troubleshooting

### Backend Issues
- **Database Connection Error**: Check your `.env` file and ensure PostgreSQL is running
- **Port Already in Use**: Change the port in `application-dev.properties`

### Frontend Issues
- **Cannot connect to backend**: Ensure the backend is running on port 8080
- **CORS errors**: The backend has CORS configured for `http://localhost:4200`
- **Module not found**: Run `npm install` in the blogWeb directory

## Development Tips

### Backend Hot Reload
The Spring Boot DevTools is included for automatic restart during development.

### Frontend Hot Reload
Angular CLI automatically reloads the page when you make changes to the code.

### Database Schema
The application uses JPA with `ddl-auto=update`, so tables will be created automatically.

## Default Configuration

- Backend Port: `8080`
- Frontend Port: `4200`
- API Context Path: `/api/v1`
- Database: PostgreSQL
- Default Page Size: 9 posts per page

## Support
For issues or questions, please check the application logs in both backend and frontend consoles.
