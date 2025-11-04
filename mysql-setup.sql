-- MySQL Database Setup for Personal Finance Manager
-- Run this script as MySQL root user to create the database and user

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS personal_finance_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Use the database
USE personal_finance_db;

-- Create a dedicated user for the application (optional, for better security)
-- CREATE USER IF NOT EXISTS 'finance_user'@'localhost' IDENTIFIED BY 'your_secure_password';
-- GRANT ALL PRIVILEGES ON personal_finance_db.* TO 'finance_user'@'localhost';
-- FLUSH PRIVILEGES;

-- Show database info
SELECT 'Database created successfully!' as Status;
SHOW DATABASES LIKE 'personal_finance_db';
