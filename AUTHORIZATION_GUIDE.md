# Authorization Implementation Guide

This Spring Boot application implements JWT-based authorization using Supabase as the authentication provider.

## Overview

The application uses Spring Security with OAuth2 Resource Server to validate JWT tokens issued by Supabase. The JWKS endpoint is configured to fetch public keys for token validation.

## Configuration

### JWKS Endpoint
- **URL**: `https://imdrhifkhpbmoxfmylhi.supabase.co/auth/v1/.well-known/jwks.json`
- **Purpose**: Provides public keys for JWT signature verification

### Security Configuration
- **File**: `src/main/java/com/example/demo/security/SecurityConfig.java`
- **Features**:
  - JWT token validation using Supabase JWKS
  - CORS configuration for cross-origin requests
  - Role-based access control
  - Public endpoints (no authentication required)
  - Protected endpoints (authentication required)

## API Endpoints

### Public Endpoints (No Authentication Required)
- `GET /api/public/health` - Health check
- `GET /api/posts/nearby` - Get posts near a location
- `GET /api/posts` - Get all posts
- `GET /api/posts/{id}` - Get post by ID

### Protected Endpoints (Authentication Required)
- `GET /api/authenticated` - Get authenticated user info
- `GET /api/profile` - Get user profile with metadata
- `GET /api/user` - User role required
- `GET /api/admin` - Admin role required
- `POST /api/posts` - Create new post (USER role required)
- `PUT /api/posts/{id}` - Update post (owner or ADMIN role required)
- `DELETE /api/posts/{id}` - Delete post (owner or ADMIN role required)
- `GET /api/posts/my-posts` - Get user's own posts (USER role required)

## Authentication

### JWT Token Structure
The application expects JWT tokens with the following claims:
- `sub`: User ID (used as principal)
- `email`: User email address
- `user_metadata`: Custom user data
- `app_metadata`: Application-specific user data
- `roles`: Array of user roles (in user_metadata or app_metadata)

### Role Mapping
- Roles from `user_metadata.roles` or `app_metadata.roles` are mapped to Spring Security authorities
- Format: `ROLE_{ROLE_NAME}` (e.g., "admin" becomes "ROLE_ADMIN")
- Default role: `ROLE_USER` if no roles are specified

## Usage Examples

### 1. Making Authenticated Requests

```bash
# Get user profile
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
     http://localhost:8080/api/profile

# Create a new post
curl -X POST \
     -H "Authorization: Bearer YOUR_JWT_TOKEN" \
     -H "Content-Type: application/json" \
     -d '{"title":"My Post","content":"Post content","latitude":40.7128,"longitude":-74.0060}' \
     http://localhost:8080/api/posts

# Get user's posts
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
     http://localhost:8080/api/posts/my-posts
```

### 2. Role-Based Access

```bash
# Admin endpoint (requires ADMIN role)
curl -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
     http://localhost:8080/api/admin

# User endpoint (requires USER role)
curl -H "Authorization: Bearer USER_JWT_TOKEN" \
     http://localhost:8080/api/user
```

### 3. Public Endpoints

```bash
# Health check
curl http://localhost:8080/api/public/health

# Get nearby posts
curl "http://localhost:8080/api/posts/nearby?lat=40.7128&lon=-74.0060&distance=1000"
```

## Security Features

### 1. JWT Validation
- Automatic signature verification using Supabase public keys
- Token expiration validation
- Issuer validation

### 2. CORS Configuration
- Allows all origins (`*`)
- Supports common HTTP methods
- Includes credentials support
- Exposes Authorization header

### 3. Role-Based Authorization
- Method-level security with `@PreAuthorize`
- Role extraction from JWT claims
- Owner-based resource access control

### 4. Resource Ownership
- Users can only modify their own posts
- Admins can modify any post
- Automatic user ID extraction from JWT

## Error Responses

### 401 Unauthorized
```json
{
  "timestamp": "2024-01-01T00:00:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "path": "/api/posts"
}
```

### 403 Forbidden
```json
{
  "timestamp": "2024-01-01T00:00:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "path": "/api/posts/1"
}
```

## Testing

### 1. Get JWT Token from Supabase
Use Supabase Auth API to get a JWT token:
```javascript
const { data, error } = await supabase.auth.signInWithPassword({
  email: 'user@example.com',
  password: 'password'
});
const token = data.session.access_token;
```

### 2. Test Endpoints
Use the token in Authorization header:
```bash
curl -H "Authorization: Bearer $TOKEN" \
     http://localhost:8080/api/profile
```

## Configuration Files

### application.properties
```properties
# Supabase JWT configuration
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://imdrhifkhpbmoxfmylhi.supabase.co/auth/v1/.well-known/jwks.json
supabase.project.url=https://imdrhifkhpbmoxfmylhi.supabase.co
app.cors.allowed-origins=http://localhost:3000
```

### Dependencies (pom.xml)
- `spring-boot-starter-security`
- `spring-boot-starter-oauth2-resource-server`
- `spring-security-test`

## Troubleshooting

### Common Issues

1. **Invalid JWT Token**
   - Check token expiration
   - Verify token format
   - Ensure token is from correct Supabase project

2. **CORS Errors**
   - Check CORS configuration
   - Verify allowed origins
   - Include credentials in requests

3. **Role Authorization Fails**
   - Check role format in JWT claims
   - Verify role mapping in SecurityConfig
   - Ensure roles are in user_metadata or app_metadata

4. **JWKS Fetch Errors**
   - Verify JWKS endpoint URL
   - Check network connectivity
   - Ensure Supabase project is active

### Debug Mode
Enable debug logging in `application.properties`:
```properties
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.security.oauth2=DEBUG
```
