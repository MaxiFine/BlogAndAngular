# Docker Deployment Guide

## Building and Running with Docker

### Build Individual Images

#### Backend
```bash
docker build -t blog-backend:latest .
```

#### Frontend
```bash
cd blogWeb
docker build -t blog-frontend:latest .
```

### Using Docker Compose

#### Start All Services
```bash
docker-compose up -d
```

#### Stop All Services
```bash
docker-compose down
```

#### Rebuild and Start
```bash
docker-compose up -d --build
```

#### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f blog-frontend
docker-compose logs -f blog-app
```

### Access the Application

- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080/api/v1

### Docker Image Details

#### Frontend Image (blog-frontend)
- **Base**: nginx:alpine
- **Build Stage**: node:18-alpine
- **Port**: 80 (mapped to 4200 on host)
- **Features**:
  - Multi-stage build for optimized image size
  - Production Angular build
  - Nginx for serving static files
  - Gzip compression enabled
  - Custom nginx configuration for Angular routing
  - Health check endpoint

#### Backend Image (blog-app)
- **Port**: 8080 (mapped to 8080 on host)
- **Network**: blog-app-network (bridge)

### Environment Variables

Create a `.env` file in the root directory:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/blogs
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password
```

### Troubleshooting

#### Frontend can't connect to backend
- Ensure both containers are on the same network
- Check backend is accessible at http://localhost:8080
- Update API URL in frontend if needed

#### Port already in use
```bash
# Change ports in docker-compose.yml
ports:
  - 8081:8080  # Backend
  - 4201:80    # Frontend
```

#### Rebuild after code changes
```bash
docker-compose down
docker-compose up -d --build
```

### Production Deployment

#### Push Images to Registry
```bash
# Tag images
docker tag blog-frontend:latest maxfine22/blog-frontend:v1.0
docker tag blog-backend:latest maxfine22/blog-backend:v1.0

# Push to Docker Hub
docker push maxfine22/blog-frontend:v1.0
docker push maxfine22/blog-backend:v1.0
```

#### Pull and Run on Server
```bash
docker-compose pull
docker-compose up -d
```
