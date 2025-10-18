# Docker Guide for Spring Boot JWT Authorization App

This guide explains how to build, run, and deploy the Spring Boot application with JWT authorization using Docker.

## Files Overview

- `Dockerfile` - Development Dockerfile with multi-stage build
- `Dockerfile.prod` - Production-optimized Dockerfile
- `docker-compose.yml` - Complete stack with PostgreSQL and app
- `.dockerignore` - Files to exclude from Docker build context

## Quick Start

### 1. Using Docker Compose (Recommended)

```bash
# Build and start all services
docker-compose up --build

# Run in background
docker-compose up -d --build

# View logs
docker-compose logs -f app

# Stop services
docker-compose down
```

### 2. Using Docker directly

```bash
# Build the image
docker build -t demo4-app .

# Run with external database
docker run -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  -e DB_PORT=5433 \
  -e DB_NAME=mydatabase \
  -e DB_USER=myuser \
  -e DB_PASSWORD=mysecretpassword \
  demo4-app
```

## Environment Variables

### Database Configuration
- `DB_HOST` - Database host (default: postgres for docker-compose, host.docker.internal for standalone)
- `DB_PORT` - Database port (default: 5432)
- `DB_NAME` - Database name (default: mydatabase)
- `DB_USER` - Database user (default: myuser)
- `DB_PASSWORD` - Database password (default: mysecretpassword)

### JVM Configuration
- `JAVA_OPTS` - JVM options (optimized for containers)
- `SPRING_PROFILES_ACTIVE` - Spring profile (default: docker)

## Docker Features

### Security
- ✅ Non-root user execution
- ✅ Security updates applied
- ✅ Minimal attack surface (Alpine Linux)
- ✅ Health checks included

### Performance
- ✅ Multi-stage build for smaller image size
- ✅ JVM optimized for containers
- ✅ G1 garbage collector
- ✅ Memory limits configured

### Monitoring
- ✅ Health check endpoint
- ✅ Container health status
- ✅ Logging configuration

## Production Deployment

### Using Production Dockerfile

```bash
# Build production image
docker build -f Dockerfile.prod -t demo4-app:prod .

# Run production container
docker run -d \
  --name demo4-app-prod \
  -p 8080:8080 \
  -e DB_HOST=your-db-host \
  -e DB_USER=your-db-user \
  -e DB_PASSWORD=your-db-password \
  --restart unless-stopped \
  demo4-app:prod
```

### Docker Compose for Production

```yaml
version: '3.8'

services:
  app:
    build:
      context: .
      dockerfile: Dockerfile.prod
    ports:
      - "8080:8080"
    environment:
      DB_HOST: ${DB_HOST}
      DB_USER: ${DB_USER}
      DB_PASSWORD: ${DB_PASSWORD}
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/api/public/health"]
      interval: 30s
      timeout: 10s
      retries: 3
```

## Health Checks

The application includes health checks at multiple levels:

### Application Health
- Endpoint: `GET /api/public/health`
- Response: `"Service is healthy!"`

### Container Health
- Docker health check every 30 seconds
- Uses curl to check the health endpoint
- 3 retries before marking as unhealthy

### Database Health
- PostgreSQL health check in docker-compose
- Ensures database is ready before starting the app

## Monitoring and Logs

### View Logs
```bash
# All services
docker-compose logs

# Specific service
docker-compose logs app

# Follow logs
docker-compose logs -f app
```

### Container Status
```bash
# Check container health
docker ps

# Check health status
docker inspect demo4-app | grep Health -A 10
```

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   ```bash
   # Check if database is running
   docker-compose ps postgres
   
   # Check database logs
   docker-compose logs postgres
   ```

2. **Application Won't Start**
   ```bash
   # Check application logs
   docker-compose logs app
   
   # Check if port is available
   netstat -tulpn | grep 8080
   ```

3. **JWT Validation Errors**
   - Verify Supabase project URL
   - Check JWKS endpoint accessibility
   - Ensure JWT token is valid

### Debug Mode

```bash
# Run with debug logging
docker run -e JAVA_OPTS="-Dlogging.level.org.springframework.security=DEBUG" demo4-app
```

## Development Workflow

### Local Development
```bash
# Start database only
docker-compose up postgres -d

# Run app locally with Maven
mvn spring-boot:run

# Or run with Docker
docker-compose up app
```

### Testing
```bash
# Run tests
mvn test

# Run integration tests
mvn verify

# Test with Docker
docker-compose up --build
curl http://localhost:8080/api/public/health
```

## Image Optimization

### Image Size
- Base image: `eclipse-temurin:21-jre-alpine` (~200MB)
- Final image: ~300MB (includes app + dependencies)

### Build Optimization
- Multi-stage build reduces final image size
- Layer caching with separate COPY commands
- `.dockerignore` excludes unnecessary files

### Runtime Optimization
- JVM tuned for containers
- Memory limits configured
- G1 garbage collector for better performance

## Security Considerations

### Container Security
- Non-root user execution
- Minimal base image (Alpine)
- Security updates applied
- No unnecessary packages

### Application Security
- JWT token validation
- CORS properly configured
- Role-based access control
- Input validation

### Network Security
- Internal database communication
- Exposed ports minimized
- Health checks for monitoring

## Scaling

### Horizontal Scaling
```bash
# Scale the application
docker-compose up --scale app=3

# Use load balancer (nginx, traefik, etc.)
```

### Resource Limits
```yaml
services:
  app:
    deploy:
      resources:
        limits:
          memory: 512M
          cpus: '0.5'
        reservations:
          memory: 256M
          cpus: '0.25'
```

This Docker setup provides a robust, secure, and scalable foundation for your Spring Boot JWT authorization application.
