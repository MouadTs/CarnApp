-- CaRatan Database Initialization Script
-- MySQL Database Setup

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS Car_DB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use the database
USE Car_DB;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    profile_pic VARCHAR(255),
    is_admin BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email)
);

-- Create cars table
CREATE TABLE IF NOT EXISTS cars (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    color VARCHAR(30) NOT NULL,
    mileage INT NOT NULL,
    transmission VARCHAR(20) NOT NULL,
    fuel_type VARCHAR(20) NOT NULL,
    engine_capacity VARCHAR(10) DEFAULT '1500',
    cylinder VARCHAR(10) DEFAULT '4',
    seats VARCHAR(5) DEFAULT '5',
    price DECIMAL(10,2) NOT NULL,
    description TEXT,
    photos VARCHAR(500),
    views INT DEFAULT 0,
    location VARCHAR(100) DEFAULT 'Jakarta',
    seller_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_make (make),
    INDEX idx_model (model),
    INDEX idx_year (year),
    INDEX idx_price (price),
    INDEX idx_seller (seller_id)
);

-- Insert sample users (passwords are BCrypt hashed 'password123')
INSERT INTO users (full_name, email, password, phone, is_admin) VALUES
('John Doe', 'john@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '+1234567890', false),
('Jane Smith', 'jane@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '+1234567891', false),
('Admin User', 'admin@caratan.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '+1234567892', true)
ON DUPLICATE KEY UPDATE full_name = VALUES(full_name);

-- Insert sample cars
INSERT INTO cars (make, model, type, year, color, mileage, transmission, fuel_type, engine_capacity, cylinder, seats, price, description, location, seller_id) VALUES
('Toyota', 'Camry', 'Sedan', 2020, 'White', 50000, 'Automatic', 'Gasoline', '2500', '4', '5', 25000.00, 'Well maintained Toyota Camry with excellent fuel economy', 'Jakarta', 1),
('Honda', 'Civic', 'Sedan', 2019, 'Black', 45000, 'Automatic', 'Gasoline', '1800', '4', '5', 22000.00, 'Excellent condition Honda Civic, one owner', 'Jakarta', 1),
('BMW', 'X5', 'SUV', 2021, 'Blue', 30000, 'Automatic', 'Gasoline', '3000', '6', '7', 45000.00, 'Luxury BMW X5 in perfect condition, fully loaded', 'Jakarta', 2),
('Ford', 'Mustang', 'Coupe', 2018, 'Red', 35000, 'Manual', 'Gasoline', '5000', '8', '4', 28000.00, 'Classic Ford Mustang, great performance car', 'Jakarta', 2),
('Tesla', 'Model 3', 'Sedan', 2022, 'Silver', 15000, 'Automatic', 'Electric', '0', '0', '5', 42000.00, 'Tesla Model 3 with autopilot, very low mileage', 'Jakarta', 1),
('Mercedes', 'C-Class', 'Sedan', 2020, 'Black', 40000, 'Automatic', 'Gasoline', '2000', '4', '5', 35000.00, 'Luxury Mercedes C-Class, premium features', 'Jakarta', 2),
('Audi', 'A4', 'Sedan', 2019, 'White', 38000, 'Automatic', 'Gasoline', '2000', '4', '5', 32000.00, 'Audi A4 quattro, excellent handling', 'Jakarta', 1),
('Lexus', 'RX', 'SUV', 2021, 'Gray', 25000, 'Automatic', 'Hybrid', '2500', '4', '5', 48000.00, 'Lexus RX hybrid, great fuel efficiency', 'Jakarta', 2)
ON DUPLICATE KEY UPDATE make = VALUES(make);

-- Create indexes for better performance
CREATE INDEX idx_cars_make_model ON cars(make, model);
CREATE INDEX idx_cars_year_price ON cars(year, price);
CREATE INDEX idx_cars_created_at ON cars(created_at);

-- Show table structure
DESCRIBE users;
DESCRIBE cars;

-- Show sample data
SELECT 'Users:' as table_name;
SELECT id, full_name, email, is_admin, created_at FROM users;

SELECT 'Cars:' as table_name;
SELECT id, make, model, year, color, price, seller_id FROM cars; 