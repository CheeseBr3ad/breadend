# JWT Testing Example

This document provides examples of how to test the JWT authentication implementation.

## Prerequisites

1. A valid Supabase JWT token
2. The Spring Boot application running on `http://localhost:8080`

## Getting a JWT Token

### Using Supabase JavaScript Client

```javascript
import { createClient } from '@supabase/supabase-js'

const supabaseUrl = 'https://imdrhifkhpbmoxfmylhi.supabase.co'
const supabaseKey = 'your-anon-key'
const supabase = createClient(supabaseUrl, supabaseKey)

// Sign in to get JWT token
const { data, error } = await supabase.auth.signInWithPassword({
  email: 'user@example.com',
  password: 'password'
})

if (data.session) {
  const jwtToken = data.session.access_token
  console.log('JWT Token:', jwtToken)
}
```

### Using cURL with Supabase REST API

```bash
curl -X POST 'https://imdrhifkhpbmoxfmylhi.supabase.co/auth/v1/token?grant_type=password' \
  -H "apikey: YOUR_ANON_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password"
  }'
```

## Testing API Endpoints

### 1. Test Public Endpoints (No Authentication)

```bash
# Health check
curl http://localhost:8080/api/public/health

# Get nearby posts
curl "http://localhost:8080/api/posts/nearby?lat=40.7128&lon=-74.0060&distance=1000"
```

### 2. Test Protected Endpoints (Authentication Required)

```bash
# Set your JWT token
export JWT_TOKEN="your-jwt-token-here"

# Get user profile
curl -H "Authorization: Bearer $JWT_TOKEN" \
     http://localhost:8080/api/profile

# Get authenticated user info
curl -H "Authorization: Bearer $JWT_TOKEN" \
     http://localhost:8080/api/authenticated
```

### 3. Test Role-Based Endpoints

```bash
# Test USER role endpoint
curl -H "Authorization: Bearer $JWT_TOKEN" \
     http://localhost:8080/api/user

# Test ADMIN role endpoint (requires ADMIN role in JWT)
curl -H "Authorization: Bearer $JWT_TOKEN" \
     http://localhost:8080/api/admin
```

### 4. Test Post Management

```bash
# Create a new post
curl -X POST \
     -H "Authorization: Bearer $JWT_TOKEN" \
     -H "Content-Type: application/json" \
     -d '{
       "title": "My Test Post",
       "content": "This is a test post content",
       "latitude": 40.7128,
       "longitude": -74.0060
     }' \
     http://localhost:8080/api/posts

# Get user's posts
curl -H "Authorization: Bearer $JWT_TOKEN" \
     http://localhost:8080/api/posts/my-posts

# Update a post (replace {id} with actual post ID)
curl -X PUT \
     -H "Authorization: Bearer $JWT_TOKEN" \
     -H "Content-Type: application/json" \
     -d '{
       "title": "Updated Post Title",
       "content": "Updated content",
       "latitude": 40.7128,
       "longitude": -74.0060
     }' \
     http://localhost:8080/api/posts/{id}
```

## Expected Responses

### Successful Authentication
```json
{
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "email": "user@example.com",
  "authorities": ["ROLE_USER"],
  "userMetadata": {},
  "appMetadata": {},
  "isAdmin": false,
  "isUser": true
}
```

### Unauthorized (401)
```json
{
  "timestamp": "2024-01-01T00:00:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "path": "/api/authenticated"
}
```

### Forbidden (403)
```json
{
  "timestamp": "2024-01-01T00:00:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "path": "/api/admin"
}
```

## JWT Token Structure

A typical Supabase JWT token contains:

```json
{
  "aud": "authenticated",
  "exp": 1640995200,
  "iat": 1640908800,
  "iss": "https://imdrhifkhpbmoxfmylhi.supabase.co/auth/v1",
  "sub": "123e4567-e89b-12d3-a456-426614174000",
  "email": "user@example.com",
  "phone": "",
  "app_metadata": {
    "provider": "email",
    "providers": ["email"]
  },
  "user_metadata": {
    "roles": ["USER"]
  },
  "role": "authenticated"
}
```

## Debugging

### Enable Debug Logging

Add to `application.properties`:
```properties
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.security.oauth2=DEBUG
```

### Common Issues

1. **Token Expired**: Check the `exp` claim in your JWT
2. **Invalid Signature**: Ensure you're using the correct JWKS endpoint
3. **Wrong Issuer**: Verify the `iss` claim matches your Supabase project
4. **Missing Roles**: Check that roles are properly set in user_metadata or app_metadata

### JWT Token Validation

You can decode and inspect your JWT token at [jwt.io](https://jwt.io) to verify its contents and structure.
