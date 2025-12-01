# Testing the Frontend-Backend Integration

## Quick Test Steps

### 1. Start the Backend
```powershell
# From project root
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

Wait for: `Started BlogApplication in X seconds`

### 2. Test Backend API (Optional)
```powershell
# Test if backend is running
curl http://localhost:8080/api/v1/blog/hello
```

Should return: `"Hello, World!"`

### 3. Start the Frontend
```powershell
# In a new terminal
cd blogWeb
npm install  # First time only
npm start
```

Browser will auto-open at: http://localhost:4200

### 4. Test the Application

#### Test 1: View Empty Blog List
- Navigate to http://localhost:4200/posts
- Should see "No blog posts yet" message
- ✅ Routing works!

#### Test 2: Create a Blog Post
- Click "Create Post" button in toolbar
- Fill in the form:
  - **Title**: My First Blog Post
  - **Image URL**: https://picsum.photos/800/400
  - **Content**: This is my first blog post content. Testing the frontend integration with Spring Boot backend.
  - **Author**: John Doe
  - **Tags**: (Press Enter after each) `technology`, `spring-boot`, `angular`
- Click "Create Post"
- Should see success message
- Should redirect to posts list
- ✅ Create functionality works!

#### Test 3: View Blog Posts
- Should now see your created post in the list
- Verify it shows:
  - Title
  - Author name
  - Creation date
  - Truncated content
  - Tags
  - View count (0)
  - Like count (0)
- ✅ List and pagination work!

#### Test 4: View Blog Details
- Click on the blog post card
- Should navigate to detail page
- Verify full content is displayed
- ✅ Detail view works!

#### Test 5: Like a Post
- Click the heart icon (favorite_border)
- Should see "Post liked!" notification
- Like count should increase
- ✅ Like functionality works!

#### Test 6: Add a Comment
- Scroll to "Add a Comment" section
- Fill in:
  - **Your Name**: Jane Smith
  - **Comment**: Great post! Very informative.
- Click "Post Comment"
- Should see "Comment added successfully!" notification
- Comment should appear in the list below
- ✅ Comments work!

## Verify CORS Configuration

If you see CORS errors in browser console:
1. Check `CorsConfig.java` has `localhost:4200`
2. Restart the backend
3. Clear browser cache

## Troubleshooting

### Backend Issues
```powershell
# Check if backend is running
curl http://localhost:8080/api/v1/blog/all-posts?page=0&size=10
```

### Frontend Issues
```powershell
# Check Angular version
cd blogWeb
ng version

# Reinstall dependencies
rm -rf node_modules
npm install
```

### Database Issues
Make sure PostgreSQL is running and `.env` file is configured:
```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/blogs
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
```

## Expected Results

✅ **All tests should pass, confirming:**
1. Frontend can communicate with backend API
2. CORS is properly configured
3. Create, Read, Like, and Comment operations work
4. Routing between pages works
5. Angular Material UI displays correctly

## API Endpoints Being Used

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/v1/blog/post` | Create new blog post |
| GET | `/api/v1/blog/all-posts` | Get paginated blog posts |
| GET | `/api/v1/blog/get-details/{id}` | Get single blog post |
| PATCH | `/api/v1/blog/like/{id}` | Like a blog post |
| POST | `/api/v1/comments/{blogId}` | Add comment to post |

## Success Indicators

- ✅ No CORS errors in browser console
- ✅ HTTP requests return 200 status
- ✅ Data displays correctly in UI
- ✅ Notifications appear for user actions
- ✅ Navigation between pages works
- ✅ Form validation works

🎉 If all tests pass, your frontend is fully integrated with the backend!
