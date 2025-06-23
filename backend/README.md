# CaRatan Backend - Java 22 with MySQL

This is the new backend for the CaRatan Car Dealership Application, built with Java 22, Spring Boot, and MySQL using JDBC/JEE patterns.

## Technology Stack

- **Java 22** - Latest LTS version with JEE features
- **Spring Boot 3.2+** - Modern web framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations with JPA
- **JDBC/JEE** - Direct database access using JDBC patterns
- **MySQL 8.0+** - Database with JDBC connector
- **JWT** - Token-based authentication
- **Maven** - Build tool

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── caratan/
│   │   │           ├── CaratanApplication.java
│   │   │           ├── config/
│   │   │           │   ├── DatabaseConfig.java
│   │   │           │   ├── SecurityConfig.java
│   │   │           │   └── DataInitializer.java
│   │   │           ├── controller/
│   │   │           ├── dto/
│   │   │           ├── entity/
│   │   │           ├── repository/
│   │   │           ├── service/
│   │   │           └── security/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/
│   │           └── init.sql
│   └── test/
├── pom.xml
└── README.md
```

## Database Setup

### 1. Install MySQL
- Download and install MySQL 8.0+ from [MySQL Official Site](https://dev.mysql.com/downloads/mysql/)
- Or use Docker: `docker run --name mysql-caratan -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=Car_DB -p 3306:3306 -d mysql:8.0`

### 2. Create Database and User
```sql
-- Connect to MySQL as root
mysql -u root -p

-- Create database
CREATE DATABASE Car_DB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user
CREATE USER 'namer'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON Car_DB.* TO 'user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Initialize Database
```bash
# Run the initialization script
mysql -u user -p Car_DB < src/main/resources/db/init.sql
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout

### User Management
- `GET /api/users/profile` - Get user profile
- `PUT /api/users/profile` - Update user profile

### Car Management (JPA + JDBC)
- `GET /api/cars` - Get all cars (JPA)
- `GET /api/cars/{id}` - Get car by ID (JPA + JDBC for view increment)
- `POST /api/cars` - Add new car (JPA)
- `PUT /api/cars/{id}` - Update car (JPA)
- `DELETE /api/cars/{id}` - Delete car (JPA)

### Car Search (JDBC)
- `GET /api/cars/search?q=term` - Search cars by make, model, or description
- `GET /api/cars/price-range?minPrice=X&maxPrice=Y` - Filter by price range
- `GET /api/cars/year-range?minYear=X&maxYear=Y` - Filter by year range
- `GET /api/cars/makes` - Get popular car makes

## JDBC/JEE Features

### Database Configuration
- **DatabaseConfig.java** - Configures MySQL connection using JDBC
- **JdbcTemplate** - Spring's JDBC abstraction for database operations
- **RowMapper** - Custom row mapping for complex queries

### Service Layer
- **CarService.java** - Demonstrates both JPA and JDBC usage:
  - JPA for basic CRUD operations
  - JDBC for complex queries and performance-critical operations
  - RowMapper for custom result mapping

### Key JDBC Operations
```java
// Complex search query
public List<Car> searchCars(String searchTerm) {
    String sql = "SELECT * FROM cars WHERE make LIKE ? OR model LIKE ? OR description LIKE ?";
    return jdbcTemplate.query(sql, new CarRowMapper(), searchPattern, searchPattern, searchPattern);
}

// Performance-critical update
public void incrementViews(Long carId) {
    String sql = "UPDATE cars SET views = views + 1 WHERE id = ?";
    jdbcTemplate.update(sql, carId);
}

// Aggregation query
public List<String> getPopularMakes() {
    String sql = "SELECT make, COUNT(*) as count FROM cars GROUP BY make ORDER BY count DESC LIMIT 10";
    return jdbcTemplate.queryForList(sql, String.class);
}
```

## Setup Instructions

1. **Install Java 22**
   ```bash
   # Download and install Java 22 from Oracle or OpenJDK
   java -version
   ```

2. **Install MySQL 8.0+**
   ```bash
   # Install MySQL server
   mysql --version
   ```

3. **Set up Database**
   ```bash
   # Create database and user
   mysql -u root -p < setup_database.sql
   
   # Initialize with sample data
   mysql -u caratan_user -p Car_DB < src/main/resources/db/init.sql
   ```

4. **Configure Application**
   ```bash
   # Update application.properties if needed
   # Default settings:
   # - Host: localhost:3306
   # - Database: Car_DB
   # - User: caratan_user
   # - Password: caratan_password
   ```

5. **Run Application**
   ```bash
   cd backend
   mvn spring-boot:run
   ```

## Database Schema

### Users Table
- `id` - Primary key
- `full_name` - User's full name
- `email` - Unique email address
- `password` - BCrypt hashed password
- `phone` - Phone number
- `profile_pic` - Profile picture URL
- `is_admin` - Admin flag
- `created_at` - Creation timestamp
- `updated_at` - Last update timestamp

### Cars Table
- `id` - Primary key
- `make` - Car make (Toyota, Honda, etc.)
- `model` - Car model (Camry, Civic, etc.)
- `type` - Car type (Sedan, SUV, etc.)
- `year` - Manufacturing year
- `color` - Car color
- `mileage` - Mileage in kilometers
- `transmission` - Transmission type
- `fuel_type` - Fuel type
- `price` - Price in USD
- `description` - Car description
- `photos` - JSON array of photo URLs
- `views` - View count
- `seller_id` - Foreign key to users table
- `created_at` - Creation timestamp
- `updated_at` - Last update timestamp

## Testing

### Test Database Connection
```bash
# Test MySQL connection
mysql -u caratan_user -p Car_DB -e "SELECT COUNT(*) FROM users;"
```

### Test API Endpoints
```bash
# Register user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Test User","email":"test@example.com","password":"password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@example.com","password":"password123"}'

# Get cars
curl http://localhost:8080/api/cars

# Search cars
curl "http://localhost:8080/api/cars/search?q=Toyota"
```

## Performance Features

- **Connection Pooling** - HikariCP for efficient database connections
- **Indexed Queries** - Optimized database indexes for fast searches
- **JDBC for Complex Queries** - Direct SQL for performance-critical operations
- **JPA for Simple CRUD** - Object-relational mapping for basic operations 