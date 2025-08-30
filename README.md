# Hydro Application - Spring Security with JWT

A Spring Boot application with comprehensive JWT-based authentication and authorization system.

## Features

- **JWT Authentication**: Secure token-based authentication
- **Role-based Authorization**: USER, MODERATOR, and ADMIN roles
- **MySQL Database**: Persistent user and role storage
- **RESTful API**: Complete authentication endpoints
- **Security Configuration**: Comprehensive security rules and filters
- **Exception Handling**: Global exception handling with proper error responses
- **CORS Support**: Cross-origin resource sharing configuration

## Technology Stack

- **Spring Boot 3.5.5**
- **Spring Security 6.x**
- **Spring Data JPA**
- **MySQL Database**
- **JWT (JSON Web Tokens)**
- **Maven**
- **Java 21**

## Database Setup

### Option 1: Using Migration Scripts (Recommended)

The project includes comprehensive database migration scripts in `src/main/resources/db/migration/`:

#### For Windows Users:
```bash
cd src/main/resources/db/migration
run_migration.bat
```

#### For Linux/Mac Users:
```bash
cd src/main/resources/db/migration
chmod +x run_migration.sh
./run_migration.sh
```

#### Manual Execution:
```bash
mysql -u root -p < src/main/resources/db/migration/manual_setup.sql
```

### Option 2: Manual Setup

1. Create the MySQL database:
```sql
CREATE DATABASE hydro_db;
```

2. Grant permissions to the user:
```sql
GRANT ALL PRIVILEGES ON hydro_db.* TO 'local_wprod'@'localhost';
FLUSH PRIVILEGES;
```

### Migration Scripts Overview

- **`manual_setup.sql`** - Basic database setup with tables and indexes
- **`V1__init_database.sql`** - Complete setup with views, procedures, and triggers
- **`run_migration.bat`** - Windows batch script for easy execution
- **`run_migration.sh`** - Linux/Mac shell script for easy execution
- **`README.md`** - Detailed documentation for the migration scripts

See `src/main/resources/db/migration/README.md` for complete documentation.

## Configuration

The application is configured with the following default settings in `application.properties`:

- **Database**: MySQL on localhost:3306
- **JWT Secret**: `hydroJwtSecretKeyForDevelopmentOnlyChangeInProduction`
- **JWT Expiration**: 24 hours (86400000 ms)
- **Server Port**: 8080

## API Endpoints

### Authentication Endpoints

#### 1. User Registration
```http
POST /api/auth/signup
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "roles": ["USER"]
}
```

#### 2. User Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "testuser",
  "password": "password123"
}
```

#### 3. Get Current User
```http
GET /api/auth/me
Authorization: Bearer <jwt_token>
```

### Test Endpoints

#### 1. Public Access
```http
GET /api/test/all
```

#### 2. User Access (requires authentication)
```http
GET /api/test/user
Authorization: Bearer <jwt_token>
```

#### 3. Moderator Access
```http
GET /api/test/mod
Authorization: Bearer <jwt_token>
```

#### 4. Admin Access
```http
GET /api/test/admin
Authorization: Bearer <jwt_token>
```

## Security Rules

### Public Endpoints
- `/api/auth/**` - Authentication endpoints
- `/api/public/**` - Public API endpoints
- `/h2-console/**` - H2 Console (if enabled)
- `/swagger-ui/**`, `/v3/api-docs/**` - API Documentation
- `/actuator/**` - Spring Boot Actuator

### Protected Endpoints
- All other endpoints require authentication
- Role-based access control using `@PreAuthorize` annotations

## Running the Application

1. **Prerequisites**:
   - Java 21 or later
   - MySQL 8.0 or later
   - Maven 3.6 or later

2. **Build the application**:
   ```bash
   mvn clean install
   ```

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**:
   - Application: http://localhost:8080
   - API Base URL: http://localhost:8080/api

## Database Secutity Schema

### Users Table
- `id` - Primary key, auto-increment
- `username` - Unique username (VARCHAR(50))
- `email` - Unique email (VARCHAR(255))
- `password` - Encrypted password (VARCHAR(255))
- `first_name` - User's first name (VARCHAR(100))
- `last_name` - User's last name (VARCHAR(100))
- `enabled` - Account enabled status (BOOLEAN)
- `account_non_expired` - Account expiration status (BOOLEAN)
- `account_non_locked` - Account lock status (BOOLEAN)
- `credentials_non_expired` - Password expiration status (BOOLEAN)
- `created_at` - Account creation timestamp (DATETIME)
- `updated_at` - Last update timestamp (DATETIME)

### Roles Table
- `id` - Primary key, auto-increment
- `name` - Unique role name (VARCHAR(50))
- `description` - Role description (VARCHAR(255))

### User Roles Table (Many-to-Many)
- `user_id` - Foreign key to users table
- `role_id` - Foreign key to roles table

Note: Composite primary key on both columns

## Default Roles

The application automatically creates the following roles on startup:
- **USER**: Default user role
- **MODERATOR**: Elevated privileges
- **ADMIN**: Full administrative access

## JWT Token Structure

The JWT token contains:
- **Subject**: Username
- **Issued At**: Token creation time
- **Expiration**: 24 hours from creation
- **Algorithm**: HS512

## Security Features

1. **Password Encryption**: BCrypt password hashing
2. **JWT Token Validation**: Secure token verification
3. **CORS Configuration**: Cross-origin request handling
4. **Session Management**: Stateless sessions
5. **Role-based Access Control**: Fine-grained authorization
6. **Input Validation**: Bean validation for all inputs
7. **Exception Handling**: Comprehensive error handling

## Development Notes

- The JWT secret should be changed in production
- Database credentials should be externalized
- Consider implementing refresh tokens for better security
- Add rate limiting for production use
- Implement proper logging and monitoring

## Testing

You can test the API using tools like:
- Postman
- cURL
- Insomnia
- Any REST client

## Example cURL Commands

### Register a new user:
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "admin@example.com",
    "password": "admin123",
    "firstName": "Admin",
    "lastName": "User",
    "roles": ["ADMIN"]
  }'
```

### Login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "admin",
    "password": "admin123"
  }'
```

### Access protected endpoint:
```bash
curl -X GET http://localhost:8080/api/test/admin \
  -H "Authorization: Bearer <your_jwt_token>"
```
