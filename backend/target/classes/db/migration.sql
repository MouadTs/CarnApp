-- Database Migration Script
-- Add missing columns to existing cars table

USE Car_DB;

-- Add missing columns if they don't exist
ALTER TABLE cars 
ADD COLUMN IF NOT EXISTS engine_capacity VARCHAR(10) DEFAULT '1500' AFTER fuel_type,
ADD COLUMN IF NOT EXISTS cylinder VARCHAR(10) DEFAULT '4' AFTER engine_capacity,
ADD COLUMN IF NOT EXISTS seats VARCHAR(5) DEFAULT '5' AFTER cylinder,
ADD COLUMN IF NOT EXISTS location VARCHAR(100) DEFAULT 'Jakarta' AFTER views;

-- Update existing records with default values if columns were just added
UPDATE cars SET 
    engine_capacity = '1500' WHERE engine_capacity IS NULL,
    cylinder = '4' WHERE cylinder IS NULL,
    seats = '5' WHERE seats IS NULL,
    location = 'Jakarta' WHERE location IS NULL;

-- Update specific cars with more realistic values
UPDATE cars SET 
    engine_capacity = '2500', cylinder = '4', seats = '5' WHERE make = 'Toyota' AND model = 'Camry';
UPDATE cars SET 
    engine_capacity = '1800', cylinder = '4', seats = '5' WHERE make = 'Honda' AND model = 'Civic';
UPDATE cars SET 
    engine_capacity = '3000', cylinder = '6', seats = '7' WHERE make = 'BMW' AND model = 'X5';
UPDATE cars SET 
    engine_capacity = '5000', cylinder = '8', seats = '4' WHERE make = 'Ford' AND model = 'Mustang';
UPDATE cars SET 
    engine_capacity = '0', cylinder = '0', seats = '5' WHERE make = 'Tesla' AND model = 'Model 3';
UPDATE cars SET 
    engine_capacity = '2000', cylinder = '4', seats = '5' WHERE make = 'Mercedes' AND model = 'C-Class';
UPDATE cars SET 
    engine_capacity = '2000', cylinder = '4', seats = '5' WHERE make = 'Audi' AND model = 'A4';
UPDATE cars SET 
    engine_capacity = '2500', cylinder = '4', seats = '5' WHERE make = 'Lexus' AND model = 'RX'; 